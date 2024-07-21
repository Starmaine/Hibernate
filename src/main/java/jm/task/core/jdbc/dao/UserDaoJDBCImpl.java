package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {
    private String query;
    Connection connection;

    {
        try {
            connection = Util.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        query = "CREATE TABLE IF NOT EXISTS Users (" +
                "id BIGINT(11) NOT NULL AUTO_INCREMENT" +
                ", name VARCHAR(20)" +
                ", lastname VARCHAR (20)" +
                ", age TINYINT(100)" +
                ", PRIMARY KEY(id))";
        try (Statement table = connection.createStatement()) {
            table.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        query = "DROP TABLE IF EXISTS Users";
        try (Statement drop = connection.createStatement()) {
            drop.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        query = "INSERT INTO Users (name, lastname, age) VALUES (?, ?, ?)";
        try (PreparedStatement saveUs = connection.prepareStatement(query)) {
            saveUs.setString(1, name);
            saveUs.setString(2, lastName);
            saveUs.setByte(3, age);
            saveUs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        query = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement remove = connection.prepareStatement(query)) {
            remove.setLong(1, id);
            remove.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        query = "SELECT id, name, lastname, age FROM Users";

        try (PreparedStatement getAll = connection.prepareStatement(query);
             ResultSet resultSet = getAll.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastname");
                byte age = resultSet.getByte("age");

                User user = new User(name, lastname, age);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {
        query = "TRUNCATE TABLE Users";

        try (PreparedStatement clean = connection.prepareStatement(query)) {
            clean.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
