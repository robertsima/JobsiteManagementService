package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.enums.JobsiteType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"business", "requestor"})
@Table(name = "jobsites")
public class Jobsite implements com.jobsite_management_service.abstraction.Jobsite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    Business business;

    //covariant return satisfies abstraction.Jobsite#getRequestor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User requestor;

    @Column(name = "name", nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "jobsite_type", nullable = false)
    JobsiteType jobsiteType;

    @Column(name = "address", nullable = false)
    String address;

    @Column(name = "address_line_2")
    String addressLine2;

    @Column(name = "city", nullable = false)
    String city;

    @Column(name = "state", nullable = false)
    String state;

    @Column(name = "zipcode", nullable = false)
    String zipcode;

    @Column(name = "country", nullable = false)
    String country;

    @Column(name = "start_date")
    OffsetDateTime startDate;

    @Column(name = "expected_completion")
    OffsetDateTime expectedCompletion;

    @Column(name = "active", nullable = false)
    Boolean active = Boolean.TRUE;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    //convenience for reports/search - one printable location line
    public String getLocation() {
        return String.format("%s, %s, %s %s, %s", address, city, state, zipcode, country);
    }
}
