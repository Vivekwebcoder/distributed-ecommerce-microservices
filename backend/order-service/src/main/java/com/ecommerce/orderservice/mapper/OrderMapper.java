package com.ecommerce.orderservice.mapper;

import com.ecommerce.orderservice.dto.request.AddressDto;
import com.ecommerce.orderservice.dto.request.OrderItemRequest;
import com.ecommerce.orderservice.dto.response.OrderItemResponse;
import com.ecommerce.orderservice.dto.response.OrderResponse;
import com.ecommerce.orderservice.dto.response.OrderStatusResponse;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "shippingAddress", source = "shippingAddress", qualifiedByName = "stringToAddressDto")
    @Mapping(target = "billingAddress", source = "billingAddress", qualifiedByName = "stringToAddressDto")
    @Mapping(target = "totalItems", expression = "java(order.getOrderItems() != null ? order.getOrderItems().size() : 0)")
    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponseList(List<Order> orders);

    @Mapping(target = "skuCode", source = "productSku")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItems);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "productSku", source = "skuCode")
    @Mapping(target = "subtotal", expression = "java(itemRequest.getUnitPrice().multiply(java.math.BigDecimal.valueOf(itemRequest.getQuantity())))")
    OrderItem toOrderItemEntity(OrderItemRequest itemRequest);

    @Mapping(target = "status", source = "orderStatus")
    @Mapping(target = "lastUpdated", source = "updatedAt")
    OrderStatusResponse toOrderStatusResponse(Order order);

    @Named("addressDtoToString")
    default String addressDtoToString(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(addressDto);
        } catch (JsonProcessingException e) {
            return addressDto.toString();
        }
    }

    @Named("stringToAddressDto")
    default AddressDto stringToAddressDto(String addressJson) {
        if (addressJson == null || addressJson.isBlank()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(addressJson, AddressDto.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
