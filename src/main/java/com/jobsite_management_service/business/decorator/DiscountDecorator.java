package com.jobsite_management_service.business.decorator;

import com.jobsite_management_service.abstraction.Discount;

import java.math.BigDecimal;

public class DiscountDecorator implements Discount {
    @Override
    public BigDecimal calculateDiscount(BigDecimal basePrice) {
        return null;
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal basePrice) {
        return null;
    }
}
