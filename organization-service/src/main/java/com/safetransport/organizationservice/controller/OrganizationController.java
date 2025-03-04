package com.safetransport.organizationservice.controller;

import com.safetransport.organizationservice.dto.OrganizationRequestDTO;
import com.safetransport.organizationservice.dto.OrganizationResponseDTO;
import com.safetransport.organizationservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<List<OrganizationResponseDTO>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @PostMapping
    public ResponseEntity<OrganizationResponseDTO> createOrganization(@RequestBody OrganizationRequestDTO request) {
        return ResponseEntity.ok(organizationService.createOrganization(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> getOrganizationById(@PathVariable UUID id) {
        return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> updateOrganization(@PathVariable UUID id, @RequestBody OrganizationRequestDTO updatedOrg) {
        return ResponseEntity.ok(organizationService.updateOrganization(id, updatedOrg));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable UUID id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}