package com.example.winter_project_2024.Service;

import com.example.winter_project_2024.DTO.UserDetailDTO;
import com.example.winter_project_2024.Entity.Member;
import com.example.winter_project_2024.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username).orElse(null);

        if(member == null) throw new UsernameNotFoundException("User not found");

        return new UserDetailDTO(member);
    }
}
