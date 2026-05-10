package com.mvc.jobportal.controller;

import com.mvc.jobportal.entity.candidate.SavedJobs;
import com.mvc.jobportal.entity.job.Post;
import com.mvc.jobportal.entity.profile.CandidateProfile;
import com.mvc.jobportal.entity.profile.Users;
import com.mvc.jobportal.services.action.JobPostActivityService;
import com.mvc.jobportal.services.action.JobSeekerSaveService;
import com.mvc.jobportal.services.profile.CandidateProfileService;
import com.mvc.jobportal.services.user.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SaveJobController {

    private final UsersService usersService;
    private final CandidateProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public SaveJobController(UsersService usersService, CandidateProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id, SavedJobs jobSeekerSave) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersService.findByEmail(currentUsername);
            Optional<CandidateProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            Post jobPostActivity = jobPostActivityService.getOne(id);
            if (seekerProfile.isPresent() && jobPostActivity != null) {
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(seekerProfile.get());
            } else {
                throw new RuntimeException("User not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model) {

        List<Post> jobPost = new ArrayList<>();
        Object currentUserProfile = usersService.getCurrentUser();

        List<SavedJobs> jobSeekerSaveList = jobSeekerSaveService.getCandidatesSavedJobs((CandidateProfile) currentUserProfile);
        for (SavedJobs jobSeekerSave : jobSeekerSaveList) {
            jobPost.add(jobSeekerSave.getJob());
        }

        model.addAttribute("jobPost", jobPost);
        model.addAttribute("user", currentUserProfile);

        return "saved-jobs";
    }
}