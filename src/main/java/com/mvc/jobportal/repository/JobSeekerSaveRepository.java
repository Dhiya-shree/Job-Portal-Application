package com.mvc.jobportal.repository;

import com.mvc.jobportal.entity.candidate.SavedJobs;
import com.mvc.jobportal.entity.job.Post;
import com.mvc.jobportal.entity.profile.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerSaveRepository extends JpaRepository<SavedJobs,Integer> {
    List<SavedJobs> findByUserId(CandidateProfile profile);
    List<SavedJobs> findByJob(Post post);
}
