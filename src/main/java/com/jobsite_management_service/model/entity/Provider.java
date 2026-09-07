package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.Inventory;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Provider implements com.jobsite_management_service.abstraction.Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Override
    public Inventory getInventory() {
        return null;
    }
}
