
package com.careerforge.backend.service;

import com.careerforge.backend.dto.MLPredictionRequest;
import com.careerforge.backend.dto.MLPredictionResponse;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class MLPredictionService {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String mlApiUrl;

    public MLPredictionService(
            ObjectMapper objectMapper,
            @Value("${careerforge.ml.api-url:http://127.0.0.1:8000}") String mlApiUrl) {

        this.objectMapper = objectMapper;
        this.mlApiUrl = mlApiUrl;

        this.httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(5))
        .build();
    }

    public MLPredictionResponse predict(MLPredictionRequest request)
            throws IOException, InterruptedException {

        String requestJson = objectMapper.writeValueAsString(request);
        System.out.println("ML request JSON: " + requestJson);

        HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create(mlApiUrl + "/predict"))
        .timeout(Duration.ofSeconds(15))
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .POST(HttpRequest.BodyPublishers.ofByteArray(
        requestJson.getBytes(java.nio.charset.StandardCharsets.UTF_8)))
        .build();

        HttpResponse<String> response = httpClient.send(
                httpRequest,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
    throw new IllegalStateException(
            "ML API returned HTTP " + response.statusCode()
                    + ": " + response.body());
}

        return objectMapper.readValue(
                response.body(),
                MLPredictionResponse.class);
    }
}
