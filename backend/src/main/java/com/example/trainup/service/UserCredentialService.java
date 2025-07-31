package com.example.trainup.service;

import com.example.trainup.model.Role;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.RoleRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCredentialService {
    private final RoleRepository roleRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    public void assignRoleBasedOnUserType(UserCredentials userCredentials) {
        UserCredentials.UserType userType = userCredentials.getUserType();
        Role role = roleRepository.findByName("ROLE_" + userType.name())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_" + userType.name());
                    return roleRepository.save(newRole);
                });
        userCredentials.getRoles().add(role);
        userCredentialsRepository.save(userCredentials);
    }
}
