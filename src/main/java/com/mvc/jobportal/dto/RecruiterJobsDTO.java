package com.mvc.jobportal.dto;

import com.mvc.jobportal.entity.job.Company;
import com.mvc.jobportal.entity.job.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterJobsDTO {
    private Long totalCandidates;
    private Integer jobPostId;
    private String jobTitle;
    private Location locationId;
    private Company companyId;

}
