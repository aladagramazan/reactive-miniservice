package com.rem.aggregator_service.customer_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private Integer id;
    private String name;
    private Integer balance;
}
