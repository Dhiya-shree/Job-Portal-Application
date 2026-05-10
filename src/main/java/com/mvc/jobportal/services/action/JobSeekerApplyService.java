package com.mvc.jobportal.services.action;

import com.mvc.jobportal.entity.job.Application;
import com.mvc.jobportal.entity.job.Post;
import com.mvc.jobportal.entity.profile.CandidateProfile;
import com.mvc.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {
    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<Application> getCandidateForJobs(CandidateProfile profile){
        return  jobSeekerApplyRepository.findByUserId(profile);
    }

    public List<Application> getCandidates(Post post){
        return jobSeekerApplyRepository.findByPost(post);
    }

    public void addNew(Application apply){
        jobSeekerApplyRepository.save(apply);
    }
}
