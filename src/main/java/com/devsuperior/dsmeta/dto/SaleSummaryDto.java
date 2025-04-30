package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.projection.SaleSummaryProjection;
import org.springframework.stereotype.Service;

@Service
public class SaleSummaryDto {
    String sellerName;
    Double total;

    public SaleSummaryDto() {}

    public SaleSummaryDto(String sellerName, Double total) {
        this.sellerName = sellerName;
        this.total = total;
    }

    public SaleSummaryDto(SaleSummaryProjection projection) {
        sellerName = projection.getSellerName();
        total = projection.getTotal();
    }

    public String getSellerName() {
        return sellerName;
    }

    public Double getTotal() {
        return total;
    }
}
