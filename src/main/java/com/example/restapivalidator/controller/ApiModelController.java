package com.example.restapivalidator.controller;

import com.example.restapivalidator.dto.CreationResponseDto;
import com.example.restapivalidator.model.ApiModel;
import com.example.restapivalidator.service.ApiModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiModelController {
    @Autowired
    private ApiModelService service;
    @PostMapping("/define-model")
    public ResponseEntity<?> defineModel(@RequestBody ApiModel apiModel) {
        try {
            service.saveModel(apiModel);
            return ResponseEntity.created(URI.create("/define-model"))
                    .body(new CreationResponseDto("CREATED", LocalDateTime.now(), apiModel.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CreationResponseDto("ERROR", null, null));
        }
    }
    @GetMapping("/get-model/{id}")
    public ResponseEntity<?> getModel(@PathVariable String id) {
        Optional<ApiModel> model = service.findModelById(id);
        if (model.isPresent()) {
            return ResponseEntity.ok(model.get());
        } else {
            return ResponseEntity.status(404).body("Model not found");
        }
    }
}
