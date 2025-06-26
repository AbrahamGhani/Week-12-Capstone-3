package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

// This class handles shopping cart database operations using MySQL.
// It extends a base DAO class to inherit database connection logic and implements the ShoppingCartDao interface.
@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private final ProductDao productDao; // Used to look up product details for each cart item

    // Constructor accepts DataSource for DB connections and ProductDao to fetch products
    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    // Retrieves a user's shopping cart and all its items
    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();  // New empty cart

        String sql = "SELECT * FROM shopping_cart WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);  // Bind user ID
            ResultSet rs = stmt.executeQuery();  // Execute query

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");

                // Use ProductDao to get full product details
                Product product = productDao.getById(productId);

                if (product != null) {
                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    item.setDiscountPercent(BigDecimal.ZERO); // Default to no discount
                    cart.add(item); // Add item to cart
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading shopping cart for user ID: " + userId, e);
        }

        return cart;
    }

    // Adds a product to the user's cart (or increments if it already exists)
    @Override
    public void addToCart(int userId, int productId) {
        if (itemExistsInCart(userId, productId)) {
            incrementQuantity(userId, productId);  // If already in cart, increase quantity
        } else {
            addNewProduct(userId, productId);  // Otherwise, insert new record
        }
    }

    // Checks if a product is already in the cart
    @Override
    public boolean itemExistsInCart(int userId, int productId) {
        String sql = "SELECT COUNT(*) FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // If count > 0, item exists
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR checking if product exists in cart", e);
        }

        return false;
    }

    // Adds a new product to the cart
    private void addNewProduct(int userId, int productId) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);

            stmt.executeUpdate();  // Insert row with quantity 1
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert product into shopping cart.", e);
        }
    }

    // Increments the quantity of a product already in the cart
    private void incrementQuantity(int userId, int productId) {
        String sql = "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);

            stmt.executeUpdate();  // Increment quantity
        } catch (SQLException e) {
            throw new RuntimeException("Failed to increment cart quantity.", e);
        }
    }

    // Clears the entire cart for a user
    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            stmt.executeUpdate();  // Remove all items for user
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear shopping cart.", e);
        }
    }

    // Updates the quantity of a specific product in the cart
    @Override
    public void updateItemQuantity(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, quantity);      // New quantity
            statement.setInt(2, userId);        // Target user
            statement.setInt(3, productId);     // Target product

            statement.executeUpdate();          // Execute update
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update cart item.");
        }
    }
}
