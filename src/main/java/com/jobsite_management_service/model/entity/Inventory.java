package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.Item;
import com.jobsite_management_service.abstraction.Provider;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Inventory implements com.jobsite_management_service.abstraction.Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    Long providerID;
    String providerName;
    Map<Integer, Item> itemsList; //volume, item


    @Override
    public void addItem(Item item, int quantity) {

    }

    @Override
    public void removeItem(Item item, int quantity) {

    }

    @Override
    public int getStockCount(Item item) {
        return 0;
    }

    @Override
    public boolean isInStock(String sku) {
        return false;
    }

    @Override
    public void Restock(Provider provider) {

    }
}
