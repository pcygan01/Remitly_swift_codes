package com.remitly.swiftcodes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.remitly.swiftcodes.model.SwiftCodeMongo;

@Configuration
@EnableMongoRepositories(basePackages = "com.remitly.swiftcodes.repository")
public class MongoConfig {

    @Bean
    public IndexResolver indexResolver(MongoTemplate mongoTemplate, MongoMappingContext mongoMappingContext) {
        // Create indexes for the SwiftCodeMongo class
        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
        IndexOperations indexOps = mongoTemplate.indexOps(SwiftCodeMongo.class);
        resolver.resolveIndexFor(SwiftCodeMongo.class).forEach(indexOps::ensureIndex);
        return resolver;
    }
} 