package com.jobsite_management_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"provider", "items"})
@Table(name = "inventories")
public class Inventory implements com.jobsite_management_service.abstraction.Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false, unique = true)
    Provider provider;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "last_restocked_at")
    OffsetDateTime lastRestockedAt;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return List.copyOf(items);
    }

    @Override
    public void addItem(com.jobsite_management_service.abstraction.Item item, int quantity) {
        Item stocked = resolve(item);

        if (stocked == null) {
            return;
        }

        if (!items.contains(stocked)) {
            stocked.setInventory(this);
            items.add(stocked);
        }

        stocked.setStockQuantity(stocked.getStockQuantity() + quantity);
    }

    @Override
    public void removeItem(com.jobsite_management_service.abstraction.Item item, int quantity) {
        Item stocked = resolve(item);

        if (stocked == null) {
            return;
        }

        stocked.setStockQuantity(Math.max(0, stocked.getStockQuantity() - quantity));
    }

    @Override
    public int getStockCount(com.jobsite_management_service.abstraction.Item item) {
        Item stocked = resolve(item);

        return stocked == null ? 0 : stocked.getStockQuantity();
    }

    @Override
    public boolean isInStock(String sku) {
        return items.stream()
                .anyMatch(i -> Objects.equals(i.getSku(), sku) && i.getStockQuantity() > 0);
    }

    @Override
    public void Restock(com.jobsite_management_service.abstraction.Provider provider) {
        //complex business operation - refresh stock counts from the supplier feed
        this.lastRestockedAt = OffsetDateTime.now();
    }

    //match on identity first, then on SKU, so a detached abstraction still resolves
    private Item resolve(com.jobsite_management_service.abstraction.Item item) {
        if (item == null) {
            return null;
        }

        if (item instanceof Item concrete && items.contains(concrete)) {
            return concrete;
        }

        return items.stream()
                .filter(i -> Objects.equals(i.getSku(), item.getSku()))
                .findFirst()
                .orElse(item instanceof Item concrete ? concrete : null);
    }
}
