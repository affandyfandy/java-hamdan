package com.midterm.group4.dto;

import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.dto.request.CreateOrderItemDTO;
import com.midterm.group4.dto.response.ReadOrderItemDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class OrderItemMapperTest {

    private final OrderItemMapper orderItemMapper = Mappers.getMapper(OrderItemMapper.class);

    @Test
     void testToEntity() {
        UUID productId = UUID.randomUUID();
        CreateOrderItemDTO dto = new CreateOrderItemDTO(productId, 5);

        OrderItem orderItem = orderItemMapper.toEntity(dto);

        assertEquals(productId, orderItem.getProduct().getProductId());
        assertEquals(5, orderItem.getQuantity());
    }

    @Test
     void testToListEntity() {
        UUID productId = UUID.randomUUID();
        CreateOrderItemDTO dto = new CreateOrderItemDTO(productId, 5);

        List<OrderItem> orderItems = orderItemMapper.toListEntity(List.of(dto));

        assertEquals(1, orderItems.size());
        assertEquals(productId, orderItems.get(0).getProduct().getProductId());
        assertEquals(5, orderItems.get(0).getQuantity());
    }

    @Test
     void testToListReadOrderItemDto() {
        UUID orderItemId = UUID.randomUUID();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(orderItemId);

        List<ReadOrderItemDTO> dtoList = orderItemMapper.toListReadOrderItemDto(List.of(orderItem));

        assertEquals(1, dtoList.size());
        assertEquals(orderItemId, dtoList.get(0).getOrderItemId());
    }
}
