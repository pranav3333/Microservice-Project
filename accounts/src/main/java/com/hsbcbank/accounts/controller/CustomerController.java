package com.hsbcbank.accounts.controller;

import com.hsbcbank.accounts.dto.CustomerDetailsDto;
import com.hsbcbank.accounts.dto.ErrorResponseDto;
import com.hsbcbank.accounts.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@Tag(
        name = "REST APIS for Customers in HSBC Bank",
        description = "REST APIS in HSBC Bank to fetch customer details"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private final ICustomerService icustomerService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(ICustomerService icustomerService) {
        this.icustomerService = icustomerService;
    }


    @Operation(
            summary = "Fetch customer Details REST API",
            description = "REST API to fetch Customer details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestHeader("hsbcbank-correlation-id") String correlationId, @RequestParam
                                                            @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile Number should be of 10 digit")
                                                            String mobileNumber) {

        logger.debug("hsbcbank-correlation-id found: {} ", correlationId);
        CustomerDetailsDto customerDetailsDto = icustomerService.fetchCustomerDetails(mobileNumber,correlationId);

        return ResponseEntity.status(HttpStatus.OK).body(customerDetailsDto);

    }


}
