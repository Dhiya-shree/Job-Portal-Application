package com.mvc.jobportal.entity.candidate;

import com.mvc.jobportal.entity.job.Post;
import com.mvc.jobportal.entity.profile.CandidateProfile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table( name="job_seeker_save",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "job"})})
@Data
@NoArgsConstructor
public class SavedJobs implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "user_account_id")
    private CandidateProfile userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job", referencedColumnName = "postId")
    private Post job;

}
