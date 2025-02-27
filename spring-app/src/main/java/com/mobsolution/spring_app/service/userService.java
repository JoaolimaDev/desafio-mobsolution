package com.mobsolution.spring_app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface userService {

    
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}