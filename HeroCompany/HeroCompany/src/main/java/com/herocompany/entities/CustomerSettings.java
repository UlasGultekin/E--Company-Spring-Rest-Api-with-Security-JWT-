package com.herocompany.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CustomerSettings {
    @NotBlank(message = "Firstname can't be blank")
    @Length(message = "Firstname must be between 3 and 50 characters", min = 3, max = 50)
    private String firstName;
    @NotBlank(message = "Lastname can't be blank")
    @Length(message = "Lastname must be between 3 and 50 characters", min = 3, max = 50)
    private String lastName;
    @NotBlank(message = "Phone can't be blank")
//    @Length(message = "Phone must be between 3 and 50 characters", min = 3, max = 10)
    // @Pattern(message = "Please enter a valid phone number",regexp = "/(\\+\\d{1,3}\\s?)?((\\(\\d{3}\\)\\s?)|(\\d{3})(\\s|-?))(\\d{3}(\\s|-?))(\\d{4})(\\s?(([E|e]xt[:|.|]?)|x|X)(\\s?\\d+))?/g")
    private String phone;

    @NotNull(message = "Email not null")
    @Email(message = "Email should be valid")
    private String email;

}
