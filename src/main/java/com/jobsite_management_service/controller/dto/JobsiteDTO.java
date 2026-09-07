package com.jobsite_management_service.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class JobsiteDTO {
    Long id;
    Long userId;
    String address;
    String zipcode;
    String city;
    String state;
    String country;
    OffsetDateTime expectedCompletion;
    OffsetDateTime createdAt;

    public JobsiteDTO(OffsetDateTime createdAt, OffsetDateTime expectedCompletion, String country, String state, String city, String zipcode, String address, Long userId, Long id) {
        this.createdAt = createdAt;
        this.expectedCompletion = expectedCompletion;
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipcode = zipcode;
        this.address = address;
        this.userId = userId;
        this.id = id;
    }
}
