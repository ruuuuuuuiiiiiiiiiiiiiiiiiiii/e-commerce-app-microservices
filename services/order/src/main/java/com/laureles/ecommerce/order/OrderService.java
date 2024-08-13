package com.laureles.ecommerce.order;

import com.laureles.ecommerce.customer.CustomerClient;
import com.laureles.ecommerce.exception.BusinessException;
import com.laureles.ecommerce.kafka.OrderConfirmation;
import com.laureles.ecommerce.kafka.OrderProducer;
import com.laureles.ecommerce.orderline.OrderLineRequest;
import com.laureles.ecommerce.orderline.OrderLineService;
import com.laureles.ecommerce.product.ProductClient;
import com.laureles.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(OrderRequest request) {
        //check customer --> OpenFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID::"+ request.customerId()));

        //purchase the product --> product microservice (Rest Template)
        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        // persist the order
        var order = this.orderRepository.save(orderMapper.toOrder(request));

        // persist the order lines
        for(PurchaseRequest purchaseRequest: request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // todo start payment process

        // send order confirmation --> notification-ms (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided IS: %d", orderId)));
    }
}
