package com.laureles.ecommerce.payment;

import com.laureles.ecommerce.customer.CustomerResponse;
import com.laureles.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(

        BigDecimal amount,

        PaymentMethod paymentMethod,

        Integer orderId,

        String orderReference,

        CustomerResponse customer

) {
}
