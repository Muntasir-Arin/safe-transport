package com.safetransport.userservice.dto;

import lombok.Builder;
import lombok.With;

import java.util.UUID;

@Builder
@With
public record VerificationResponseDTO(
        UUID requestId,
        UUID userId,
        String nidPhotoUrl,
        String reviewStatus,
        UUID reviewedBy
) {
}