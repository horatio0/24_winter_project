package com.example.winter_project_2024.Controller;

import com.example.winter_project_2024.DTO.MemberDTO;
import com.example.winter_project_2024.Entity.Member;
import com.example.winter_project_2024.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public String join(@RequestBody Member member){
        return memberService.join(member);
    }

    @GetMapping("/info")
    public MemberDTO info(String id){
        return memberService.getMemberInfo(id);
    }
}