package com.example.winter_project_2024.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Builder
public class Participant {
    private String nickname;
    private WebSocketSession session;
    private int money;
    @Setter
    private boolean allInOrDie;
    @Setter
    private int card;

    public void setBet(int value){
        this.money -= value;
    }
    public void setMoney(int value){
        this.money += value;
    }
}
