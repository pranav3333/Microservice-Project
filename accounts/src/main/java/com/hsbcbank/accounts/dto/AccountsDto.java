package com.hsbcbank.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Accounts",
        description = "Schema to hold accounts information"
)
public class AccountsDto {

    @NotEmpty(message = "Account number is mandatory")
    @Pattern(regexp = "^$|[0-9]{10}", message = "Account number should be 10 digits")
    @Schema(
            description = "Account number of HSBC bank",example = "1234567890"
    )
    private Long accountNumber;

    @NotEmpty(message = "Account type is mandatory")
    @Schema(
            description = "Account type of HSBC bank",example = "Saving"
    )
    private String accountType;

    @NotEmpty(message = "Branch address is mandatory")
    @Schema(
            description = "Branch address of HSBC bank",example = "124 Main Street, New York"
    )
    private String branchAddress;
}
