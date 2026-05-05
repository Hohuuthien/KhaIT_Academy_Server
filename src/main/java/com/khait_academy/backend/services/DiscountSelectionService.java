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

        BigDecimal safePrice = safePrice(price);
        LocalDateTime now = LocalDateTime.now();

        return discountRepository.findValidDiscounts(courseId, now)
                .stream()
                .min(Comparator.comparing(d ->
                        DiscountCalculator.apply(safePrice, d)
                ));
    }

    // ================= MAP BEST DISCOUNT =================
    public Map<Long, Discount> getBestDiscountMap(
            List<Long> courseIds,
            Map<Long, BigDecimal> priceMap
    ) {

        LocalDateTime now = LocalDateTime.now();

        List<Discount> discounts =
                discountRepository.findValidDiscountsByCourseIds(courseIds, now);

        return discounts.stream()
                .collect(Collectors.groupingBy(d -> d.getCourse().getId()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .min(Comparator.comparing(d ->
                                        DiscountCalculator.apply(
                                                safePrice(priceMap.get(entry.getKey())),
                                                d
                                        )
                                ))
                                .orElse(null) // có thể giữ hoặc filter phía dưới
                ));
    }

    // ================= FINAL PRICE =================
    public BigDecimal getFinalPrice(Long courseId, BigDecimal price) {

        BigDecimal safePrice = safePrice(price);

        return getBestDiscount(courseId, safePrice)
                .map(d -> DiscountCalculator.apply(safePrice, d))
                .orElse(safePrice);
    }

    // ================= HELPER =================
    private BigDecimal safePrice(BigDecimal price) {
        return price == null ? BigDecimal.ZERO : price;
    }
}