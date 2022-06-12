package com.herocompany.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
public class Category extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name can't be blank")
    @Length(message = "Category name must contain min 2 max  50 character.", min = 2, max = 50)
    private String categoryName;

//    private String detail;

//    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL) //,fetch = FetchType.LAZY,cascade = CascadeType.REFRESH
//    private  List<Product> productList;
}
