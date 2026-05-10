package com.mvc.jobportal.services.action;

import com.mvc.jobportal.dto.IRecruiterJobs;
import com.mvc.jobportal.dto.RecruiterJobsDTO;
import com.mvc.jobportal.entity.job.Company;
import com.mvc.jobportal.entity.job.Location;
import com.mvc.jobportal.entity.job.Post;
import com.mvc.jobportal.repository.JobPostActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public List<RecruiterJobsDTO> getRecruiterJob(int recruiterId){
        List<IRecruiterJobs> recruiterJobs=jobPostActivityRepository.getRecruiterJobs(recruiterId);
        return recruiterJobs.stream()
                .map((j)->new RecruiterJobsDTO(j.getTotalCandidates(),
                        j.getJob_post_id(),j.getJob_title()
                ,new Location(j.getLocationId(),j.getCity(),j.getState(),j.getCountry())
                ,new Company(j.getCompanyId(),j.getName(),""))
                ).toList();
    }

    public List<Post> search(String job, String location,
                             List<String> type, List<String> remote, LocalDate searchDate){
        return Objects.isNull(searchDate)?
                jobPostActivityRepository.searchWithoutDate(job, location, type, remote):
                jobPostActivityRepository.search(job, location, type, remote, searchDate);
    }

    public Post addNew(Post post){
        return jobPostActivityRepository.save(post);
    }

    public List<Post> getAll(){
        return jobPostActivityRepository.findAll();
    }

    public Post getOne(int id){
        return jobPostActivityRepository.findById(id)
                .orElseThrow(()->new RuntimeException(" Not found "));
    }
}
