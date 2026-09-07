package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.enums.BusinessType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

//prototype/template target - concrete suppliers are variations of this shape
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"inventory", "volumeDiscountHierarchy", "discounts", "businesses"})
@Table(name = "providers")
public class Provider implements com.jobsite_management_service.abstraction.Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    //stable machine readable key, e.g. HOME_DEPOT
    @Column(name = "code", nullable = false, unique = true)
    String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", nullable = false)
    BusinessType businessType;

    @Column(name = "contact_email")
    String contactEmail;

    @Column(name = "phone")
    String phone;

    @Column(name = "website")
    String website;

    //integration point for adding a new provider
    @Column(name = "api_base_url")
    String apiBaseUrl;

    @Column(name = "active", nullable = false)
    Boolean active = Boolean.TRUE;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    //covariant return satisfies abstraction.Provider#getInventory
    @OneToOne(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    Inventory inventory;

    //singleton - exactly one hierarchy exists per provider
    @OneToOne(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    VolumeDiscountHierarchy volumeDiscountHierarchy;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Discount> discounts = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "providers")
    List<Business> businesses = new ArrayList<>();

    public List<Discount> getDiscounts() {
        return List.copyOf(discounts);
    }

    public List<Business> getBusinesses() {
        return List.copyOf(businesses);
    }
}
