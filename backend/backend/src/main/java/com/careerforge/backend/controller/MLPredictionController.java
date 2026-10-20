
package com.careerforge.backend.controller;

import com.careerforge.backend.dto.MLPredictionRequest;
import com.careerforge.backend.dto.MLPredictionResponse;
import com.careerforge.backend.service.MLPredictionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ml")
public class MLPredictionController {

    private final MLPredictionService mlPredictionService;

    public MLPredictionController(
            MLPredictionService mlPredictionService) {
        this.mlPredictionService = mlPredictionService;
    }

    @PostMapping("/predict")
    public ResponseEntity<?> predict(
            @Valid @RequestBody MLPredictionRequest request) {

        try {
            MLPredictionResponse response =
                    mlPredictionService.predict(request);

            return ResponseEntity.ok(response);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "error", "ML prediction was interrupted"));

        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "error", "Unable to obtain a prediction from the ML service"));
        }
    }
}
