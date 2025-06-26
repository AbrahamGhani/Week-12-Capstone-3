package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

// This is a REST controller responsible for handling HTTP requests related to a user's shopping cart.
// The base URL for all routes in this class is /cart.
@RestController
@RequestMapping("cart")
@CrossOrigin // Allows this controller to be accessed from a different domain (useful for front-end apps)
public class ShoppingCartController
{
    // Declare dependencies required to access and manipulate user and cart data
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    // Use constructor injection to automatically supply the required DAOs (Data Access Objects)
    @Autowired
    public ShoppingCartController(UserDao userDao,
                                  ShoppingCartDao shoppingCartDao,
                                  ProductDao productDao)
    {
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
        this.productDao = productDao;
    }

    // GET /cart
    // Retrieves the full shopping cart for the currently logged-in user.
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')") // Only authenticated users with role "USER" can access this
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // Spring Security will pass the logged-in user's name into the 'Principal' object
            String userName = principal.getName();

            // Look up the full user record from the database
            User user = userDao.getByUserName(userName);

            if (user == null) {
                // If no matching user is found, return HTTP 404 (Not Found)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userName);
            }

            // Use the DAO to get this user's cart and return it to the caller
            return shoppingCartDao.getByUserId(user.getId());
        }
        catch(Exception e)
        {
            // If something unexpected happens, return a generic server error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... error with getCart.");
        }
    }

    // POST /cart/products/{productId}
    // Adds a product to the shopping cart. If it already exists, the DAO should increase its quantity.
    @PostMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal)
    {
        // Get the username of the logged-in user
        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Add the product to the cart in the database
        shoppingCartDao.addToCart(user.getId(), productId);

        // Return the updated cart back to the user
        return shoppingCartDao.getByUserId(user.getId());
    }

    // PUT /cart/products/{productId}
    // Updates the quantity of an existing product in the user's cart.
    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCart updateCartItem(@PathVariable int productId,
                                       @RequestBody Map<String, Integer> body,
                                       Principal principal)
    {
        // Get the current user
        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Extract the quantity from the request body
        int quantity = body.get("quantity");

        // Update that product's quantity in the cart
        shoppingCartDao.updateItemQuantity(user.getId(), productId, quantity);

        // Return the updated cart so the frontend can refresh the view
        return shoppingCartDao.getByUserId(user.getId());
    }

    // DELETE /cart
    // Completely clears out the user's shopping cart.
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void clearCart(Principal principal, HttpServletResponse response) throws IOException
    {
        try {
            // Identify the current user
            String username = principal.getName();
            User user = userDao.getByUserName(username);

            if (user == null) {
                // Send an HTTP 404 if the user is not found
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            // Clear all items for this user
            shoppingCartDao.clearCart(user.getId());
        } catch (Exception e) {
            // If any issue occurs, return an HTTP 500 response with a clear message
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to clear shopping cart.");
        }
    }
}
