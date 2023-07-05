package com.example.contactbook.repositories;

import com.example.contactbook.entities.Role;
import com.example.contactbook.entities.enums.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(EnumRole name);
}
