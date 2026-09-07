package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.User;
import com.jobsite_management_service.abstraction.enums.JobsiteType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "jobsites")
public class Jobsite implements com.jobsite_management_service.abstraction.Jobsite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Column
    Long userId;

    @Column
    String address;

    @Column
    String zipcode;

    @Column
    String city;

    @Column
    String state;

    @Column
    String country;

    @Column
    OffsetDateTime expectedCompletion;

    @Column
    OffsetDateTime createdAt;

    @Override
    public User getRequestor() {
        return null;
    }

    @Override
    public JobsiteType getJobsiteType() {
        return null;
    }
}
