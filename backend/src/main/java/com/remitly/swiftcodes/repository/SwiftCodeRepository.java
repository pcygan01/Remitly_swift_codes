package com.remitly.swiftcodes.repository;

import com.remitly.swiftcodes.model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, String> {
    
    List<SwiftCode> findByCountryISO2(String countryISO2);
    
    @Query("SELECT s FROM SwiftCode s WHERE s.swiftCode LIKE CONCAT(SUBSTRING(?1, 1, 8), '%')")
    List<SwiftCode> findBranchesByHeadquarterSwiftCode(String headquarterSwiftCode);
} 