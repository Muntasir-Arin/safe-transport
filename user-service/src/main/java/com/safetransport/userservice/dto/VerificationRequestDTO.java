package com.safetransport.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequestDTO {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "NID Photo URL is required")
    private String nidPhotoUrl;
}