package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.Member;
import com.riverflow.livegoapp.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByUid(username);
        if (findMember == null) throw new UsernameNotFoundException("존재하지 않는 username 입니다.");

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return new User(findMember.getUid(), encoder.encode(findMember.getPasswd()), AuthorityUtils.createAuthorityList());

    }


}


