package com.mvc.jobportal.entity.job;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "job_location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String city;
    private String state;
    private String country;

    public Location(int locationId, String city, String state, String country) {
        this.id=locationId;
        this.city=city;
        this.country=country;
        this.state=state;
    }
}
