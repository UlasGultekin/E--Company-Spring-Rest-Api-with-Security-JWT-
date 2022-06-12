package com.herocompany.entities;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class CreditCard {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardnumber;
    private String password;
    private Long limit;



}
