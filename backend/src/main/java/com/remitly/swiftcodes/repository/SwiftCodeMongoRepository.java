package com.remitly.swiftcodes.repository;

import com.remitly.swiftcodes.model.SwiftCodeMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwiftCodeMongoRepository extends MongoRepository<SwiftCodeMongo, String> {
    
    Optional<SwiftCodeMongo> findBySwiftCode(String swiftCode);
    
    List<SwiftCodeMongo> findByCountryISO2(String countryISO2);
    
    @Query("{ 'swiftCode': { $regex: ?0, $options: 'i' } }")
    List<SwiftCodeMongo> findBranchesByHeadquarterSwiftCodePrefix(String headquarterSwiftCodePrefix);
    
    boolean existsBySwiftCode(String swiftCode);
    
    void deleteBySwiftCode(String swiftCode);
} 