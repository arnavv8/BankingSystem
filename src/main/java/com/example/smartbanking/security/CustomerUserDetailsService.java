package com.example.smartbanking.security;

import com.example.smartbanking.entity.User;
import com.example.smartbanking.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // If your User has fields like enabled/locked/expired, you can set them here via the builder methods.
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash()) // ensure this is the hashed password field
                .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new)) // adds ROLE_ prefix automatically
                // .accountExpired(false)
                // .accountLocked(false)
                // .credentialsExpired(false)
                // .disabled(!user.isEnabled())
                .build();
    }
}