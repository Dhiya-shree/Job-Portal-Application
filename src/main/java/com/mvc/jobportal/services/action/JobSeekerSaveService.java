package com.mvc.jobportal.services.action;

import com.mvc.jobportal.entity.candidate.SavedJobs;
import com.mvc.jobportal.entity.job.Post;
import com.mvc.jobportal.entity.profile.CandidateProfile;
import com.mvc.jobportal.repository.JobSeekerSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {
    private final JobSeekerSaveRepository jobSeekerSaveRepository;


    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<SavedJobs> getCandidatesSavedJobs(CandidateProfile profile){
        return jobSeekerSaveRepository.findByUserId(profile);
    }

    public List<SavedJobs> getJobCandidates(Post post){
        return jobSeekerSaveRepository.findByJob(post);
    }

    public void addNew(SavedJobs savedJobs){
        jobSeekerSaveRepository.save(savedJobs);
    }
}
