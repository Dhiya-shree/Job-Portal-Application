package com.mvc.jobportal.services.user;

import com.mvc.jobportal.entity.profile.CandidateProfile;
import com.mvc.jobportal.entity.profile.RecuiterProfile;
import com.mvc.jobportal.entity.profile.Users;
import com.mvc.jobportal.repository.CandidateProfileRepository;
import com.mvc.jobportal.repository.RecruiterProfileRepository;
import com.mvc.jobportal.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsersService {
    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UserRepository userRepository, CandidateProfileRepository candidateProfileRepository, RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Users addNew(Users users){
        users.setActive(true);
        users.setRegistrationDate(LocalDate.now());
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users saved=userRepository.save(users);
        int userType=users.getUserTypeId().getUserTypeId();
        if(userType==1){
            recruiterProfileRepository.save(new RecuiterProfile(saved));
        }
        else {
            candidateProfileRepository.save(new CandidateProfile(saved));
        }
        return saved;
    }

    public Object getCurrentUsers(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String userName=authentication.getName();
            Users users=userRepository.findByEmail(userName).orElseThrow(
                    ()->new UsernameNotFoundException("Could not find the mail id ")
            );

            int userId=users.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                RecuiterProfile recuiterProfile=recruiterProfileRepository.findById(userId)
                        .orElse(new RecuiterProfile());
                return recuiterProfile;
            }
            else{
                CandidateProfile candidateProfile=candidateProfileRepository.findById(userId)
                        .orElse(new CandidateProfile());
                return candidateProfile;
            }
        }
        return null;
    }

    public Users getCurrentUser(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String mail=authentication.getName();
            Users user=userRepository.findByEmail(mail).orElseThrow(
                    ()->new UsernameNotFoundException("Could not find the mail id ")
            );
            return user;

        }
        return null;
    }

    public Users findByEmail(String mail){
        return userRepository.findByEmail(mail).orElseThrow(
                ()->new UsernameNotFoundException("Could not find the mail id "));
    }

    public Optional<Users> findUserByEmail(String mail){
        return userRepository.findByEmail(mail);
    }

}
