package org.smart4j.chapter1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter1.helper.DatabaseHelper;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.util.PropsUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties properties = PropsUtil.loadProps("config.properties");
        DRIVER = properties.getProperty("jdbc.driver");
        URL = properties.getProperty("jdbc.url");
        USERNAME = properties.getProperty("jdbc.username");
        PASSWORD = properties.getProperty("jdbc.password");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("can not load jdbc driver",e);
        }
    }


    public List<Customer> getCustomerList(String keyword) {
        Connection connection = null;
        List<Customer> customers = new ArrayList<>();
        String sql = "select * from customer";
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContact(rs.getString("contact"));
                customer.setEmail(rs.getString("email"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setRemark(rs.getString("remark"));
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            logger.error("execute sql error",e);
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("conn close error",e);
                }
            }
        }
        return null;
    }

    public Customer getCustomer(long id) {
        return DatabaseHelper.queryEntity(Customer.class,"select * from customer where id = ?",id);
    }

    public boolean createCustomer(Map<String,Object> filedMap) {
        return DatabaseHelper.insertEntity(Customer.class, filedMap);
    }

    public boolean updateCustomer(long id, Map<String,Object> filedMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, filedMap);
    }

    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
