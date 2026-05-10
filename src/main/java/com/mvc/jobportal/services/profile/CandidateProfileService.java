package com.mvc.jobportal.services.profile;

import com.mvc.jobportal.entity.profile.CandidateProfile;
import com.mvc.jobportal.entity.profile.Users;
import com.mvc.jobportal.repository.CandidateProfileRepository;
import com.mvc.jobportal.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CandidateProfileService {
    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;


    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository, UserRepository userRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.userRepository = userRepository;
    }

    public Optional<CandidateProfile> getOne(Integer id) {
        return candidateProfileRepository.findById(id);
    }

    public CandidateProfile addNew(CandidateProfile CandidateProfile) {
        return candidateProfileRepository.save(CandidateProfile);
    }

    public CandidateProfile getCurrentcandidateProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String mail = authentication.getName();
            Users users = userRepository.findByEmail(mail).orElseThrow(
                    () -> new UsernameNotFoundException("Mail id not found ")
            );
            Optional<CandidateProfile> CandidateProfile = getOne(users.getUserId());
            return CandidateProfile.get();
        }
        return null;
    }

}
