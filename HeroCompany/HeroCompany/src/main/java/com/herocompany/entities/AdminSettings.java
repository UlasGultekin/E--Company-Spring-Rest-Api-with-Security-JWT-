package com.herocompany.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
public class AdminSettings {
    @NotBlank(message = "Admin name can not be blank")
    @Length(message = "Admin name must contain min 2 max  5O character.", min = 2, max = 50)
    private String adminName;

    @NotBlank(message = "admin surname can not be blank")
    @Length(message = "Admin surname must contain min 2 max  5O character.", min = 2, max = 50)
    private String adminSurname;

    @NotBlank(message = "Company name can not be blank")
    @Length(message = "Company name must contain min 2 max  50 character.", min = 2, max = 50)
    private String companyName;

    @Length(message = "Maximum 60", max = 60)
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email Format Error")
    private String email;
}
