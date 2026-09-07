package com.jobsite_management_service.abstraction;

public interface Inventory {
    void addItem(Item item, int quantity);       // Domain action (not generic setter)
    void removeItem(Item item, int quantity);    // Domain action

    int getStockCount(Item item);                // Query
    boolean isInStock(String sku);               // Query

    void Restock(Provider provider);             // Complex business operation
}
