package com.herocompany.repositories;

import com.herocompany.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Optional<Admin> findByPassword(String password);

    Optional<Admin> findByEmailEqualsIgnoreCase(String email);

}