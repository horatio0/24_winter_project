package com.example.winter_project_2024.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FriendInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    private String sender;
    private String receiver;
}
