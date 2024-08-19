package com.rem.aggregator_service.customer_service.entity;

import com.rem.aggregator_service.customer_service.domain.Ticker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioItem {

    @Id
    private Integer id;
    private Integer customerId;
    private Ticker ticker;
    private Integer quantity;
}
