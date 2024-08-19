package com.rem.aggregator_service.customer_service.service;

import com.rem.aggregator_service.customer_service.dto.CustomerInformation;
import com.rem.aggregator_service.customer_service.entity.Customer;
import com.rem.aggregator_service.customer_service.exceptions.ApplicationExceptions;
import com.rem.aggregator_service.customer_service.mapper.EntityDtoMapper;
import com.rem.aggregator_service.customer_service.repository.CustomerRepository;
import com.rem.aggregator_service.customer_service.repository.PortfolioItemRepository;
import com.rem.customer_service.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public CustomerService(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .flatMap(this::buildCustomerInformation);
    }

    private Mono<CustomerInformation> buildCustomerInformation(Customer customer) {
        return this.portfolioItemRepository.findAllByCustomerId(customer.getId())
                .collectList()
                .map(list -> EntityDtoMapper.toCustomerInformation(customer, list));
    }
}
