package com.mvc.jobportal.entity.profile;

import com.mvc.jobportal.entity.candidate.Skill;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name="job_seeker_profile")
@NoArgsConstructor
public class CandidateProfile {

    @Id
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name="user_account_id")
    @MapsId
    private Users userId;

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String workAuthorization;
    private String employmentType;
    private String resume;

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    @OneToMany(targetEntity = Skill.class, cascade = CascadeType.ALL, mappedBy = "candidateProfile")
    private List<Skill> skills;

    public CandidateProfile(Users saved) {
        this.userId=saved;
    }
}
