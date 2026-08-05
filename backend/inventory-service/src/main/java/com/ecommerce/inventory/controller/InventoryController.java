package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.request.InventoryCreateRequest;
import com.ecommerce.inventory.dto.request.StockCheckRequest;
import com.ecommerce.inventory.dto.request.StockUpdateRequest;
import com.ecommerce.inventory.dto.response.ApiResponse;
import com.ecommerce.inventory.dto.response.InventoryResponse;
import com.ecommerce.inventory.dto.response.StockCheckResponse;
import com.ecommerce.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventory REST API", description = "Endpoints for managing SKU stock, allocations, and batch availability checks")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Initialize stock entry for a new SKU")
    public ResponseEntity<ApiResponse<InventoryResponse>> createInventory(
            @Valid @RequestBody InventoryCreateRequest request) {
        log.info("REST Request: Create inventory for SKU: {}", request.getSkuCode());
        InventoryResponse response = inventoryService.createInventory(request);
        return new ResponseEntity<>(ApiResponse.success(response, "Inventory created successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/{skuCode}/stock")
    @Operation(summary = "Update physical stock level (Admin Restock)")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateStock(
            @PathVariable String skuCode,
            @Valid @RequestBody StockUpdateRequest request) {
        log.info("REST Request: Update stock for SKU: {}", skuCode);
        InventoryResponse response = inventoryService.updateStock(skuCode, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock updated successfully"));
    }

    @PostMapping("/reduce")
    @Operation(summary = "Directly reduce stock level")
    public ResponseEntity<ApiResponse<InventoryResponse>> reduceStock(
            @Valid @RequestBody StockUpdateRequest request) {
        log.info("REST Request: Reduce stock for SKU: {}", request.getSkuCode());
        InventoryResponse response = inventoryService.reduceStock(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock reduced successfully"));
    }

    @PostMapping("/increase")
    @Operation(summary = "Directly increase stock level")
    public ResponseEntity<ApiResponse<InventoryResponse>> increaseStock(
            @Valid @RequestBody StockUpdateRequest request) {
        log.info("REST Request: Increase stock for SKU: {}", request.getSkuCode());
        InventoryResponse response = inventoryService.increaseStock(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock increased successfully"));
    }

    @PostMapping("/check")
    @Operation(summary = "Batch check availability for multiple SKUs")
    public ResponseEntity<ApiResponse<List<StockCheckResponse>>> checkStock(
            @Valid @RequestBody List<StockCheckRequest> requests) {
        log.info("REST Request: Batch check availability for {} items", requests.size());
        List<StockCheckResponse> responses = inventoryService.checkStock(requests);
        return ResponseEntity.ok(ApiResponse.success(responses, "Stock availability check completed"));
    }

    @GetMapping("/{skuCode}")
    @Operation(summary = "Fetch inventory detail by SKU code")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryBySku(
            @PathVariable String skuCode) {
        log.info("REST Request: Fetch inventory for SKU: {}", skuCode);
        InventoryResponse response = inventoryService.getInventoryBySku(skuCode);
        return ResponseEntity.ok(ApiResponse.success(response, "Inventory fetched successfully"));
    }
}
