package org.yearup.models;

public class OrderMapper {

    public static OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setOrderDate(order.getOrderDate());
        dto.setAddress(order.getAddress());
        dto.setCity(order.getCity());
        dto.setState(order.getState());
        dto.setZip(order.getZip());
        dto.setShippingAmount(order.getShippingAmount());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setLineItems(order.getLineItems());
        return dto;
    }

    public static Order mapToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setUserId(dto.getUserId());
        order.setOrderDate(dto.getOrderDate());
        order.setAddress(dto.getAddress());
        order.setCity(dto.getCity());
        order.setState(dto.getState());
        order.setZip(dto.getZip());
        order.setShippingAmount(dto.getShippingAmount());
        order.setTotalAmount(dto.getTotalAmount());
        order.setLineItems(dto.getLineItems());
        return order;
    }
}
