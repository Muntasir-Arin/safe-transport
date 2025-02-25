package com.safetransport.organizationservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationRequestDTO {
    private String name;
    private String email;
    private String phone;
}