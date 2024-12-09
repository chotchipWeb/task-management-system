package com.chotchip.task.service;

import com.chotchip.task.entity.enums.Role;
import com.chotchip.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<com.chotchip.task.entity.User> test = Optional.of( new com.chotchip.task.entity.User(1l, "email.com", "1223", Role.ADMIN, List.of()));
        Optional<com.chotchip.task.entity.User> byEmail = userRepository.findByEmail(username);
        if (byEmail.isPresent()) {
            com.chotchip.task.entity.User user = byEmail.get();
            return User.builder()
                    .username(username)
                    .password(user.getPassword())
                    .authorities(String.valueOf(user.getRole()))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }

    }
}
