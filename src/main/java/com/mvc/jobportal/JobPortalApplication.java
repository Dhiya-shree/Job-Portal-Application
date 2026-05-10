package com.mvc.jobportal;

import com.mvc.jobportal.entity.profile.UsersType;
import com.mvc.jobportal.repository.UsersTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class JobPortalApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(JobPortalApplication.class, args);
    }


    @Autowired
    UsersTypeRepository usersTypeRepository;
    @Override
    public void run(String... args) throws Exception {
        UsersType u1=new UsersType(1,"Recruiter");
        UsersType u2=new UsersType(2,"Job Seeker");


        usersTypeRepository.save(u1);
        usersTypeRepository.save(u2);

    }
}
