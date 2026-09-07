package com.jobsite_management_service.model.dto;

import java.time.OffsetDateTime;

public record JobsiteDTO(OffsetDateTime createdAt, OffsetDateTime expectedCompletion,
                         String country, String state, String city, String zipcode,
                         String address, Long userId, Long id) {}