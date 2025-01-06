package com.example.winter_project_2024.Service;

import com.example.winter_project_2024.DTO.MemberDTO;
import com.example.winter_project_2024.DTO.MemberUpdateDTO;
import com.example.winter_project_2024.DTO.MoneyDTO;
import com.example.winter_project_2024.Entity.Member;
import com.example.winter_project_2024.Repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    public Member getMember(String nickname){
        return memberRepository.getMemberByNickname(nickname);
    }

    public Set<String> getRecentPlayer(String myId){
        Set<String> recentPlayerId = new HashSet<>();
        memberRepository.getReferenceById(myId).getRecentPlayer().forEach(member -> {
            recentPlayerId.add(member.getMemberId());
        });
        return recentPlayerId; 
    }

    //C
    public String join(Member member){
        if(memberRepository.existsById(member.getMemberId())) return "이미 존재하는 아이디 입니다";
        else if(memberRepository.existsByNickname(member.getNickname())) return "이미 존재하는 닉네임 입니다.";
        else {
            member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
            member.setMoney(100);
            member.setTotalGame(0);
            member.setWin(0);
            member.setRole("ROLE_USER");
            memberRepository.save(member);
            return "회원가입이 완료되었습니다.";
        }
    }

    //R
    public MemberDTO getMemberInfo(String id){
        Member member = memberRepository.getReferenceById(id);
        return modelMapper.map(member, MemberDTO.class);
    }

    //U
    public String update(String memberId, MemberUpdateDTO memberUpdateDTO){
        try{
            Member member = memberRepository.getReferenceById(memberId);
            member.setNickname(memberUpdateDTO.getNickname());
            member.setName(memberUpdateDTO.getName());
            return "수정이 완료되었습니다.";
        } catch (NullPointerException | EntityNotFoundException e) {
            return "오류 : 존재하지 않는 아이디입니다.";
        }
    }

    //D
    public String delete(String id){
        try{
            memberRepository.delete(memberRepository.getReferenceById(id));
            return "회원탈퇴가 완료되었습니다";
        } catch (NullPointerException | EntityNotFoundException e) {
            return "오류 : 존재하지 않는 아이디입니다.";
        }
    }

    public MoneyDTO updateMoney(String id, int value){              //머니 구매 시
        MoneyDTO money = new MoneyDTO();
        Member member = memberRepository.getReferenceById(id);

        money.setBefore(member.getMoney());
        member.setMoney(money.getBefore()+value);
        money.setAfter(member.getMoney());

        return money;
    }

    public void setMoney(String id, int value){                     //게임 종료 후 머니 업데이트
        memberRepository.getReferenceById(id).setMoney(value);
    }

    public double getWinRate(String id){
        Member member = memberRepository.getReferenceById(id);
        return ((double) member.getWin() / member.getTotalGame());
    }
}
