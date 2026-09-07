package com.jobsite_management_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "users")
public class User implements com.jobsite_management_service.abstraction.User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Column
    String name;

    @Column
    String email;

    @Column
    String createdAt;

    @Override
    public String getUserType() {
        return "";
    }

    @Override
    public String getFullName() {
        return "";
    }

    @Override
    public String getUserId() {
        return "";
    }
}
