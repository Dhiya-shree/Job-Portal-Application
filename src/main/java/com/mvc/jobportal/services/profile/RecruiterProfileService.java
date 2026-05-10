package com.mvc.jobportal.services.profile;

import com.mvc.jobportal.entity.profile.RecuiterProfile;
import com.mvc.jobportal.entity.profile.Users;
import com.mvc.jobportal.repository.RecruiterProfileRepository;
import com.mvc.jobportal.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;


    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UserRepository userRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    public Optional<RecuiterProfile> getOne(Integer id){
        return recruiterProfileRepository.findById(id);
    }

    public RecuiterProfile addNew(RecuiterProfile recuiterProfile){
        return recruiterProfileRepository.save(recuiterProfile);
    }

    public RecuiterProfile getCurrentRecruiterProfile(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String mail=authentication.getName();
            Users users=userRepository.findByEmail(mail).orElseThrow(
                    ()->new UsernameNotFoundException("Mail id not found ")
            );
            Optional<RecuiterProfile> recuiterProfile=getOne(users.getUserId());
            return recuiterProfile.get();
        }
        return null;
    }
}
