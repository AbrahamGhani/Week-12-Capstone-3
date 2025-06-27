package org.yearup.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.OrdersDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.yearup.models.OrderMapper.mapToDTO;
import static org.yearup.models.OrderMapper.mapToEntity;

@RestController
@RequestMapping("orders")
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class OrdersController {

    private final ShoppingCartDao shoppingCartDao;
    private final OrdersDao ordersDao;
    private final UserDao userDao;
    private final ProfileDao profileDao;

    @Autowired
    public OrdersController(ShoppingCartDao shoppingCartDao,
                           OrdersDao orderDao,
                           UserDao userDao,
                           ProfileDao profileDao) { // <-- inject here
        this.shoppingCartDao = shoppingCartDao;
        this.ordersDao = orderDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderDTO> checkout(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUserName(username);
        Profile profile = profileDao.getByUserId(user.getId());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ShoppingCart cart = shoppingCartDao.getByUserId(user.getId());

        if (cart == null || cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Calculate total
        BigDecimal total = cart.getTotal();

        // Create the Order
        Order order = new Order();
        order.setUserId(user.getId());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setShippingAmount(new BigDecimal("5.99"));
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());

        ordersDao.createOrder(order);

        List<OrderLineItem> lineItems = new ArrayList<>();

        for (ShoppingCartItem item : cart.getItems().values()) {
            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setOrderId(order.getId());
            lineItem.setProductId(item.getProductId());
            lineItem.setQuantity(item.getQuantity());
            lineItem.setPrice(item.getProduct().getPrice());
            lineItem.setDiscount(BigDecimal.ZERO); // assume no discount

            ordersDao.addItem(lineItem);
            lineItems.add(lineItem);
        }

        // Clear the cart
        shoppingCartDao.clearCart(user.getId());

        // Build and return OrderDTO
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setOrderDate(order.getOrderDate());
        dto.setShippingAmount(order.getShippingAmount());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setAddress(order.getAddress());
        dto.setCity(order.getCity());
        dto.setState(order.getState());
        dto.setZip(order.getZip());
        dto.setLineItems(lineItems);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }



//    @PostMapping
//    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO dto) {
//        Order order = OrderMapper.mapToEntity(dto);
//        ordersDao.createOrder(order); // saves and sets generated ID
//
//        // Fetch full order with line items using new ID
//        Order fullOrder = ordersDao.getById(order.getId());
//
//        OrderDTO resultDto = OrderMapper.mapToDTO(fullOrder); // convert back to DTO
//        return ResponseEntity.created(URI.create("/orders/" + resultDto.getId()))
//                .body(resultDto);
//    }




}
