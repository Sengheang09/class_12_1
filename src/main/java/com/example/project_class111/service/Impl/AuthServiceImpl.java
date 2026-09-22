package com.example.project_class111.service.Impl;

import com.example.project_class111.dto.RequestDto.LoginRequest;
import com.example.project_class111.dto.RequestDto.RegisterRequest;
import com.example.project_class111.dto.ResponseDto.LoginResponse;
import com.example.project_class111.dto.ResponseDto.MessageResponse;
import com.example.project_class111.entity.LoginAttempt;
import com.example.project_class111.entity.Role;
import com.example.project_class111.entity.User;
import com.example.project_class111.enums.RoleUser;
import com.example.project_class111.exception.BadRequestException;
import com.example.project_class111.repo.LoginAttemptRepository;
import com.example.project_class111.repo.RoleRepository;
import com.example.project_class111.repo.UserRepository;
import com.example.project_class111.security.JwtService;
import com.example.project_class111.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;

    @Value("${security.lock-duration-minutes:5}")
    private long lockDurationMinutes;

    @Override
    @Transactional
    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Role userRole = roleRepository.findByName(RoleUser.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleUser.ROLE_USER)));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();

        user.getRoles().add(userRole);

        userRepository.save(user);

        return new MessageResponse("User registered successfully");
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String clientIp = getClientIp();
        checkIpLock(clientIp);

        User user;
        try {
            user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            handleFailedLogin(clientIp);
            return null;
        } catch (LockedException e) {
            throw new BadRequestException("User account is locked. Please contact admin.");
        }

        resetFailedAttempts(clientIp);

        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return new LoginResponse(jwtService.generateToken(user), user.getUsername(), roles);
    }

    private void checkIpLock(String ipAddress) {
        LoginAttempt attempt = loginAttemptRepository.findByIpAddress(ipAddress).orElse(null);
        if (attempt == null) return;

        if (attempt.getLockTime() != null) {
            LocalDateTime unlockTime = attempt.getLockTime().plusMinutes(lockDurationMinutes);
            if (LocalDateTime.now().isBefore(unlockTime)) {
                long waitMinutes = Duration.between(LocalDateTime.now(), unlockTime).toMinutes() + 1;

                throw new BadRequestException("3 failed attempts. Please try again in " + waitMinutes + " minute(s).");
            }
            attempt.setFailedAttempts(0);
            attempt.setLockTime(null);
            loginAttemptRepository.save(attempt);
        }
    }

    private void handleFailedLogin(String ipAddress) {
        LoginAttempt attempt = loginAttemptRepository.findByIpAddress(ipAddress)
                .orElseGet(() -> LoginAttempt.builder()
                        .ipAddress(ipAddress)
                        .failedAttempts(0)
                        .build());

        int attempts = attempt.getFailedAttempts() + 1;
        attempt.setFailedAttempts(attempts);
        attempt.setLastAttemptTime(LocalDateTime.now());

        if (attempts >= 3) {
            attempt.setLockTime(LocalDateTime.now());
            loginAttemptRepository.save(attempt);
            throw new BadRequestException("Account locked for this IP! You entered the wrong password 3 times. Try again after " + lockDurationMinutes + " minute(s).");
        }

        loginAttemptRepository.save(attempt);
        throw new BadRequestException("You have " + (3 - attempts) + " attempts.");
    }

    private void resetFailedAttempts(String ipAddress) {
        loginAttemptRepository.findByIpAddress(ipAddress).ifPresent(attempt -> {
            if (attempt.getFailedAttempts() > 0 || attempt.getLockTime() != null) {
                attempt.setFailedAttempts(0);
                attempt.setLockTime(null);
                loginAttemptRepository.save(attempt);
            }
        });
    }

    private String getClientIp() {
        String xfHeader = httpServletRequest.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty() && ! "unknown".equalsIgnoreCase(xfHeader)) {
            return xfHeader.split(",")[0].trim();
        }

        String xRealIp = httpServletRequest.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp.trim();
        }

        String remoteAddr = httpServletRequest.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            return "127.0.0.1";
        }
        return remoteAddr;

    }
}