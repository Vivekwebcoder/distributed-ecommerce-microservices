package com.ecommerce.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String brand;
    private String category;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String seller;
    private String imageUrl;
    private Boolean active;
}
