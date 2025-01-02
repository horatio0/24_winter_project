package com.example.winter_project_2024.Controller;

import com.example.winter_project_2024.Entity.Friend;
import com.example.winter_project_2024.Service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/friend/invite")
    public HashMap<String, String> inviteFriend(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String friendId){
        HashMap<String, String> response = new HashMap<>();
        response.put("message", friendService.inviteFriend(userDetails.getUsername(), friendId));
        return response;
    }

    @GetMapping("/friend/invite")
    public List<String> getInvite(@AuthenticationPrincipal UserDetails userDetails){
        return friendService.getInvite(userDetails.getUsername());
    }

    @DeleteMapping("/friend/invite")
    public HashMap<String, String> acceptInvite(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String sender){
        HashMap<String, String> response = new HashMap<>();
        response.put("message", friendService.acceptInvite(userDetails.getUsername(), sender));
        return response;
    }

    @GetMapping("/friend")
    public Set<Friend> getFriend(@AuthenticationPrincipal UserDetails userDetails){
        return friendService.getFriend(userDetails.getUsername());
    }
}
