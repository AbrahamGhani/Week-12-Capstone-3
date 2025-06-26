package org.yearup.data;

import org.yearup.models.ShoppingCart;


public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    void addToCart(int userId, int productId);
    void clearCart(int userId);
    boolean itemExistsInCart(int userId, int productId);
    void updateItemQuantity(int userId, int productId, int quantity);

}
