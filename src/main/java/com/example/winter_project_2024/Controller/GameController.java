package com.example.winter_project_2024.Controller;

import com.example.winter_project_2024.Repository.RoomRegistry;
import com.example.winter_project_2024.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final RoomRegistry roomRegistry;
    private final MemberService memberService;



    @GetMapping("/rooms")
    public Set<String> getIndianPokerRooms(){
        return roomRegistry.getRooms();
    }
}
