package com.createment.springone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    final String DB_HOST = "jdbc:mysql://localhost:3306/bookstore";
    final String DB_USER = "bookstoreuser";
    final String DB_PASS = "bookstorepassword";

    private ResultSet runQuery(String query) {
        ResultSet rs;
        try {
            Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASS);
            Statement stmt = conn.createStatement();
            chicken();
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
            chicken();

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

    @GetMapping("/books")
    public List<Map<String, String>> retrieveBooks() {
        String sqlQuery = "SELECT * FROM books";

        TableData booksTableData = populateBooksModel(sqlQuery);

        return booksTableData.data.values().stream().toList();
    }
    @GetMapping("/books/dabdab")
    public List<Map<String, String>> retrieveBooks2() {
        String sqlQuery = "SELECT * FROM books";

        TableData booksTableData = populateBooksModel(sqlQuery);
        System.out.println("PIG");

        return booksTableData.data.values().stream().toList();
    }

    public void chicken(){
        System.out.println("Can i have one chicken sanwich please.");
    }


    //  here is a bunch of comments cus i have absolutely no idea what to do....
    //someone... HELLLPPPUHHH MEEEE!
    //PUHHHLEASSAAAAHHHH!!




    @PostMapping("/books")
    public ResponseEntity<String> setSingleBook(@RequestBody Map<String, String> book) {
        List<String> headings = new ArrayList<>();
        headings = book.keySet().stream().toList();

        List<String> values = new ArrayList<>();
        values = book.values().stream().toList();

        String sqlQuery = "INSERT INTO books (";
        for(int x = 0; x < headings.size(); x++) {
            sqlQuery += headings.get(x) + ", ";
            chicken();
        }
        sqlQuery += ") VALUES (";

        for(int y = 0; y < values.size(); y++) {
            sqlQuery += values.get(y) + ", ";
        }

        sqlQuery += ")";

        try{
            Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASS);
            PreparedStatement prep = conn.prepareStatement(
                    "insert into books (title, author, genre, price, year, isbn) values(?, ?, ?, ?, ?, ?);");
            prep.setString(1, values.get(0));
            prep.setString(2, values.get(1));
            prep.setString(3, values.get(2));
            prep.setDouble(4, Double.valueOf(values.get(3)));
            prep.setInt(5, Integer.valueOf(values.get(4)));
            prep.setString(6, values.get(5));
            chicken();

            prep.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        System.out.println("some result: ");

        return new ResponseEntity<>(
                "Cheesecake is the best",
                HttpStatus.CREATED);
    }
}
