package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.Jobsite;
import com.jobsite_management_service.abstraction.Provider;
import com.jobsite_management_service.abstraction.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//mutable composite entity object
@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Business implements com.jobsite_management_service.abstraction.Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_id")
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_id")
    List<Jobsite> jobsites;

    @Override
    public List<Jobsite> getJobSites() {
        return List.of();
    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public List<Provider> getProviders() {
        return List.of();
    }
}
