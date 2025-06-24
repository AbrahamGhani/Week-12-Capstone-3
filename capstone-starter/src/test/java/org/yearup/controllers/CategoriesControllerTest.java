package org.yearup.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriesControllerTest {

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private CategoriesController categoriesController;

    private Category testCategory;
    private List<Category> testCategories;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setCategoryId(1);
        testCategory.setName("Electronics");
        testCategory.setDescription("Electronic devices and accessories");

        testCategories = new ArrayList<>();
        testCategories.add(testCategory);
        
        Category category2 = new Category();
        category2.setCategoryId(2);
        category2.setName("Clothing");
        category2.setDescription("Apparel and fashion items");
        testCategories.add(category2);
    }

    @Test
    void getAll_shouldReturnAllCategories_whenSuccessful() {
        // Arrange
        when(categoryDao.getAllCategories()).thenReturn(testCategories);

        // Act
        List<Category> result = categoriesController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        verify(categoryDao, times(1)).getAllCategories();
    }

    @Test
    void getById_shouldReturnCategory_whenCategoryExists() {
        // Arrange
        int categoryId = 1;
        when(categoryDao.getById(categoryId)).thenReturn(testCategory);

        // Act
        Category result = categoriesController.getById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getCategoryId());
        assertEquals("Electronics", result.getName());
        verify(categoryDao, times(1)).getById(categoryId);
    }

    @Test
    void getById_shouldThrowNotFoundException_whenCategoryNotFound() {
        // Arrange
        int categoryId = 999;
        when(categoryDao.getById(categoryId)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoriesController.getById(categoryId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getProductsById_shouldReturnProducts_whenSuccessful() {
        // Arrange
        int categoryId = 1;
        List<Product> testProducts = new ArrayList<>();
        
        Product product1 = new Product();
        product1.setProductId(1);
        product1.setName("Smartphone");
        product1.setPrice(new BigDecimal("499.99"));
        product1.setCategoryId(categoryId);
        testProducts.add(product1);

        when(productDao.listByCategoryId(categoryId)).thenReturn(testProducts);

        // Act
        List<Product> result = categoriesController.getProductsById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Smartphone", result.get(0).getName());
        verify(productDao, times(1)).listByCategoryId(categoryId);
    }

    @Test
    void addCategory_shouldReturnCreatedCategory_whenSuccessful() {
        // Arrange
        Category newCategory = new Category();
        newCategory.setName("Books");
        newCategory.setDescription("Books and literature");

        Category createdCategory = new Category();
        createdCategory.setCategoryId(3);
        createdCategory.setName("Books");
        createdCategory.setDescription("Books and literature");

        when(categoryDao.create(any(Category.class))).thenReturn(createdCategory);

        // Act
        Category result = categoriesController.addCategory(newCategory);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getCategoryId());
        assertEquals("Books", result.getName());
        verify(categoryDao, times(1)).create(any(Category.class));
    }

    @Test
    void getAll_shouldThrowInternalServerError_whenExceptionOccurs() {
        // Arrange
        when(categoryDao.getAllCategories()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoriesController.getAll();
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("Oops... our bad.", exception.getReason());
    }
} 