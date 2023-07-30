package com.boardify.boardify.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{
    private Long id;
    @NotEmpty(message = "Username should not be empty")
    private String username;
    @NotEmpty(message = "First name should not be empty")
    private String firstName;
    @NotEmpty(message = "Last name should not be empty")
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    private String accountStatus;

    @NotEmpty(message = "Street address should not be empty")
    private String address;
    @NotEmpty(message = "City should not be empty")
    private String city;
    @NotEmpty(message = "Country should not be empty")
    private String country;
    @NotEmpty(message = "State/Province should not be empty")
    private String state;
    @NotEmpty(message = "Zip/Postal Code should not be empty")
    private String zipCode;
    @NotEmpty(message = "Phone number should not be empty")
    private String phone;
    private String stripeToken;
    private int won;
    private int joined;
}
