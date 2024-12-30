package com.example.winter_project_2024.Entity;

import com.example.winter_project_2024.DTO.MemberDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Getter
@Setter
public class Room {
    private String roomId;
    private Map<String, Participant> member = new HashMap<>();
    private String boss;

    private int totalAmount;
    private int gameState;

    public String newBoss() {
        if (member.isEmpty()) {
            return "No participants available to become the boss.";
        }
        boss = member.values().iterator().next().getNickname();
        return "방장이 " + boss + "님으로 변경되었습니다.";
    }

    public Participant getMemberInfo(String sessionId) {
        return member.get(sessionId); // sessionId로 직접 조회
    }

    public void addMember(MemberDTO memberDTO, WebSocketSession session) {
        Participant participant = Participant.builder()
                .card(0)
                .money(memberDTO.getMoney())
                .nickname(memberDTO.getNickname())
                .session(session)
                .build();
        member.put(session.getId(), participant); // sessionId를 키로 추가
    }

    public void removeMember(String sessionId) {
        member.remove(sessionId); // sessionId로 직접 삭제
    }
}

//@Getter
//@Setter
//public class Room {
//    private String roomId;
//    private List<Participant> member = new ArrayList<>();
//    private List<String> index = new ArrayList<>();
//    private String boss;
//
//    private int totalAmount;
//    private int gameState;
//
//    public String newBoss() {
//        boss = member.getFirst().getNickname();
//        return "방장이" + boss + "님으로 변경되었습니다.";
//    }
//
//    public Participant getMemberInfo(String sessionId) throws NullPointerException{
//        return member.get(index.indexOf(sessionId));
//    }
//
//    public void addMember(MemberDTO memberDTO, WebSocketSession session) {
//        Participant participant = Participant.builder()
//                .card(0)
//                .money(memberDTO.getMoney())
//                .nickname(memberDTO.getNickname())
//                .session(session)
//                .build();
//        member.add(participant);
//        index.add(session.getId());
//    }
//
//    public void removeMember(String sessionId) {
//        member.remove(index.indexOf(sessionId));
//        index.remove(sessionId);
//    }
//}
