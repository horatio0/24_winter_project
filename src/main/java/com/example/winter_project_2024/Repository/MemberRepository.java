package com.example.winter_project_2024.Repository;

import com.example.winter_project_2024.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}