package com.ecommerce.inventory.mapper;

import com.ecommerce.inventory.dto.request.InventoryCreateRequest;
import com.ecommerce.inventory.dto.response.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public InventoryResponse toResponse(Inventory inventory) {
        if (inventory == null) {
            return null;
        }

        return InventoryResponse.builder()
                .id(inventory.getId())
                .skuCode(inventory.getSkuCode())
                .quantity(inventory.getQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .reorderThreshold(inventory.getReorderThreshold())
                .status(inventory.getStatus())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }

    @Override
    public Inventory toEntity(InventoryCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Inventory.builder()
                .skuCode(request.getSkuCode())
                .quantity(request.getQuantity())
                .reorderThreshold(request.getReorderThreshold() != null ? request.getReorderThreshold() : 10)
                .reservedQuantity(0)
                .build();
    }

    @Override
    public List<InventoryResponse> toResponseList(List<Inventory> inventories) {
        if (inventories == null) {
            return null;
        }

        List<InventoryResponse> list = new ArrayList<>(inventories.size());
        for (Inventory inventory : inventories) {
            list.add(toResponse(inventory));
        }

        return list;
    }
}
