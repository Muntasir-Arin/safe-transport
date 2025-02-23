package com.safetransport.userservice.service;

import com.safetransport.userservice.dto.VerificationRequestDTO;
import com.safetransport.userservice.dto.VerificationResponseDTO;
import com.safetransport.userservice.exception.UserNotFoundException;
import com.safetransport.userservice.model.ReviewStatus;
import com.safetransport.userservice.model.User;
import com.safetransport.userservice.model.VerificationRequests;
import com.safetransport.userservice.repository.UserRepository;
import com.safetransport.userservice.repository.VerificationRequestsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationRequestsService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_APPROVED = "APPROVED";
    private static final String STATUS_REJECTED = "REJECTED";

    private final VerificationRequestsRepository verificationRequestRepository;
    private final UserRepository userRepository;

    public VerificationResponseDTO submitVerificationRequest(VerificationRequestDTO requestDTO) {
        User user = getUserById(requestDTO.getUserId());

        VerificationRequests verificationRequest = createVerificationRequest(user, requestDTO.getNidPhotoUrl());
        VerificationRequests savedRequest = verificationRequestRepository.save(verificationRequest);

        return mapToResponseDTO(savedRequest);
    }

    public VerificationResponseDTO reviewVerificationRequest(UUID requestId, UUID adminId, String status) {
        validateStatus(status);

        VerificationRequests request = getVerificationRequestById(requestId);
        User admin = getUserById(adminId);

        request.setReviewStatus(ReviewStatus.valueOf(status));
        request.setReviewedBy(admin);

        VerificationRequests updatedRequest = verificationRequestRepository.save(request);
        return mapToResponseDTO(updatedRequest);
    }

    private User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    private VerificationRequests getVerificationRequestById(UUID requestId) {
        return verificationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Verification request not found with ID: " + requestId));
    }

    private VerificationRequests createVerificationRequest(User user, String nidPhotoUrl) {
        VerificationRequests verificationRequest = new VerificationRequests();
        verificationRequest.setUser(user);
        verificationRequest.setNidPhotoUrl(nidPhotoUrl);
        verificationRequest.setReviewStatus(ReviewStatus.valueOf(STATUS_PENDING));
        return verificationRequest;
    }

    private void validateStatus(String status) {
        if (!STATUS_APPROVED.equals(status) && !STATUS_REJECTED.equals(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private VerificationResponseDTO mapToResponseDTO(VerificationRequests request) {
        return VerificationResponseDTO.builder()
                .userId(request.getUser().getId())
                .requestId(request.getId())
                .nidPhotoUrl(request.getNidPhotoUrl())
                .reviewStatus(String.valueOf(request.getReviewStatus()))
                .reviewedBy(request.getReviewedBy() != null ? request.getReviewedBy().getId() : null)
                .build();
    }
}
