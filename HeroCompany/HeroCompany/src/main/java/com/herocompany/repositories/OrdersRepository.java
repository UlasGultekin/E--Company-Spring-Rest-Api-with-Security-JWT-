package com.herocompany.repositories;

import com.herocompany.entities.Basket;
import com.herocompany.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}