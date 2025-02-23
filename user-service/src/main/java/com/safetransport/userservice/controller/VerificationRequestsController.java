package com.safetransport.userservice.controller;

import com.safetransport.userservice.dto.VerificationRequestDTO;
import com.safetransport.userservice.dto.VerificationResponseDTO;
import com.safetransport.userservice.service.VerificationRequestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/verify")
@RequiredArgsConstructor
public class VerificationRequestsController {

    private final VerificationRequestsService verificationRequestsService;

    @PostMapping("/submit")
    public ResponseEntity<VerificationResponseDTO> submitVerificationRequest(
            @RequestBody VerificationRequestDTO requestDTO) {

        VerificationResponseDTO response = verificationRequestsService.submitVerificationRequest(requestDTO);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/review/{requestId}")
    public ResponseEntity<VerificationResponseDTO> reviewVerificationRequest(
            @PathVariable UUID requestId,
            @RequestParam UUID adminId,
            @RequestParam String status) {

        VerificationResponseDTO response = verificationRequestsService.reviewVerificationRequest(requestId, adminId, status);
        return ResponseEntity.ok(response);
    }
}
