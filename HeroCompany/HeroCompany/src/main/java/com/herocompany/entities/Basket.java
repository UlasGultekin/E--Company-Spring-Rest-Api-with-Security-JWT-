package com.herocompany.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Basket extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_Id")
    private Customer customer;

    boolean status= false;

    private int quantity;
}
