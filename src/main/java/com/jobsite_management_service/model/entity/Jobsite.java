package com.jobsite_management_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "jobsites")
public class Jobsite {

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
