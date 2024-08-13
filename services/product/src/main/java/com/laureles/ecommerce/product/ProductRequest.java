package com.laureles.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,

        @NotNull(message = "Product name is required")
        String name,

        @NotNull(message = "Product description is required")
        String description,

        @Positive(message = "Available quantity should positive")
        double availableQuantity,

        @Positive(message = "Available price should positive")
        BigDecimal price,

        @NotNull(message = "Product category is required")
        Integer categoryId
) {

}
