package com.example.winter_project_2024.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Member{
    @Id
    private String memberId;
    private String password;
    private String name;
    private int money;
    private String nickname;
    private String role;
}