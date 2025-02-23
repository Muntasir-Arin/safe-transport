package com.safetransport.userservice.service;

import com.safetransport.userservice.dto.UserRequestDTO;
import com.safetransport.userservice.dto.UserResponseDTO;
import com.safetransport.userservice.exception.UserNotFoundException;
import com.safetransport.userservice.model.User;
import com.safetransport.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
        validateEmailUniqueness(request.getEmail());
        User user = buildUserFromRequest(request);
        return convertToDTO(userRepository.save(user));
    }

    public UserResponseDTO getUserById(UUID id) {
        User user = findUserById(id);
        return convertToDTO(user);
    }

    public UserResponseDTO updateUser(UUID id, UserRequestDTO updatedUser) {
        User user = findUserById(id);
        updateUserFromRequest(user, updatedUser, user.isVerified());
        return convertToDTO(userRepository.save(user));
    }

    public UserResponseDTO adminUpdateUser(UUID id, UserRequestDTO updatedUser) {
        User user = findUserById(id);
        updateUserFromRequest(user, updatedUser, updatedUser.isVerified());
        return convertToDTO(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        validateUserExistence(id);
        userRepository.deleteById(id);
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    private void validateUserExistence(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }

    private User buildUserFromRequest(UserRequestDTO request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .nidNumber(request.getNidNumber())
                .verified(false)
                .build();
    }

    private void updateUserFromRequest(User user, UserRequestDTO updatedUser, boolean verified) {
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setNidNumber(updatedUser.getNidNumber());
        user.setVerified(verified);
    }

    private UserResponseDTO convertToDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .nidNumber(user.getNidNumber())
                .verified(user.isVerified())
                .build();
    }
}
