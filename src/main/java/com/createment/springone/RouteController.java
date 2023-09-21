package com.createment.springone;

import com.mysql.cj.xdevapi.Table;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RouteController {

    final String DB_HOST = "jdbc:mysql://localhost:3306/bookstore";
    final String DB_USER = "bookstoreuser";
    final String DB_PASS = "bookstorepassword";

    private ResultSet runQuery(String query) {
        ResultSet rs;
        try {
            Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASS);
            Statement stmt = conn.createStatement();

            // Execute a SQL query to get all books
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rs;
    }

    private TableData populateBooksModel(String sqlQuery) {
        TableData tableDataToReturn = new TableData();
        try {
            ResultSet resultSet = runQuery(sqlQuery);

            // Create a HashMap to hold the results
            Map<Integer, Map<String, String>> books = new HashMap<>();

            // Loop through the ResultSet and add each book to the HashMap
            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                double price = resultSet.getDouble("price");
                int year = resultSet.getInt("year");

                Map<String, String> book = new HashMap<>();

                book.put("title", title);
                book.put("author", author);
                book.put("year", String.valueOf(year));
                book.put("price", String.format("%.2f", price));

                books.put(id, book);
            }

            // Add the HashMap to the model and return the Thymeleaf template name
            tableDataToReturn.data = books;
            tableDataToReturn.tableName = "Books";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tableDataToReturn;
    }

    private TableData populateBooksV2Model(String sqlQuery) {
        TableData tableDataToReturn = new TableData();
        try {
            ResultSet resultSet = runQuery(sqlQuery);

            // Create a HashMap to hold the results
            Map<Integer, Map<String, String>> books = new HashMap<>();

            // Loop through the ResultSet and add each book to the HashMap
            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                double price = resultSet.getDouble("price");
                int year = resultSet.getInt("year");
                String isbn = resultSet.getString("ISBN");

                Map<String, String> book = new HashMap<>();

                book.put("title", title);
                book.put("author_id", String.valueOf(authorId));
                book.put("year", String.valueOf(year));
                book.put("price", String.format("%.2f", price));
                book.put("ISBN", isbn);
                System.out.println("Chicken sandwich");

                books.put(id, book);
            }

            // Add the HashMap to the model and return the Thymeleaf template name
            tableDataToReturn.data = books;
            tableDataToReturn.tableName = "Books_v2";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tableDataToReturn;
    }

    private TableData populateCustomersModel(String sqlQuery) {
        TableData tableDataToReturn = new TableData();
        try {
            ResultSet resultSet = runQuery(sqlQuery);

            // Create a HashMap to hold the results
            Map<Integer, Map<String, String>> customers = new HashMap<>();

            // Loop through the ResultSet and add each book to the HashMap
            while (resultSet.next()) {
                int id = resultSet.getInt("customer_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");

                Map<String, String> book = new HashMap<>();

                book.put("first_name", firstName);
                book.put("last_name", lastName);
                book.put("email", email);

                customers.put(id, book);
            }

            // Add the HashMap to the model and return the Thymeleaf template name
            tableDataToReturn.data = customers;
            tableDataToReturn.tableName = "Customers";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tableDataToReturn;
    }

    @GetMapping("/customer")
    public String getCustomers(Model model) {
        String sqlQuery = "SELECT * FROM customers";

        TableData booksTableData = populateCustomersModel(sqlQuery);

        model.addAttribute("data", booksTableData.data);
        model.addAttribute("tableName", booksTableData.tableName);

        return "table";
    }

    @GetMapping("/customer/last")
    public String getLastCustomer(Model model) {
        String sqlQuery = "SELECT * FROM customers ORDER BY customer_id DESC LIMIT 1";

        TableData booksTableData = populateCustomersModel(sqlQuery);

        model.addAttribute("data", booksTableData.data);
        model.addAttribute("tableName", booksTableData.tableName);

        return "table";
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        String sqlQuery = "SELECT * FROM books";

        TableData booksTableData = populateBooksModel(sqlQuery);

        model.addAttribute("data", booksTableData.data);
        model.addAttribute("tableName", booksTableData.tableName);

        return "table";
    }

    @GetMapping("/booksv2")
    public String getBooksV2(Model model) {
        String sqlQuery = "SELECT * FROM books_v2";

        TableData booksTableData = populateBooksV2Model(sqlQuery);

        model.addAttribute("data", booksTableData.data);
        model.addAttribute("tableName", booksTableData.tableName);

        return "table";
    }

    @GetMapping("/booksv2/mostexpensive")
    public String sortMostExpensive(Model model) {
        String sqlQuery = "SELECT * FROM books_v2 ORDER BY price DESC LIMIT 1";

        TableData booksv2TableData = populateBooksV2Model(sqlQuery);

        model.addAttribute("value", booksv2TableData.data);
        model.addAttribute("tableName", booksv2TableData.tableName);

        return "table";



    }

    @GetMapping("/books/last")
    public String showLastBook(Model model) {
        String sqlQuery = "SELECT * FROM books ORDER BY book_id DESC LIMIT 1";

        TableData booksTableData = populateBooksModel(sqlQuery);

        model.addAttribute("data", booksTableData.data);
        model.addAttribute("tableName", booksTableData.tableName);

        return "table";
    }

    @GetMapping("/books/first")
    public String showFirstBook(Model model) {
        String sqlQuery = "SELECT * FROM books ORDER BY book_id ASC LIMIT 1";

        TableData booksTableData = populateBooksModel(sqlQuery);

        model.addAttribute("data", booksTableData.data);
        model.addAttribute("tableName", booksTableData.tableName);
      
        return "table";
    }

    @GetMapping("/books/{id}")
    public String showSpecificBook(Model model, @PathVariable String id) {
        String sqlQuery = "SELECT * FROM books WHERE book_id = " + id;

        TableData booksTableData = populateBooksModel(sqlQuery);

        model.addAttribute("data", booksTableData.data);
        model.addAttribute("tableName", booksTableData.tableName);

        return "table";
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("title", "The Hunger Games");
        model.addAttribute("author", "Suzanne Collins");
        model.addAttribute("price", 5.09);
        model.addAttribute("isbn", "9780439023481");
        return "table";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

}
