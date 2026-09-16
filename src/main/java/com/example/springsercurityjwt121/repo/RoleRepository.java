package com.example.springsercurityjwt121.repo;

import com.example.springsercurityjwt121.entities.Role;
import com.example.springsercurityjwt121.enums.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(RoleUser name);

}
