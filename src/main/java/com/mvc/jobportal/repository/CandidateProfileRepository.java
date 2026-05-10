package com.mvc.jobportal.repository;

import com.mvc.jobportal.entity.profile.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile,Integer> {
}
