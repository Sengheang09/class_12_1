package com.example.springsercurityjwt121.service;

import com.example.springsercurityjwt121.dto.LoginRequest;
import com.example.springsercurityjwt121.dto.LoginResponse;
import com.example.springsercurityjwt121.dto.MessageResponse;
import com.example.springsercurityjwt121.dto.RegisterRequest;
import com.example.springsercurityjwt121.entities.Role;
import com.example.springsercurityjwt121.entities.User;
import com.example.springsercurityjwt121.enums.RoleUser;
import com.example.springsercurityjwt121.exeption.BadRequestException;
import com.example.springsercurityjwt121.exeption.ResourceNotFoundException;
import com.example.springsercurityjwt121.repo.RoleRepository;
import com.example.springsercurityjwt121.repo.UserRepository;
import com.example.springsercurityjwt121.security.CustomUserDetailService;
import com.example.springsercurityjwt121.security.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.Duration.between;
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            CustomUserDetailService userDetailsService,
            JwtService jwtService

    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Value("${security.lock-duration-minutes}")
    private int lockDurationMinutes;

    @Value("${jwt.secret}")
    private String secret;
    private SecretKey secretKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public MessageResponse registerUser(RegisterRequest request) {

        if(userRepository.existsByUsername(request.getUsername())){
            throw new BadRequestException("Username already exists");
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User userEntity = new User();

        Role role = roleRepository.findByName(RoleUser.ROLE_USER);

        userEntity.setUsername(request.getUsername());
        userEntity.setEmail(request.getEmail());
        userEntity.setAccountNonLocked(true);
        userEntity.setLockDate(null);
        userEntity.setAttempt(0);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        userEntity.getRoles().add(role);

        userRepository.save(userEntity);

        return new MessageResponse("User registered successfully");
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->new ResourceNotFoundException("User not found"));

        checkLockAccount(user);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        }catch (Exception e) {
            validateAttempt(user);
            return null;
        }

//        reSetLock(user);
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//
//        String token = jwtService.generateToken(userDetails);
        String token = Jwts.builder()
                .subject(user.getUsername())
                .claim("Roles", user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(secretKey())
                .compact();

        List<String> roles =user.getRoles().stream().map(role -> role.getName().name()).toList();

        return new LoginResponse(
                user.getUsername(),
                token,
                roles
        );

    }

    private void checkLockAccount(User user) {

        if(user.isAccountNonLocked()) return;

        if (user.getLockDate().plusMinutes(lockDurationMinutes).isBefore(LocalDateTime.now())) {

            user.setAccountNonLocked(true);
            user.setLockDate(null);
            user.setAttempt(0);
            userRepository.save(user);

            return;
        }

        long lockTime = (user.getLockDate() != null)
                ? between(
                        LocalDateTime.now(), user.getLockDate().plusMinutes(lockDurationMinutes)
                ).toMinutes() + 1 : lockDurationMinutes;


        throw new BadRequestException("User account is locked "+lockTime+" minutes");

    }

    private void validateAttempt(User user){
        int attempts = user.getAttempt() + 1;
        user.setAttempt(attempts);

        if(attempts >= 3){
            user.isAccountNonLocked(false);
            user.setLockDate(LocalDateTime.now());
            userRepository.save(user);
            throw new BadRequestException("User account is locked "+lockDurationMinutes+" minutes");
        }

        throw new BadRequestException("you have "+(3-attempts)+" attempts for login.");


    }

    private void reSetLock(User user){
        user.isAccountNonLocked(true);
        user.setLockDate(null);
        user.setAttempt(0);
        userRepository.save(user);
    }
}
