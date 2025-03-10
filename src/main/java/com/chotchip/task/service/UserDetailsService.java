package com.chotchip.task.service;

import com.chotchip.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
