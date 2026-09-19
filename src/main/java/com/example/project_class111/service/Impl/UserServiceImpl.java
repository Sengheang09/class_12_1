package com.example.project_class111.service.Impl;

import com.example.project_class111.dto.RequestDto.LoginRequest;
import com.example.project_class111.dto.RequestDto.RegisterRequest;
import com.example.project_class111.dto.RequestDto.UserRequestDto;
import com.example.project_class111.dto.ResponseDto.LoginResponse;
import com.example.project_class111.dto.ResponseDto.MessageResponse;
import com.example.project_class111.dto.ResponseDto.UserResponseDto;
import com.example.project_class111.entity.Role;
import com.example.project_class111.entity.User;
import com.example.project_class111.enums.RoleUser;
import com.example.project_class111.exception.BadRequestException;
import com.example.project_class111.exception.ResourceNotFoundException;
import com.example.project_class111.mapper.UserMapper;
import com.example.project_class111.repo.RoleRepository;
import com.example.project_class111.repo.UserRepository;
import com.example.project_class111.security.JwtService;
import com.example.project_class111.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public MessageResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already in use");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        Role roleUser = roleRepository.findByName(RoleUser.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleUser.ROLE_USER)));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(roleUser);

        userRepository.save(user);

        return new MessageResponse("User created successfully");
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if (!userRepository.existsByUsername(request.getUsername())) {
            throw new BadCredentialsException("Username not found");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Wrong password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtService.generateToken(userDetails);

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return new LoginResponse(
                userDetails.getUsername(),
                token,
                roles
        );
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new BadRequestException("User with email '" + userRequestDto.getEmail() + "' already exists");
        }

        User user = UserMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        Role roleUser = roleRepository.findByName(RoleUser.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleUser.ROLE_USER)));
        user.getRoles().add(roleUser);

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
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }

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
