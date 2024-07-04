package com.kevin.dinofly.security;

import com.kevin.dinofly.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserMapper userMapper;



    public CustomUserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        CustomUserDetails user = userMapper.findUserDetailsById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Method not supported. Use loadUserById instead.");
    }
}
