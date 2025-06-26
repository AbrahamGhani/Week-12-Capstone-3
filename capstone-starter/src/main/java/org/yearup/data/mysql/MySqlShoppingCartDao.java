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

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao{

    private final ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId){
        ShoppingCart cart = new ShoppingCart();

        String sql = "SELECT * " +
                "FROM shopping_cart " +
                "WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");

                // Use ProductDao to get full Product info
                Product product = productDao.getById(productId);

                if (product != null)
                {
                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    item.setDiscountPercent(BigDecimal.ZERO); // default to no discount

                    cart.add(item);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error loading shopping cart for user ID: " + userId, e);
        }

        return cart;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Override
public void addToCart(int userId, int productId) {
    if (itemExistsInCart(userId, productId)) {
        incrementQuantity(userId, productId);
    } else {
        addNewProduct(userId, productId);
    }
}

    @Override
    public boolean itemExistsInCart(int userId, int productId) {
        String sql = "SELECT COUNT(*) FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR on checking if product exists in cart", e);
        }
        return false;
    }

    private void addNewProduct(int userId, int productId) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("ERROR on adding new product to cart", e);
        }
    }
    private void incrementQuantity(int userId, int productId) {
        String sql = "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("ERROR on incrementing quantity of product in cart", e);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public void clearCart(int userId){
    String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql))
    {
        stmt.setInt(1, userId);
        stmt.executeUpdate();
    }
    catch (SQLException e){
        throw new RuntimeException("Error clearing shopping cart for user ID: " + userId, e);
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateItemQuantity(int userId, int productId, int quantity)
    {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to update cart item.");
        }
    }

}
