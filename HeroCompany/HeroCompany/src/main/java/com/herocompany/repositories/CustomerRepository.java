package com.herocompany.repositories;

import com.herocompany.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmailEqualsIgnoreCase(String email);




    //Optional<Customer> findByPasswordEquals(String password);

    Optional<Customer> findByResetPasswordToken(String resetPasswordToken);

    Optional<Customer> findByEmail(String email);




}