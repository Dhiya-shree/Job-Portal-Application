package com.mvc.jobportal.entity.candidate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvc.jobportal.entity.profile.CandidateProfile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="skills")
@Data
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String experience;
    private String yearsOfExperience;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="candidate_profile")
    private CandidateProfile candidateProfile;


}
