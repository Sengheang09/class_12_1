package com.example.project_class111.service.Impl;

import com.example.project_class111.dto.RequestDto.UserRequestDto;
import com.example.project_class111.dto.ResponseDto.UserResponseDto;
import com.example.project_class111.entity.User;
import com.example.project_class111.exception.BadRequestException;
import com.example.project_class111.exception.ResourceNotFoundException;
import com.example.project_class111.mapper.UserMapper;
import com.example.project_class111.repo.UserRepository;
import com.example.project_class111.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new BadRequestException("User with email '" + userRequestDto.getEmail() + "' already exists");
        }

        User user = UserMapper.toEntity(userRequestDto);
        User saved = userRepository.save(user);
        return UserMapper.toResponseDto(saved);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!user.getEmail().equalsIgnoreCase(userRequestDto.getEmail())
                && userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new BadRequestException("User with email '" + userRequestDto.getEmail() + "' already exists");
        }

        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setRoel(userRequestDto.getRoel());

        User updated = userRepository.save(user);
        return UserMapper.toResponseDto(updated);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
