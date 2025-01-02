package com.example.winter_project_2024.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "me")
    private Set<Friend> friends = new HashSet<>();
}