package com.mobsolution.spring_app.service.impl;


import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mobsolution.spring_app.domain.model.User;
import com.mobsolution.spring_app.domain.repository.UserRepository;
import com.mobsolution.spring_app.service.userService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  userService  {
   

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário inválido!"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole()));
    }



    
}