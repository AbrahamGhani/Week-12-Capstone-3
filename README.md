
# 🛍️ E-Commerce API — Capstone Project

## What This Is

This is a Java-based **E-Commerce API** built using **Spring Boot**. It lets users do basic online shopping tasks like:
- Browsing categories and products
- Managing a shopping cart
- Logging in and out (with different user roles like regular user or admin)

This project comes with both:
- A working **backend API**
- A simple **web frontend**

Everything uses **JWT tokens for login**, and there's role-based access (so regular users can’t do admin stuff).

---

## 💻 What I Built

I worked on these parts of the project:

- ✅ `CategoriesController.java` — API controller for managing categories
- ✅ `MySqlCategoryDao.java` — DAO class for category database access
- ✅ `ShoppingCartController.java` — API controller for shopping cart
- ✅ `MySqlShoppingCartDao.java` — DAO class for cart logic
- ✅ `ShoppingCartDao.java` — DAO interface for cart features

Everything else was provided as part of the assignment.

---

## 🧪 Categories API

| Method | Endpoint | What it does | Who can use it |
|--------|----------|-----------------------|---------------|
| GET    | `/categories`                    | Get all categories               | Anyone |
| GET    | `/categories/{id}`              | Get one category by ID          | Anyone |
| GET    | `/categories/{id}/products`     | Get all products in a category  | Anyone |
| POST   | `/categories`                   | Add a new category              | Admin only |
| PUT    | `/categories/{id}`              | Update a category               | Admin only |
| DELETE | `/categories/{id}`              | Delete a category               | Admin only |

---

## 🔍 Project Folder Overview

```
capstone-starter/
├── controllers/
│   ├── CategoriesController.java         
│   └── ShoppingCartController.java       
├── data/
│   ├── ShoppingCartDao.java              
│   └── mysql/
│       ├── MySqlCategoryDao.java         
│       └── MySqlShoppingCartDao.java     
├── models/
│   └── ...                               # Provided by instructor
└── capstone-client-web-application/      # Provided web frontend
```

---

## 🧰 Tools & Tech

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **JDBC (SQL)**
- **Microsoft SQL Server**
- **JWT (login tokens)**
- **Bootstrap + JavaScript + Axios** (frontend)

---

## ⚙️ How To Run It

> You’ll need Java 17 and Maven.

```bash
git clone https://github.com/AbrahamGhani/Week-12-Capstone-3.git
cd Week-12-Capstone-3/capstone-starter
./mvnw clean install
./mvnw spring-boot:run
```

Once running:  
🔗 `http://localhost:8080`

---

## 🛢️ Database Info

The app connects to a remote SQL Server. You don’t have to set anything up.  
Info is in `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://skills4it.database.windows.net:1433;...
spring.datasource.username=YearUp
spring.datasource.password=StrongP@ssword123
```

---

## 🔐 Login & Roles

- **User**: Can view products and manage their own cart
- **Admin**: Can add/edit/delete categories & products

---

## 🧪 Try It Out With Postman

Postman test files are included:
- `easyshop-solo.postman_collection.json`
- `easyshop-optional-solo.postman_collection.json`

Example API Call:
```bash
GET http://localhost:8080/categories
```

---

## ✅ Features I Helped Build

- Categories CRUD (Create, Read, Update, Delete)
- Shopping Cart logic
- Role-based access (User vs Admin)
- Input validation & error handling
- SQL injection prevention (via prepared statements)

---

## 📘 What I Learned

- How to use **Spring Boot** to build REST APIs
- How to manage DB access with **DAOs and JDBC**
- How to secure APIs using **JWT tokens** and role checks
- How to organize a backend project
- How to test APIs with Postman

---

## 🙏 Thanks To

- Year Up
- Spring Boot + Java community
- Instructors & Pluralsight

---

> This was a capstone project. I built the shopping cart and category features, while the rest of the project (users, products, login system, etc.) was already set up.
