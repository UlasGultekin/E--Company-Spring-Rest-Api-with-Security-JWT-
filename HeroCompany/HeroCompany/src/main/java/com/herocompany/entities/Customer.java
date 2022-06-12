package com.herocompany.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.hibernate.validator.constraints.CodePointLength;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
public class Customer extends Base {
    //aşağıdaki attr'ler olmak zorunda
    // UserDetailService içinde bunlara ait methodlar var genellikle bütün login işlemlerinde bu attr ler kullanılır
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Firstname can't be blank")
    @Length(message = "Firstname must be between 3 and 50 characters", min = 3, max = 50)
    private String firstName;
    @NotBlank(message = "Lastname can't be blank")
    @Length(message = "Lastname must be between 3 and 50 characters", min = 3, max = 50)
    private String lastName;
    @NotBlank(message = "Phone can't be blank")
//    @Length(message = "Phone must be between 3 and 50 characters", min = 3, max = 10)

    private String phone;

    @NotNull(message = "Email not null")
    @Email(message = "Email should be valid")
    private String email;


//    @Length(message = "Maximum 10 min 3",min = 5, max = 10)
    @NotBlank(message = "password can not be blank")
   @Pattern(message = "Password must contain min one upper,lower letter and 0-9 digit number ",
    regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{6,}")
    private String password;
    private boolean enabled;
    private boolean tokenExpired;




    @ManyToOne
    //@JsonIgnore
    @JoinColumn(name = "role_Id",referencedColumnName = "id")
    private Role role;


@Column(name = "reset_password_token")
private String resetPasswordToken;

}
