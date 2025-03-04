package com.safetransport.organizationservice.repository;

import com.safetransport.organizationservice.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByEmail(String email);
}
