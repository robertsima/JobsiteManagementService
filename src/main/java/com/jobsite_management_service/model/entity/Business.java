package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.enums.BusinessType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

//mutable composite entity object - a business is composed of its users, jobsites and providers
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"users", "jobsites", "providers"})
@Table(name = "businesses")
public class Business implements com.jobsite_management_service.abstraction.Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", nullable = false)
    BusinessType businessType;

    @Column(name = "tax_id", unique = true)
    String taxId;

    @Column(name = "email")
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "address")
    String address;

    @Column(name = "city")
    String city;

    @Column(name = "state")
    String state;

    @Column(name = "zipcode")
    String zipcode;

    @Column(name = "country")
    String country;

    @Column(name = "active", nullable = false)
    Boolean active = Boolean.TRUE;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    //composition - users and jobsites do not outlive the business that owns them
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    List<User> users = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Jobsite> jobsites = new ArrayList<>();

    //association - providers exist independently of any one business
    @Getter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name = "business_providers",
            joinColumns = @JoinColumn(name = "business_id"),
            inverseJoinColumns = @JoinColumn(name = "provider_id"))
    List<Provider> providers = new ArrayList<>();

    @Override
    public List<com.jobsite_management_service.abstraction.Jobsite> getJobSites() {
        return List.copyOf(jobsites);
    }

    @Override
    public List<com.jobsite_management_service.abstraction.User> getUsers() {
        return List.copyOf(users);
    }

    @Override
    public List<com.jobsite_management_service.abstraction.Provider> getProviders() {
        return List.copyOf(providers);
    }

    public void addUser(User user) {
        users.add(user);
        user.setBusiness(this);
    }

    public void addJobsite(Jobsite jobsite) {
        jobsites.add(jobsite);
        jobsite.setBusiness(this);
    }

    public void addProvider(Provider provider) {
        providers.add(provider);
    }
}
