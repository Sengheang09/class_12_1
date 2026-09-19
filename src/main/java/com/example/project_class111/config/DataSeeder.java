package com.example.project_class111.config;

import com.example.project_class111.entity.Role;
import com.example.project_class111.entity.User;
import com.example.project_class111.enums.RoleUser;
import com.example.project_class111.repo.RoleRepository;
import com.example.project_class111.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role userRole = roleRepository.findByName(RoleUser.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(RoleUser.ROLE_USER)));
            Role adminRole = roleRepository.findByName(RoleUser.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(RoleUser.ROLE_ADMIN)));

            userRepository.findByUsername("admin").orElseGet(() -> {
                User user = new User();
                user.setUsername("admin");
                user.setEmail("admin@gmail.com");
                user.setPassword(passwordEncoder.encode("123456789"));
                user.getRoles().add(userRole);
                user.getRoles().add(adminRole);
                return userRepository.save(user);
            });
        };
    }
}
