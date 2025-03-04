package com.safetransport.organizationservice.service;

import com.safetransport.organizationservice.dto.OrganizationRequestDTO;
import com.safetransport.organizationservice.dto.OrganizationResponseDTO;
import com.safetransport.organizationservice.exception.OrganizationNotFoundException;
import com.safetransport.organizationservice.model.Organization;
import com.safetransport.organizationservice.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public List<OrganizationResponseDTO> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrganizationResponseDTO createOrganization(OrganizationRequestDTO request) {
        validateEmailUniqueness(request.getEmail());
        Organization organization = buildOrganizationFromRequest(request);
        return convertToDTO(organizationRepository.save(organization));
    }

    public OrganizationResponseDTO getOrganizationById(UUID id) {
        Organization organization = findOrganizationById(id);
        return convertToDTO(organization);
    }

    public OrganizationResponseDTO updateOrganization(UUID id, OrganizationRequestDTO updatedOrg) {
        Organization organization = findOrganizationById(id);
        organization.setName(updatedOrg.getName());
        organization.setEmail(updatedOrg.getEmail());
        organization.setPhone(updatedOrg.getPhone());
        return convertToDTO(organizationRepository.save(organization));
    }

    public void deleteOrganization(UUID id) {
        validateOrganizationExistence(id);
        organizationRepository.deleteById(id);
    }

    private void validateEmailUniqueness(String email) {
        if (organizationRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }
    }

    private Organization findOrganizationById(UUID id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + id));
    }

    private void validateOrganizationExistence(UUID id) {
        if (!organizationRepository.existsById(id)) {
            throw new OrganizationNotFoundException("Organization not found with ID: " + id);
        }
    }

    private Organization buildOrganizationFromRequest(OrganizationRequestDTO request) {
        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setEmail(request.getEmail());
        organization.setPhone(request.getPhone());
        return organization;
    }

    private OrganizationResponseDTO convertToDTO(Organization organization) {
        return OrganizationResponseDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .email(organization.getEmail())
                .phone(organization.getPhone())
                .build();
    }
}