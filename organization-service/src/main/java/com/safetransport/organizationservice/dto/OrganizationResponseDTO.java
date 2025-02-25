package com.safetransport.organizationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class OrganizationResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
}