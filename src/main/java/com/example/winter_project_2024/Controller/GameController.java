package com.example.winter_project_2024.Controller;

import com.example.winter_project_2024.Repository.RoomRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final RoomRegistry roomRegistry;

    @GetMapping("/rooms")
    public Set<String> getIndianPokerRooms(){
        return roomRegistry.getRooms();
    }
}
