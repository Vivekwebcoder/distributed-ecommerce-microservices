package com.ecommerce.inventory.mapper;

import com.ecommerce.inventory.dto.request.InventoryCreateRequest;
import com.ecommerce.inventory.dto.response.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;

import java.util.List;

public interface InventoryMapper {

    InventoryResponse toResponse(Inventory inventory);

    Inventory toEntity(InventoryCreateRequest request);

    List<InventoryResponse> toResponseList(List<Inventory> inventories);
}
