package com.example.winter_project_2024.Service;

import com.example.winter_project_2024.Entity.Friend;
import com.example.winter_project_2024.Entity.FriendInvite;
import com.example.winter_project_2024.Entity.Member;
import com.example.winter_project_2024.Repository.FriendInviteRepository;
import com.example.winter_project_2024.Repository.FriendRepository;
import com.example.winter_project_2024.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendInviteRepository inviteRepository;
    private final MemberRepository memberRepository;

    public Set<Friend> getFriend(String myId){
        return memberRepository.getReferenceById(myId).getFriends();
    }

    public String acceptInvite(String myId, String senderId){
        try {
            Member me = memberRepository.getReferenceById(myId);
            Member sender = memberRepository.getReferenceById(senderId);

            inviteRepository.inviteAccept(myId, senderId);

            Friend friend = new Friend();
            friend.setMe(me);
            friend.setFriendNickname(sender.getNickname());
            friendRepository.save(friend);

            friend.setMe(sender);
            friend.setFriendNickname(me.getNickname());
            friendRepository.save(friend);

            return "요청이 수락되었습니다";
        } catch (Exception e) {
            log.info(e.getMessage());
            return "오류가 발생했습니다.";
        }
    }

    public List<String> getInvite(String myId){
        return inviteRepository.findByMyID(myId);
    }

    public String inviteFriend(String myId, String friendId){
        try {
            FriendInvite invite = new FriendInvite();
            invite.setSender(myId);
            invite.setReceiver(friendId);
            inviteRepository.save(invite);
            return "친구 초대를 보냈습니다.";
        } catch (Exception e)  {
            return "오류가 발생했습니다.";
        }
    }
}
