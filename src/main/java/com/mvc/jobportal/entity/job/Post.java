package com.mvc.jobportal.entity.job;

import com.mvc.jobportal.entity.profile.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
@Table(name = "job_post_activity")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @ManyToOne
    @JoinColumn(name="recuriterId",referencedColumnName = "userId")
    private Users recuriterId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="jobLocationId",referencedColumnName = "Id")
    private Location locationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="jobCompanyId",referencedColumnName = "id")
    private Company companyId;

    @Transient
    private  boolean isActive;

    @Transient
    private boolean isSaved;

    @Length(max=10000)
    public String description;

    private String jobType;
    private String salary;
    private String remote;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate postedDate;
    private String jobTitle;

}
