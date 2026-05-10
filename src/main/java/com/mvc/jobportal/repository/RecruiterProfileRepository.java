package com.mvc.jobportal.repository;

import com.mvc.jobportal.entity.profile.RecuiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterProfileRepository extends JpaRepository<RecuiterProfile,Integer> {
}
