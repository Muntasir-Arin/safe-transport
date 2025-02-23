package com.safetransport.userservice.repository;

import com.safetransport.userservice.model.VerificationRequests;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VerificationRequestsRepository extends JpaRepository<VerificationRequests, UUID> {
}
