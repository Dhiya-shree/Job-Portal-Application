package com.mvc.jobportal.entity.job;

import com.mvc.jobportal.entity.profile.CandidateProfile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table( name = "job_seeker_apply", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId","jobId"})
})
@Data
@NoArgsConstructor
public class Application implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="candidate",referencedColumnName = "user_account_id")
    private CandidateProfile userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="post",referencedColumnName = "postId")
    private Post post;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate applyDate;

    private String coverLetter;
}
