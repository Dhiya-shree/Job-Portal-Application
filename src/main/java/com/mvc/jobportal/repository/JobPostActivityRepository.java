package com.mvc.jobportal.repository;

import com.mvc.jobportal.dto.IRecruiterJobs;
import com.mvc.jobportal.entity.job.Post;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JobPostActivityRepository extends JpaRepository<Post,Integer> {

    @Query(value = """
Select count(s.user_id) as totalCandidates,
       j.job_post_id,
       j.job_title,
       l.id as locationId,l.city,l.state,l.country,
       c.id as companyId,c.name
       FROM 
           job_post_activity j 
       INNER JOIN 
       job_location l ON j.location_id=l.id
       INNER JOIN
        job_company c ON j.company_id=c.id
    LEFT JOIN 
        job_seeker_apply s ON s.post=j.post_id
     WHERE j.posted_by_id= :recruiter 
    GROUP BY j.job_post_id
         """,nativeQuery = true)
    List<IRecruiterJobs> getRecruiterJobs(@Param("recruiter") Integer recruiter);


    @Query(value = """
SELECT * job_post_activity j 
     INNER JOIN job_location l ON j.locationId=l.id
     WHERE j.job_title like %:job%
     AND ( l.city LIKE %:location% OR l.state LIKE %:location% OR l.country LIKE %:location% )
     AND j.job_type IN(:type)
     AND j.remate IN (:remote)
     """,nativeQuery = true)
    List<Post> searchWithoutDate(@Param("job") String job,
                                 @Param("location") String location,
                                 @Param("remote") List<String> remote,
                                 @Param("type") List<String> type);

    @Query(value = """
      SELECT * from job_post_activity j 
            INNER JOIN job_location l ON j.location_id=l.id
            WHERE 
           j.job_title LIKE %:job%
       AND (l.state like %:location% OR l.city like %:location% or l.country like %:location%)
         AND j.job_title IN (:type)
          AND j.remote IN (:remote)
                AND (posted_date >= :date)""",nativeQuery = true)
    List<Post> search(@Param("job") String job,
                      @Param("location") String location,
                      @Param("remote") List<String> remote,
                      @Param("type") List<String> type,
                      @Param("date") LocalDate date);

}
