package com.laureles.ecommerce.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomRequest request) {

        if (request == null) {
            return null;
        } else {
            return Customer.builder()
                    .id(request.id())
                    .firstname(request.firstname())
                    .lastname(request.lastname())
                    .email(request.email())
                    .address(request.address())
                    .build();
        }
    }

    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}
