package com.chotchip.task.service;

import com.chotchip.task.entity.User;
import com.chotchip.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }
}
