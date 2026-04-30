package com.khait_academy.backend.services;

import com.khait_academy.backend.entities.Discount;
import com.khait_academy.backend.repositories.DiscountRepository;
import com.khait_academy.backend.utils.DiscountCalculator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountSelectionService {

    private final DiscountRepository discountRepository;

    // ================= BEST DISCOUNT =================

    public Optional<Discount> getBestDiscount(Long courseId, BigDecimal price) {

        List<Discount> discounts = discountRepository.findValidDiscounts(
                courseId,
                LocalDateTime.now()
        );

        return discounts.stream()
                .min(Comparator.comparing(d ->
                        DiscountCalculator.apply(price, d)
                ));
    }

    public Map<Long, Discount> getBestDiscountMap(
        List<Long> courseIds,
        Map<Long, BigDecimal> priceMap
    ) {

    List<Discount> discounts =
            discountRepository.findValidDiscountsByCourseIds(
                    courseIds,
                    LocalDateTime.now()
            );

    return discounts.stream()
            .collect(Collectors.groupingBy(d -> d.getCourse().getId()))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().stream()
                            .min(Comparator.comparing(d ->
                                    DiscountCalculator.apply(
                                            priceMap.get(entry.getKey()),
                                            d
                                    )
                            ))
                            .orElse(null)
            ));
    }
    // ================= FINAL PRICE =================

    public BigDecimal getFinalPrice(Long courseId, BigDecimal price) {

        return getBestDiscount(courseId, price)
                .map(discount -> DiscountCalculator.apply(price, discount))
                .orElse(price);
    }
}