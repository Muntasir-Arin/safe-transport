package com.safetransport.userservice.dto;


import lombok.Builder;
import lombok.With;

import java.util.UUID;

@Builder
@With
public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String phone,
        String nidNumber,
        boolean verified
) {
}
