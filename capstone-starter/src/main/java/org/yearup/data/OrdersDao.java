package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

public interface OrdersDao {
    void createOrder(Order order);
    void addItem(OrderLineItem item);
    Order getById(int orderId);
}
