package com.herocompany.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Entity
@Data
public class Orders extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @OneToMany
    @JoinTable(name = "Order_Basket",joinColumns = @JoinColumn(name = "order_Id"),
                    inverseJoinColumns = @JoinColumn(name = "basket_id",referencedColumnName = "id"))
    private List<Basket> baskets;

    private int total;
}
