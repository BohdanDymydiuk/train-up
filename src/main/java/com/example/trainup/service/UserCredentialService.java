package com.example.trainup.service;

import com.example.trainup.model.user.UserCredentials;

public interface UserCredentialService {
    void assignRoleBasedOnUserType(UserCredentials userCredentials);
}
