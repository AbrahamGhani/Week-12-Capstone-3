# E-Commerce API Capstone Project

## Overview

This is a Spring Boot-based E-Commerce API that provides RESTful endpoints for managing products, categories, users, and shopping cart functionality. The project includes a complete web application frontend and comprehensive API backend with JWT authentication and role-based authorization.

## 🎯 My Contribution

I implemented the **Categories functionality** for this E-Commerce API, which includes:

- **CategoriesController** - REST API endpoints for category management
- **CategoryDao Interface** - Data access contract for categories
- **MySqlCategoryDao** - MySQL implementation of category data access
- **Category Model** - Data model for category entities

### Categories API Endpoints

| Method | Endpoint | Description | Authorization |
|--------|----------|-------------|---------------|
| GET | `/categories` | Get all categories | Public |
| GET | `/categories/{id}` | Get category by ID | Public |
| GET | `/categories/{categoryId}/products` | Get products by category | Public |
| POST | `/categories` | Create new category | Admin only |
| PUT | `/categories/{id}` | Update category | Admin only |
| DELETE | `/categories/{id}` | Delete category | Admin only |

## 🏗️ Project Structure

```
capstone-starter/
├── src/main/java/org/yearup/
│   ├── controllers/
│   │   ├── CategoriesController.java     Implementation
│   │   ├── ProductsController.java
│   │   ├── AuthenticationController.java
│   │   └── ShoppingCartController.java
│   ├── data/
│   │   ├── CategoryDao.java             Implementation
│   │   ├── mysql/
│   │   │   ├── MySqlCategoryDao.java      Implementation
│   │   │   ├── MySqlProductDao.java
│   │   │   ├── MySqlUserDao.java
│   │   │   └── MySqlProfileDao.java
│   │   └── ...
│   ├── models/
│   │   ├── Category.java                 Implementation
│   │   ├── Product.java
│   │   ├── User.java
│   │   └── ...
│   └── security/                         
environment
└── capstone-client-web-application/      
frontend
```

## 🚀 Technologies Used

- **Backend**: Spring Boot 2.7.3, Spring Security, Spring JDBC
- **Database**: Microsoft SQL Server
- **Authentication**: JWT (JSON Web Tokens)
- **Frontend**: HTML, CSS, JavaScript, Bootstrap, Axios
- **Build Tool**: Maven
- **Java Version**: 17

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Microsoft SQL Server (or access to the provided database)
- Modern web browser

## 🛠️ Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/AbrahamGhani/Week-12-Capstone-3.git
   cd Week-12_Capstone-3
   ```

2. **Navigate to the project directory**
   ```bash
   cd capstone-starter
   ```

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The API will be available at `http://localhost:8080`

## 🗄️ Database Setup

The application is configured to connect to a remote SQL Server database. The connection details are in `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://skills4it.database.windows.net:1433;database=courses;encrypt=true;trustServerCertificate=false;loginTimeout=30;
spring.datasource.username=YearUp
spring.datasource.password=StrongP@ssword123
```

## 🔐 Authentication

The API uses JWT-based authentication with role-based authorization:

- **Public endpoints**: Categories and Products (read-only)
- **User endpoints**: Shopping cart, profile management
- **Admin endpoints**: Category and Product management

### Default Users
- **Regular User**: Can view products, manage shopping cart
- **Admin User**: Full access to all endpoints including category management


### API Testing with Postman

Postman collections are provided in the `capstone_postman_collections/` directory:
- `easyshop-solo.postman_collection.json` - Basic API testing
- `easyshop-optional-solo.postman_collection.json` - Advanced testing scenarios

### Example API Calls

**Get all categories:**
```bash
GET http://localhost:8080/categories
```

**Create a new category (Admin only):**
```bash
POST http://localhost:8080/categories
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "name": "Electronics",
  "description": "Electronic devices and accessories"
}
```

**Get products by category:**
```bash
GET http://localhost:8080/categories/1/products
```

## 🔧 Key Features Implemented

### Categories Management
- ✅ **CRUD Operations**: Create, Read, Update, Delete categories
- ✅ **RESTful API**: Standard HTTP methods with proper status codes
- ✅ **Error Handling**: Comprehensive exception handling with meaningful error messages
- ✅ **Security**: Role-based access control (Admin-only for write operations)
- ✅ **Database Integration**: MySQL implementation with prepared statements
- ✅ **Data Validation**: Proper input validation and error responses

### Technical Highlights
- **Prepared Statements**: SQL injection prevention
- **Connection Management**: Proper resource cleanup with try-with-resources
- **Exception Handling**: Graceful error handling with appropriate HTTP status codes
- **Security**: Spring Security integration with method-level authorization
- **Cross-Origin Support**: CORS enabled for frontend integration

## 📚 Learning Outcomes

Through this project, I gained experience with:

- **Spring Boot Framework**: REST controllers, dependency injection, configuration
- **Database Access**: JDBC with prepared statements, connection management
- **Security**: JWT authentication, role-based authorization
- **API Design**: RESTful principles, proper HTTP status codes
- **Error Handling**: Exception management, user-friendly error messages
- **Testing**: API testing with Postman, integration testing

## 🤝 Acknowledgments

- **Year Up**: For providing the comprehensive development environment
- **Pluralsight**: For the educational resources and learning platform
- **Spring Framework**: For the robust backend framework
- **Bootstrap**: For the responsive frontend design
- **Instructors**: For guidance throughout the development process

## 📄 License

This project is part of the Year Up Software Development curriculum.

---

**Note**: This project was developed as part of a capstone assignment. The Categories functionality was implemented by me, while the rest of the application was provided as part of the development environment. 