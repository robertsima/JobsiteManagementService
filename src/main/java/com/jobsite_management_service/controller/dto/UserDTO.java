package com.jobsite_management_service.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDTO {
    Long id;
    String name;
    String email;
    String createdAt;

    public UserDTO(String createdAt, String email, String name, Long id) {
        this.createdAt = createdAt;
        this.email = email;
        this.name = name;
        this.id = id;
    }
}
