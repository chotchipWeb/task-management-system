package com.chotchip.task.service;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.entity.User;
import com.chotchip.task.execption.UserNotFoundException;
import com.chotchip.task.mapper.UserMapper;
import com.chotchip.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(email));
    }

    @Transactional
    public void create(UserRequestDTO userRequestDTO) {
        userRepository.save(userMapper.toEntity(userRequestDTO));
    }
}
