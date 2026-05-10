package com.mvc.jobportal.repository;

import com.mvc.jobportal.entity.job.Application;
import com.mvc.jobportal.entity.job.Post;
import com.mvc.jobportal.entity.profile.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerApplyRepository extends JpaRepository<Application,Integer> {

    List<Application> findByUserId(CandidateProfile userId);
    List<Application> findByPost(Post post);
}
