package com.example.project_class111.repo;

import com.example.project_class111.entity.Role;
import com.example.project_class111.enums.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleUser name);
}
