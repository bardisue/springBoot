package org.prgrms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record JdbcCustomerRepository() {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    private static final String SELECT_BY_NAME_SQL = "select * from customers Where name = ?";//" and email = ?";
    private static final String SELECT_ALL_SQL = "select * from customers";
    private static final String INSERT_SQL = "INSERT INTO customers(customer_id, name, email) VALUES (UUID_TO_BIN(?), ?, ?)";
    private static final String UPDATE_BY_ID_SQL = "UPDATE customers SET name = ? WHERE customer_id = UUID_TO_BIN(?)";
    private static final String DELETE_ALL_SQL = "DELETE FROM customers";
    public List<String> findNames(String name){
        List<String> names = new ArrayList<>();

        try(
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                var statement = connection.prepareStatement(SELECT_BY_NAME_SQL);
        ){
            statement.setString(1, name);
            logger.info("statement -> {}", statement);
            try(var resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    var customerName = resultSet.getString("name");
                    var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                    logger.info("customer id -> {}, name -> {}, createdAt -> {}", customerId , name, createdAt);
                    names.add(customerName);
                }
            }
        } catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }

        return names;
    }

    public List<String> findALLName(){
        List<String> names = new ArrayList<>();

        try(
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
                var resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                var customerName = resultSet.getString("name");
                var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                logger.info("customer id -> {}, name -> {}, createdAt -> {}", customerId , customerName, createdAt);
                names.add(customerName);
            }
        } catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }

        return names;
    }


    public int insertCustomer(UUID customerId, String name, String email){
        try(
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                var statement = connection.prepareStatement(INSERT_SQL);
        ){
            statement.setBytes(1, customerId.toString().getBytes());
            statement.setString(2, name);
            statement.setString(3, email);
            return statement.executeUpdate();
        } catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return 0;
    }
    public int updateCustomerName(UUID customerId, String name){
        try(
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                var statement = connection.prepareStatement(UPDATE_BY_ID_SQL);
        ){
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());
            return statement.executeUpdate();
        } catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return 0;
    }


    public int deleteAllCustomers(){
        try(
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                var statement = connection.prepareStatement(DELETE_ALL_SQL);
        ){
            return statement.executeUpdate();
        } catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return 0;
    }


    public static void main(String[] args){
        var customerRepository = new JdbcCustomerRepository();

        var count = customerRepository.deleteAllCustomers();
        logger.info("deleted count{}", count);

        customerRepository.insertCustomer(UUID.randomUUID(), "new-user", "new-user@gamil.com");
        var customer2 = UUID.randomUUID();
        customerRepository.insertCustomer(customer2, "new-user2", "new-user2@gamil.com");

        customerRepository.findALLName().forEach(v -> logger.info("Found name : {}", v));

        customerRepository.updateCustomerName(customer2, "updated-user2");
        
        customerRepository.findALLName().forEach(v -> logger.info("Found name : {}", v));
    }
}