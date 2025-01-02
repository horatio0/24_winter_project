package com.example.winter_project_2024.Repository;

import com.example.winter_project_2024.Entity.FriendInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendInviteRepository extends JpaRepository<FriendInvite, String> {
    @Query(value = "select sender from FriendInvite where receiver = :myId")
    public List<String> findByMyID(@Param(value = "myId")String myId);

    @Query(value = "delete from FriendInvite where receiver = :myid and sender = :sender")
    public void inviteAccept(@Param(value = "myId") String myId, @Param(value = "sender") String sender);

}
