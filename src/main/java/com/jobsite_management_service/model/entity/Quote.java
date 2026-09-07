package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.Jobsite;
import com.jobsite_management_service.abstraction.Provider;
import com.jobsite_management_service.abstraction.Request;
import com.jobsite_management_service.abstraction.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "quotes")
public class Quote implements Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;


    @Override
    public Jobsite getJobsite() {
        return null;
    }

    @Override
    public Provider getProvider() {
        return null;
    }

    @Override
    public User getRequestor() {
        return null;
    }

    @Override
    public String getStatus() {
        return "";
    }
}
