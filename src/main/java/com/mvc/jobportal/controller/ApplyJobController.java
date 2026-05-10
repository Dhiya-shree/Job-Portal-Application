package com.mvc.jobportal.controller;

import com.mvc.jobportal.entity.candidate.SavedJobs;
import com.mvc.jobportal.entity.job.Application;
import com.mvc.jobportal.entity.job.Post;
import com.mvc.jobportal.entity.profile.CandidateProfile;
import com.mvc.jobportal.entity.profile.RecuiterProfile;
import com.mvc.jobportal.entity.profile.Users;
import com.mvc.jobportal.services.action.JobPostActivityService;
import com.mvc.jobportal.services.action.JobSeekerApplyService;
import com.mvc.jobportal.services.action.JobSeekerSaveService;
import com.mvc.jobportal.services.profile.CandidateProfileService;
import com.mvc.jobportal.services.profile.RecruiterProfileService;
import com.mvc.jobportal.services.user.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ApplyJobController {
    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final RecruiterProfileService recruiterProfileService;
    private final CandidateProfileService candidateProfileService;


    public ApplyJobController(JobPostActivityService jobPostActivityService, UsersService usersService, JobSeekerSaveService jobSeekerSaveService, JobSeekerApplyService jobSeekerApplyService, RecruiterProfileService recruiterProfileService, CandidateProfileService candidateProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.recruiterProfileService = recruiterProfileService;
        this.candidateProfileService = candidateProfileService;
    }

    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable int id, Model model){
        Post jobDetails = jobPostActivityService.getOne(id);
        List<Application> jobSeekerApplyList = jobSeekerApplyService.getCandidates(jobDetails);
        List<SavedJobs> jobSeekerSaveList = jobSeekerSaveService.getJobCandidates(jobDetails);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                RecuiterProfile user = recruiterProfileService.getCurrentRecruiterProfile();
                if (user != null) {
                    model.addAttribute("applyList", jobSeekerApplyList);
                }
            } else {
                CandidateProfile user = candidateProfileService.getCurrentcandidateProfile();
                if (user != null) {
                    boolean exists = false;
                    boolean saved = false;
                    for (Application jobSeekerApply : jobSeekerApplyList) {
                        if (jobSeekerApply.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            exists = true;
                            break;
                        }
                    }
                    for (SavedJobs jobSeekerSave : jobSeekerSaveList) {
                        if (jobSeekerSave.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            saved = true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied", exists);
                    model.addAttribute("alreadySaved", saved);
                }
            }
        }

        Application jobSeekerApply = new Application();
        model.addAttribute("applyJob", jobSeekerApply);

        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", usersService.getCurrentUser());
        return "job-details";
    }

    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id, Application jobSeekerApply) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersService.findByEmail(currentUsername);
            Optional<CandidateProfile> seekerProfile = candidateProfileService.getOne(user.getUserId());
            Post jobPostActivity = jobPostActivityService.getOne(id);
            if (seekerProfile.isPresent() && jobPostActivity != null) {
                jobSeekerApply = new Application();
                jobSeekerApply.setUserId(seekerProfile.get());
                jobSeekerApply.setPost(jobPostActivity);
                jobSeekerApply.setApplyDate(LocalDate.now());
            } else {
                throw new RuntimeException("User not found");
            }
            jobSeekerApplyService.addNew(jobSeekerApply);
        }

        return "redirect:/dashboard/";
    }
}
