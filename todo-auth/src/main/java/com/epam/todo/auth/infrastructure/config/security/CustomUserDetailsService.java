package com.epam.todo.auth.infrastructure.config.security;

import com.epam.todo.auth.infrastructure.auth.model.User;
import com.epam.todo.auth.infrastructure.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username: <{}>", username);

        User user = userRepository.findByUsername(username);
//        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        CustomUserDetails userDetails = new CustomUserDetails();
//        userDetails.setUsername("user");
//        userDetails.setPassword(passwordEncoder.encode("password"));
//        if (username.equals(userDetails.getUsername())) {
//            return userDetails;
//        }

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setUserId(user.getId());
        userDetails.setTalentId(user.getTalentId());
        return userDetails;
    }
}
