package com.chotchip.task.service;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.entity.User;
import com.chotchip.task.entity.enums.Role;
import com.chotchip.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Transactional
    public void create(UserRequestDTO userRequestDTO) {
        userRepository.save(new User(null, userRequestDTO.getEmail(),
                userRequestDTO.getPassword(), Role.CLIENT, List.of()));
    }
}
