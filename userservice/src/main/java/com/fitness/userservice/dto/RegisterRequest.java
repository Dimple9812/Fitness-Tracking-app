//what all we want to accept from user will be defined here
package com.fitness.userservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message ="Email is required") //msg in case the validation fails
    @Email(message= "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6 , message = "Password must have atleast of 6 characters")
    private String password;

    private String keyCloakId;

    private  String firstName;
    private  String lastName;


}
