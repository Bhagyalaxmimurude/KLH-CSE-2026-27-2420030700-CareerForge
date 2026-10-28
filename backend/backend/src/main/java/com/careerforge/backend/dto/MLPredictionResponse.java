
package com.careerforge.backend.dto;

public record MLPredictionResponse(
        int predicted_label,
        String label_meaning,
        String note
) {
}
