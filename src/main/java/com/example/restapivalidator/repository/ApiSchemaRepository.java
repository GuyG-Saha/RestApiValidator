package com.example.restapivalidator.repository;

import com.example.restapivalidator.model.ApiModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiSchemaRepository extends MongoRepository<ApiModel, String> {
}
