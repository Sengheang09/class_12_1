package com.example.springsercurityjwt121.service;

import com.example.springsercurityjwt121.dto.LoginRequest;
import com.example.springsercurityjwt121.dto.LoginResponse;
import com.example.springsercurityjwt121.dto.MessageResponse;
import com.example.springsercurityjwt121.dto.RegisterRequest;
import com.example.springsercurityjwt121.entities.Role;
import com.example.springsercurityjwt121.entities.User;
import com.example.springsercurityjwt121.enums.RoleUser;
import com.example.springsercurityjwt121.exeption.BadRequestException;
import com.example.springsercurityjwt121.repo.RoleRepository;
import com.example.springsercurityjwt121.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository
    , RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public MessageResponse registerUser(RegisterRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->new BadRequestException("User already exists"));

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User userEntity = new User();

        Role role = roleRepository.findByName(RoleUser.ROLE_USER);

        userEntity.setUsername(request.getUsername());
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        userEntity.getRoles().add(role);

        userRepository.save(userEntity);

        return new MessageResponse("User registered successfully");
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return null;
    }
}
