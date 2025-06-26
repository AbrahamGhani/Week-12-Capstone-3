package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.OrderItem;

public interface OrdersDao {
    void createOrder(Order order);
    void addItem(OrderItem item);
}
