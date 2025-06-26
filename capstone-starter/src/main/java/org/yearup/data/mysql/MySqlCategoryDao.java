package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component  // Allows Spring to discover and inject this DAO class
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    // Constructor receives a DataSource and passes it to the base class
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    // Retrieves all categories from the database
    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM categories";  // SQL to fetch all rows

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet row = statement.executeQuery();

            // Loop through each row and convert it to a Category object
            while (row.next())
            {
                Category category = mapRow(row);
                categories.add(category);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);  // Converts checked exception to unchecked
        }

        return categories;  // Return all fetched categories
    }

    // Retrieves a category by its ID
    @Override
    public Category getById(int categoryId)
    {
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);  // Set parameter for ID

            ResultSet row = statement.executeQuery();

            if (row.next()) {
                return mapRow(row);  // Convert to object if found
            } else {
                return null;  // Return null if no match found
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    // Inserts a new category and returns the inserted object with the new ID
    @Override
    public Category create(Category category)
    {
        String sql = "INSERT INTO categories(name, description) VALUES (?, ?)";

        try (Connection connection = getConnection())
        {
            // Request to return the generated ID
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int category_id = generatedKeys.getInt(1);
                    return getById(category_id);  // Fetch and return full object
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return null;  // If insert failed
    }

    // Updates an existing category by ID
    @Override
    public void update(int categoryId, Category category)
    {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category.getName());         // New name
            statement.setString(2, category.getDescription());  // New description
            statement.setInt(3, categoryId);                    // ID to update

            statement.executeUpdate();  // Execute update statement
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    // Deletes a category by its ID
    @Override
    public void delete(int categoryId)
    {
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);  // Set the ID to delete

            statement.executeUpdate();  // Run the delete operation
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    // Converts a ResultSet row into a Category object
    private Category mapRow(ResultSet row) throws SQLException
    {
        // Extract columns from the current row
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        // Populate a new Category object
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setName(name);
        category.setDescription(description);

        return category;
    }
}
