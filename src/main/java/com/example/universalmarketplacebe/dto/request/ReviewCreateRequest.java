package com.example.universalmarketplacebe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to create a new review")
public record ReviewCreateRequest(
        @Schema(description = "ID of the user being reviewed", example = "2")
        @NotNull Long targetId,
        
        @Schema(description = "Rating given to the user (1-5)", example = "5")
        @NotNull @Min(1) @Max(5) Integer rating,
        
        @Schema(description = "Review comment or feedback", example = "Great seller, very fast shipping!")
        @NotBlank String comment
) {
}
