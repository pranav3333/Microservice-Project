package com.hsbcbank.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "CustomerDetails",
        description = "Schema to hold customer,account,cards and loans information"
)
public class CustomerDetailsDto {

    @NotEmpty(message = "Name is mandatory")
    @Size(min = 3, max = 50, message = "Name should be between 3 and 50 characters")
    @Schema(
            description = "Name of customer",example = "Pranav Kurankar"
    )
    private String name;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Schema(
            description = "Email of customer",example = "pranavkurankar@gamil.com"
    )
    private String email;

    @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number should be 10 digits")
    @Schema(
            description = "Mobile number of customer",example = "9909787888"
    )
    private String mobileNumber;

    @Schema(
            description = "Account details of the Customer"
    )
    private AccountsDto accounts;

    @Schema(
            description = "Loans details of the Customer"
    )
    private LoansDto loans;

    @Schema(
            description = "Cards details of the Customer"
    )
    private CardsDto cards;

}
