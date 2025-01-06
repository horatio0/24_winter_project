package com.example.winter_project_2024.Repository;

import com.example.winter_project_2024.Entity.RecentPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentPlayerRepository extends JpaRepository<RecentPlayer, Integer> {
}
