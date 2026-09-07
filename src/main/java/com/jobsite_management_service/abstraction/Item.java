package com.jobsite_management_service.abstraction;

public interface Item {
    String getSku();              // Essential identification
    String getName();
    double getPrice();

    String getColor();
    String getMaterial();
    String getDimensions();
    String getDescripton();
    String getWeight();
}
