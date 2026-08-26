package com.jobsite_management_service.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CustomerDTO {
    Long id;
    String name;
    String email;
    String createdAt;

    public CustomerDTO(String createdAt, String email, String name, Long id) {
        this.createdAt = createdAt;
        this.email = email;
        this.name = name;
        this.id = id;
    }
}
