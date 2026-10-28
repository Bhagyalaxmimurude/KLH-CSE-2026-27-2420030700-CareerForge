
package com.careerforge.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MLPredictionRequest(
        @NotNull @Min(0) @Max(10) Integer coding,
        @NotNull @Min(0) @Max(10) Integer dsa,
        @NotNull @Min(0) @Max(10) Integer sql,
        @NotNull @Min(0) @Max(10) Integer projects,
        @NotNull @Min(0) @Max(10) Integer communication
) {
}
