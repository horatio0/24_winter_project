package com.example.winter_project_2024.Handler;

import com.example.winter_project_2024.DTO.MemberDTO;
import com.example.winter_project_2024.DTO.MessageDTO;
import com.example.winter_project_2024.DTO.ParsingDTO;
import com.example.winter_project_2024.Entity.Participant;
import com.example.winter_project_2024.Entity.Room;
import com.example.winter_project_2024.Repository.RoomRegistry;
import com.example.winter_project_2024.Service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class IndianPokerHandler extends TextWebSocketHandler {

    private final RoomRegistry roomRegistry = new RoomRegistry();
    private final ConcurrentHashMap<String, String> sessionToRoom = new ConcurrentHashMap<>();
    private final MemberService memberService;


    public IndianPokerHandler(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Authentication authentication = (Authentication) session.getAttributes().get("auth");
        log.info(authentication.getName());

        String roomId = getRoomIdFromSession(session.getUri());                                 // 세션으로 부터 roomId를 가져와
        log.info("이것이 당신의 roomId 입니다 : {}", roomId);

        if (roomId == null) session.close();
        else{
            Room room = roomRegistry.getOrCreateRoom(roomId, session.getId());                      // roomId를 이용해 room객체를 검색하고 없으면 만들어
            try {
                MemberDTO member = memberService.getMemberInfo(Objects.requireNonNull(session.getPrincipal()).getName());       // 세션에 담긴 spring security 인증 정보를 이용해 사용자 정보를 가져와
                room.addMember(member, session);                                                        // room에 참가자를 추가해
                sessionToRoom.put(session.getId(), roomId);                                             // sessionId와 roomId를 매핑해

                broadcast(room, member.getNickname() + "님이 입장하셨습니다.", 0, null);                // 입장 멘트 브로드캐스팅
                log.info("브로트 캐스트 완");
            } catch (NullPointerException e) {              // NullPoint 발생 불가능 하지만 일단 처리
                log.info("바로 null point 정상화");
                log.info(e.getMessage());
                broadcast(room, "Err_UserNotFound__", 404, "error");
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Room room = roomRegistry.getRoom(sessionToRoom.get(session.getId()));                   // sessionId를 이용해 room 객체를 가져와
        String payload = message.getPayload();                                                  // 프론트에서 온 message를 가져와
        ObjectMapper objectMapper = new ObjectMapper();                                         // objectmapper : json string converter
        ParsingDTO parsingDTO = objectMapper.readValue(payload, ParsingDTO.class);              // 프린트에서 온 json message를 객체로 매핑

        switch (parsingDTO.getType().toLowerCase()) {                                           // 프론트에서 뭘 요구했나
            case "start" :
                room.setGameState(1);
                room.setTotalAmount(0);
                broadcast(room, "게임을 시작합니다!", 0, null);
                shuffle(room);
                break;
            case "bet" :                                                                        // 베팅했구나
                if (room.getGameState()==0) break;
                betting(room, session.getId(), parsingDTO.getValue());                          // 베팅 메소드 호출
                break;
            case "get_winner" :
                if (room.getGameState()==0) break;
                getWinner(room);
                break;
            case "die" :
                if (room.getGameState()==0) break;
                die(room, session.getId());
                break;
            case "all_in" :
                if (room.getGameState()==0) break;
                allIn(room, session.getId());
                break;
            case "end" :
                room.setGameState(0);
                room.setTotalAmount(0);
                broadcast(room, "게임이 종료되었습니다!", 0, null);
                break;
            case "send_message" :
                broadcast(room, parsingDTO.getMessage(), 0, null);
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        String roomId = getRoomIdFromSession(session.getUri());
        Room room = roomRegistry.getRoom(roomId);
        try{
            memberService.setMoney(Objects.requireNonNull(session.getPrincipal()).getName(), room.getMemberInfo(session.getId()).getMoney());
            room.removeMember(session.getId());

            broadcast(room, room.getMemberInfo(session.getId()).getNickname() + "님이 나갔습니다.", 0, null);

            sessionToRoom.remove(session.getId());
            if(session.getId().equals(room.getBoss())) broadcast(room, room.newBoss(), 0, null);
            if(room.getMember().isEmpty()) roomRegistry.removeRoom(roomId);
        } catch (NullPointerException e){
            broadcast(room, "Err_UserNotFound__", 404, "error");
        }
    }

    private void shuffle(Room room){
        int card;
        for (Participant p : room.getMember().values()){
            card = (int)(Math.random()*9+1);
            p.setCard(card);
        }
    }

    private void die(Room room, String sessionId) throws JsonProcessingException{
        Participant p = room.getMemberInfo(sessionId);
        if(p==null) broadcast(room, "Err_UserNotFound__", 404, "error");
        else broadcast(room, p.getNickname() + "님이 DIE를 외쳤습니다! 들고 있던 숫자는 : " + p.getCard() + " 입니다!", 0, null);
    }

    private void getWinner(Room room) throws JsonProcessingException{
        int max=0;
        Participant winner = null;
        boolean isSameScore = false;
        for (Participant p : room.getMember().values()){
            broadcast(room, p.getNickname() + "님은 " + p.getCard() + " 카드를 들고있었습니다!", 0, null);
            if(p.getCard() > max) winner = p;
        }
        for (Participant p : room.getMember().values()){
            if (p.getCard() == max && !(p.getNickname().equals(winner))) {
                isSameScore = true;
                break;
            }
        }
        if (isSameScore){
            broadcast(room, "동점이 존재합니다! 베팅 금액은 다음 게임의 승자가 가져갑니다!", room.getTotalAmount(), "win");
        } else {
            winner.setMoney(room.getTotalAmount());
            room.setTotalAmount(0);
            broadcast(room, "승리자 " + winner.getNickname() + "님!", 0, null);
        }
        shuffle(room);
    }

    private void allIn(Room room, String sessionId) throws JsonProcessingException{
        Participant p = room.getMemberInfo(sessionId);
        if (p==null) broadcast(room, "Err_UserNotFound__", 404, "error");
        else{
            room.setTotalAmount(room.getTotalAmount()+p.getMoney());
            p.setBet(p.getMoney());
            broadcast(room, p.getNickname() + "님이 올인했습니다!", 0, null);
        }
    }

    private void betting(Room room, String sessionId, int value) throws JsonProcessingException {                               // 베팅 메소드
        Participant p = room.getMemberInfo(sessionId);                                          // 세션 아이디를 통해 참가자 정보 조회
        if(p==null) broadcast(room, "Err_UserNotFound__", 404, "error");
        else {
            p.setBet(value);                                                                        // 참가자 bet 변수 설정
            room.setTotalAmount(room.getTotalAmount()+value);
            broadcast(room, p.getNickname() + "님이" + value + "원을 베팅했습니다.", room.getTotalAmount(), "bet"); // 베팅 브로드캐스트
        }
    }

    private String getRoomIdFromSession(URI uri) {
        return UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("roomId");
    }

    private void broadcast(Room room, String msg, int val, String type) throws JsonProcessingException {

        MessageDTO message = MessageDTO.builder()
                .type(type)
                .message(msg)
                .value(val)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(message);

        room.getMember().values().stream()
                .map(Participant::getSession)
                .filter(WebSocketSession::isOpen)
                .forEach(s -> {
                    try {
                        s.sendMessage(new TextMessage(json));
                    } catch (IOException e) {
                        log.error(" > 메세지 전송 실패 - 세션 ID : {}", s.getId());
                    }
                });
    }
}
