package com.example.trainup.security;

import com.example.trainup.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userCredentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer with email: "
                        + email + " doesn't exist"));
    }
}
