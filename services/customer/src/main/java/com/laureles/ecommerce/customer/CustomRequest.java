package com.laureles.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomRequest(
        String id,
        @NotNull(message = "Customer First Name is required")
        String firstname,
        @NotNull(message = "Customer Last Name is required")
        String lastname,
        @NotNull(message = "Customer Email is required")
        @Email(message = "Customer Email is not a valid email address")
        String email,
        Address address
) {
}
