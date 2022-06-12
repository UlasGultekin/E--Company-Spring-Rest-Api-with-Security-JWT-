package com.herocompany.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Product extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product Name can't be blank")
    @Length(message = "Product Name must contain min 2 max  50 character.", min = 2, max = 50)
    private String productName;

    @NotBlank(message = "Detail can't be blank")
    @Length(message = "Company name must contain min 2 max  50 character.", min = 2, max = 50)
    private String detail;

    @Range(message = "price can be between 0 and 99999", min = 0, max = 99999)
    private int price;

    @ManyToOne

    @JoinColumn(name="category_id",referencedColumnName = "id")
    private Category category;

    @PositiveOrZero
    @Column(nullable = false)
    private int Stock;



}
