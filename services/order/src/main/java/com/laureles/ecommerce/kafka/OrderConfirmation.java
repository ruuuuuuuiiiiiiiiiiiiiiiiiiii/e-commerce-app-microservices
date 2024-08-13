package com.laureles.ecommerce.kafka;

import com.laureles.ecommerce.customer.CustomerResponse;
import com.laureles.ecommerce.order.PaymentMethod;
import com.laureles.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
