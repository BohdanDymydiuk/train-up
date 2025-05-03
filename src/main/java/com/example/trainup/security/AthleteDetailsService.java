package com.example.trainup.security;

import com.example.trainup.repository.AthleteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("athleteDetailsService")
@RequiredArgsConstructor
public class AthleteDetailsService implements UserDetailsService {
    private final AthleteRepository athleteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return athleteRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can not find athlete by email: " + email));
    }
}
