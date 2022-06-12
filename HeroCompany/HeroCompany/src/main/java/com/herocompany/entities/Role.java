package com.herocompany.entities;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; //role ün adı ROLE_user gibi.

//    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    List<Admin> admins;
//
//    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    List<Customer> customers;
    //user hazırlanırken rollere gitmete çalışıyor . defaultta eagledır. circle a neden oldu .dikkate alma.
    //biz prog ayağa kaldırdığımızda ilk etapta jwt user ı çağıracağımızdan mapped by ı buraya yazıyoruz.

}
