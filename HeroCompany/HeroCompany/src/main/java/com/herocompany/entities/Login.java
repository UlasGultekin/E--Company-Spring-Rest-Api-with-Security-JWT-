package com.herocompany.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class Login {
    //username pass al jwtyi üret sonuç olarak
    @Length(message = "Maximum 60", max = 60)
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email Format Error")
    private String username;

    @Length(message = "Maximum 5 min 10",min = 5, max = 10)
    @NotBlank(message = "password can not be blank")
//    @Pattern(message = "Password must contain min one upper,lower letter and 0-9 digit number ",
//            regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$)")
    private String password;
}
