package com.khait_academy.backend.utils;

import com.khait_academy.backend.entities.Discount;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountCalculator {

    public static BigDecimal apply(BigDecimal price, Discount discount) {

        if (discount == null || !discount.isValidNow()) {
            return price;
        }

        BigDecimal result;

        switch (discount.getType()) {
            case PERCENT -> {
                BigDecimal percent = discount.getValue()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

                result = price.subtract(price.multiply(percent));
            }

            case FIXED -> {
                result = price.subtract(discount.getValue());
            }

            default -> result = price;
        }

        // ❗ không cho âm
        return result.max(BigDecimal.ZERO);
    }
}