package com.example.winter_project_2024.Service;

import com.example.winter_project_2024.DTO.MemberDTO;
import com.example.winter_project_2024.DTO.MemberUpdateDTO;
import com.example.winter_project_2024.Entity.Member;
import com.example.winter_project_2024.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.SplittableRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    //C
    public String join(Member member){
        if(memberRepository.existsById(member.getMemberId())) return "이미 존재하는 아이디 입니다";
        else {
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
        } catch (NullPointerException e) {
            return "오류 : 존재하지 않는 아이디입니다.";
        }
    }

    //D
    public boolean delete(String id){
        try{
            memberRepository.delete(memberRepository.getReferenceById(id));
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
