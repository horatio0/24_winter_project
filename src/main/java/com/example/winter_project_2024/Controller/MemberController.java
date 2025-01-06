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
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/winrate")
    public HashMap<String, Double> getWinRate(@AuthenticationPrincipal UserDetails userDetails){
        HashMap<String, Double> winRate = new HashMap<>();
        winRate.put("winrate", memberService.getWinRate(userDetails.getUsername()));
        return winRate;
    }

    @GetMapping("/recent_player")
    public Set<String> getRecentPlayer(@AuthenticationPrincipal UserDetails userDetails){
        return memberService.getRecentPlayer(userDetails.getUsername());
    }

    @PostMapping("/member/join")
    public HashMap<String, String> join(@RequestBody Member member){
        HashMap<String, String> response = new HashMap<>();
        response.put("message", memberService.join(member));
        return response;
    }

    @GetMapping("/member/info")
    public MemberDTO info(@AuthenticationPrincipal UserDetails userDetails){
        return memberService.getMemberInfo(userDetails.getUsername());
    }

    @DeleteMapping("/member/delete")
    public HashMap<String, String> delete(@AuthenticationPrincipal UserDetails userDetails){
        HashMap<String, String> response = new HashMap<>();
        response.put("message", memberService.delete(userDetails.getUsername()));
        return response;
    }

    @PutMapping("/member/update")
    public HashMap<String, String> update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MemberUpdateDTO memberUpdateDTO){
        HashMap<String, String> response = new HashMap<>();
        response.put("message", memberService.update(userDetails.getUsername(), memberUpdateDTO));
        return response;
    }

    @PutMapping("/member/update/money")
    public MoneyDTO updateMoney(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int value){
        return memberService.updateMoney(userDetails.getUsername(), value);
    }
}