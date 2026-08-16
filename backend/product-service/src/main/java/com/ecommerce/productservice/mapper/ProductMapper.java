package com.ecommerce.productservice.mapper;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @org.mapstruct.Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product entity);

    List<ProductResponse> toResponseList(List<Product> entities);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product entity);
}
