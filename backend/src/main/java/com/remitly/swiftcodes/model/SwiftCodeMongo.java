package com.remitly.swiftcodes.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "swift_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwiftCodeMongo {

    @Id
    private String id;
    
    @NotBlank(message = "SWIFT code is required")
    @Indexed(unique = true)
    private String swiftCode;
    
    @NotBlank(message = "Bank name is required")
    private String bankName;
    
    @NotBlank(message = "Country ISO2 code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country ISO2 must be a valid 2-letter code")
    @Indexed
    private String countryISO2;
    
    @NotBlank(message = "Country name is required")
    private String countryName;
    
    private String address;
    
    @NotNull(message = "Is headquarter field is required")
    @Indexed
    private Boolean isHeadquarter;
    
    // This field is not persisted but used in responses for headquarter swift codes
    @Transient
    private List<SwiftCodeBranchDTO> branches;
} 