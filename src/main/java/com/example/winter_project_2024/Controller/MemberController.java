package com.example.winter_project_2024.Controller;

import com.example.winter_project_2024.DTO.MemberDTO;
import com.example.winter_project_2024.DTO.MemberUpdateDTO;
import com.example.winter_project_2024.DTO.MoneyDTO;
import com.example.winter_project_2024.Entity.Member;
import com.example.winter_project_2024.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;


    @PostMapping("/join")
    public HashMap<String, String> join(@RequestBody Member member){
        HashMap<String, String> response = new HashMap<>();
        response.put("message", memberService.join(member));
        return response;
    }

    @GetMapping("/info")
    public MemberDTO info(@AuthenticationPrincipal UserDetails userDetails){
        return memberService.getMemberInfo(userDetails.getUsername());
    }

    @DeleteMapping("/delete")
    public HashMap<String, String> delete(@AuthenticationPrincipal UserDetails userDetails){
        HashMap<String, String> response = new HashMap<>();
        response.put("message", memberService.delete(userDetails.getUsername()));
        return response;
    }

    @PutMapping("/update")
    public HashMap<String, String> update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MemberUpdateDTO memberUpdateDTO){
        HashMap<String, String> response = new HashMap<>();
        response.put("message", memberService.update(userDetails.getUsername(), memberUpdateDTO));
        return response;
    }

    @PutMapping("/update/money")
    public MoneyDTO updateMoney(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int value){
        return memberService.updateMoney(userDetails.getUsername(), value);
    }
}