package com.example.crm.service;

import com.example.crm.entity.User;
import com.example.crm.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public void refreshAuthentication(User updatedUser) {
        CustomUserDetails updatedDetails = new CustomUserDetails(updatedUser);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedDetails,
                null,
                updatedDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
