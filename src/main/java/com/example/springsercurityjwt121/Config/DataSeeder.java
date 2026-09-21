package com.example.springsercurityjwt121.Config;

import com.example.springsercurityjwt121.entities.Role;
import com.example.springsercurityjwt121.entities.User;
import com.example.springsercurityjwt121.enums.RoleUser;
import com.example.springsercurityjwt121.repo.RoleRepository;
import com.example.springsercurityjwt121.repo.UserRepository;
import lombok.Builder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Builder
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ){
        return args -> {

            Role roleUser = roleRepository.findByName(RoleUser.ROLE_USER);
            Role roleAdmin = roleRepository.findByName(RoleUser.ROLE_ADMIN);

            if(!userRepository.existsByUsername("admin")){

                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");
                admin.setAccountNonLocked(true);
                admin.setAttempt(0);
                admin.setLockDate(null);
                admin.setPassword(passwordEncoder.encode("123456789"));

                admin.getRoles().add(roleUser);
                admin.getRoles().add(roleAdmin);

                userRepository.save(admin);

            }
        };
    }

}
