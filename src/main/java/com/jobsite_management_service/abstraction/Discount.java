package com.jobsite_management_service.abstraction;

import java.math.BigDecimal;

public interface Discount {
    //Decorator for payments - both operations are relative to the price being decorated
    BigDecimal calculateDiscount(BigDecimal basePrice);
    BigDecimal applyDiscount(BigDecimal basePrice);
}
