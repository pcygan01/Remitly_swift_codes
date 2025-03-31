package com.remitly.swiftcodes.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSwiftCodeRequest {
    
    @NotBlank(message = "SWIFT code is required")
    @Pattern(regexp = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$", 
            message = "SWIFT code must be in valid format")
    private String swiftCode;
    
    @NotBlank(message = "Bank name is required")
    private String bankName;
    
    @NotBlank(message = "Country ISO2 code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country ISO2 must be a valid 2-letter code")
    private String countryISO2;
    
    @NotBlank(message = "Country name is required")
    private String countryName;
    
    private String address;
    
    @NotNull(message = "Is headquarter field is required")
    private Boolean isHeadquarter;
} 