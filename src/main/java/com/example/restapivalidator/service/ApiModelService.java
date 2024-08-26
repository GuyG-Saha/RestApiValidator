package com.example.restapivalidator.service;

import com.example.restapivalidator.model.ApiModel;
import com.example.restapivalidator.repository.ApiSchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.example.restapivalidator.util.HashUtil;

@Service
public class ApiModelService {
    @Autowired
    ApiSchemaRepository apiModelRepository;
    public void saveModel(ApiModel apiModel) {
        String apiModelId = HashUtil.generateHash(apiModel.getMethod().toUpperCase() +
                "-" + apiModel.getPath()).substring(0, 10);
        apiModel.setId(apiModelId);
        apiModelRepository.save(apiModel);
    }
    public Optional<ApiModel> findModelById(String id) {
        return apiModelRepository.findById(id);
    }
}
