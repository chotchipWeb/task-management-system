package com.chotchip.task.service;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.dto.response.UserResponseDTO;
import com.chotchip.task.entity.User;
import com.chotchip.task.execption.UserInvalidCredentialsException;
import com.chotchip.task.execption.UserNotFoundException;
import com.chotchip.task.mapper.UserMapper;
import com.chotchip.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        String email = userRequestDTO.getEmail();
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
           throw new UserInvalidCredentialsException(email);
        }
        User entity = userMapper.toEntity(userRequestDTO);
        return userMapper.toDTO(userRepository.save(entity));
    }
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository
                .findById(id).orElseThrow(() ->
                        new UserNotFoundException(id));
        return userMapper.toDTO(user);
    }
}
