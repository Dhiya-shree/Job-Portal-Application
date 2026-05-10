package com.mvc.jobportal.controller;

import com.mvc.jobportal.entity.candidate.Skill;
import com.mvc.jobportal.entity.profile.CandidateProfile;
import com.mvc.jobportal.entity.profile.Users;
import com.mvc.jobportal.repository.UserRepository;
import com.mvc.jobportal.services.profile.CandidateProfileService;
import com.mvc.jobportal.util.FileGetResource;
import com.mvc.jobportal.util.FileUPLoad;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class CandidateProfileController {
    private final UserRepository userRepository;
    private final CandidateProfileService candidateProfileService;


    public CandidateProfileController(UserRepository userRepository, CandidateProfileService candidateProfileService) {
        this.userRepository = userRepository;
        this.candidateProfileService = candidateProfileService;
    }

    CandidateProfile profile=new CandidateProfile();


    @GetMapping("/")
    public String candidateProf(Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        List<Skill> skills=new ArrayList<>();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user=userRepository.findByEmail(authentication.getName()).orElseThrow(
                    ()->new RuntimeException("User not found")
            );
            candidateProfileService.getOne(user.getUserId()).ifPresent(candidateProfile -> {
                profile=candidateProfile;
                if(!candidateProfile.getSkills().isEmpty()) {
                    skills.addAll(candidateProfile.getSkills());
                }
            });

        }
        model.addAttribute("skills",skills);
        model.addAttribute("profile",profile);

        return "job-seeker-profile";
    }

    @GetMapping("/{id}")
    public String candidateProfileById(@PathVariable int id, Model model) {
        Optional<CandidateProfile> candidateProfile = candidateProfileService.getOne(id);
        if (candidateProfile.isPresent()) {
            model.addAttribute("profile", candidateProfile.get());
            return "job-seeker-profile";
        } else {
            throw new UsernameNotFoundException("No user with id " + id);
        }
    }

  @PostMapping("/addNew")
    public String addNewCandidate(
          CandidateProfile candidateProfile,
          @RequestParam MultipartFile image,
          @RequestPart MultipartFile pdf,
          Model model
          ){

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user=userRepository.findByEmail(authentication.getName()).orElseThrow(
                    ()->new UsernameNotFoundException("User not found")
            );
            candidateProfile.setUserId(user);
            candidateProfile.setUserAccountId(user.getUserId());
        }

        List<Skill> skills=new ArrayList<>();
        model.addAttribute("profile",candidateProfile);
        model.addAttribute("skills",skills);

        for(Skill skill  : candidateProfile.getSkills()){
            skill.setCandidateProfile(candidateProfile);
        }

        String imageName = "";
        String resumeName="";

        if(image!=null && image.getOriginalFilename()!=null && !image.getOriginalFilename().isEmpty()){
            imageName= StringUtils.cleanPath(image.getOriginalFilename());
            candidateProfile.setProfilePhoto(imageName);
        }

        if(pdf!=null && pdf.getOriginalFilename()!=null && !pdf.getOriginalFilename().isEmpty()){
            resumeName=StringUtils.cleanPath(pdf.getOriginalFilename());
            candidateProfile.setResume(resumeName);
        }

        CandidateProfile savedCandidateProfile =candidateProfileService.addNew(candidateProfile);

        try{
            String uploadDir="photos/candidate/"+savedCandidateProfile.getUserAccountId();
            if(!imageName.isEmpty()){
                FileUPLoad.saveFile(uploadDir,imageName,image);
            }

            if(!resumeName.isEmpty()){
                FileUPLoad.saveFile(uploadDir,resumeName,pdf);
            }
        }
        catch (IOException e){
            throw new RuntimeException("Incorrect file");
        }

        return "redirect:/dashboard/";
  }


   @GetMapping("/downloadResume")
    public ResponseEntity downloadResume(
            @RequestParam String fileName,
            @RequestParam String userId
   )
   {
       Resource resource=null;
       try {
           resource=FileGetResource.getFileAsResource("photos/candidate/"+userId,fileName);

       } catch (IOException e) {
           return ResponseEntity.badRequest().build();
       }

       if(resource==null){
           return new ResponseEntity<>("File not found ", HttpStatus.NOT_FOUND);
       }

       String contentType="application/octet-stream";
       String header="attachment; filename=\""+resource.getFilename()+"\"";

       return ResponseEntity.ok()
               .contentType(MediaType.parseMediaType(contentType))
               .header("Content-Disposition",header)
               .body(resource);
   }


}
