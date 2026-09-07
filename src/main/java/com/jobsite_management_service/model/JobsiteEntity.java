package com.jobsite_management_service.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "jobsites")
public class JobsiteEntity {

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
}
