package com.jobsite_management_service.abstraction;

import java.math.BigDecimal;

public interface Item {
    String getSku();              // Essential identification
    String getName();
    BigDecimal getPrice();
    String getColor();
    String getMaterial();
    String getDimensions();
    String getDescription();
    String getWeight();
}
