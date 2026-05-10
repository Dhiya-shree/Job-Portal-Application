package com.mvc.jobportal.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class AuthenticationSuccess implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Authentication authentication) throws IOException {
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        String mail=userDetails.getUsername();
        System.out.println(" User logged in "+mail);
        boolean redirect=authentication.getAuthorities().stream().anyMatch((a)->(Objects.equals(a.getAuthority(), "Job Seeker") ||a.getAuthority().equals("Recruiter")));
        if (redirect){
            response.sendRedirect("/dashboard");
        }
    }
}
