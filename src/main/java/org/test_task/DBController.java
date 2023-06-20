package org.test_task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBController {
    private static Connection connection;
    private static Statement statement;

    public static Connection createConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/my_db";
        String userName = "postgres";
        String password = "admin";
        connection = DriverManager.getConnection(url, userName, password);

        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public static Statement createStatement() throws SQLException {
        statement = connection.createStatement();
        return statement;
    }

    public static void closeStatement() throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }

    public static void createTableEquations() throws SQLException {
        statement.executeUpdate("create table if not exists equations(id serial primary key, value varchar(200))");
    }

    public static void createTableEquationRoots() throws SQLException {
        statement.executeUpdate("create table if not exists equations_roots(id serial primary key, equation_id int, value double precision)");
    }

    public static void insertIntoTableEquations(String equation) throws SQLException {
        statement.executeUpdate("insert into equations (value) values ('" + equation + "')");
    }

    public static void insertIntoTableEquationRoots(int equationId, double root) throws SQLException {
        statement.executeUpdate("insert into equations_roots (equation_id, value) values ('" + equationId + "', '" + root + "')");
    }

    public static int getEquationIdByValue(String value) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery("select id from equations where value = '" + value + "'")) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return 0;
            }
        }
    }

    public static List<String> getEquationsByRoot(double root) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery("select equations.value from equations join equations_roots on equations.id = equations_roots.equation_id where equations_roots.value = '" + root + "'")) {
            List<String> equationList = new ArrayList<>();
            while (resultSet.next()) {
                equationList.add(resultSet.getString("value"));
            }
            return equationList;
        }
    }

    public static List<String> getRootsByEquation(String equation) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery("select equations_roots.value from equations_roots join equations on equations_roots.equation_id = equations.id where equations.value = '" + equation + "'")) {
            List<String> equationList = new ArrayList<>();
            while (resultSet.next()) {
                equationList.add(resultSet.getString("value"));
            }
            return equationList;
        }
    }

    public static List<String> getEquationsAndRoots() throws SQLException {
        try (ResultSet resultSet = statement.executeQuery("select equations.value, equations_roots.value from equations left join equations_roots on equations.id = equations_roots.equation_id")) {
            List<String> equationList = new ArrayList<>();
            while (resultSet.next()) {
                String equation = resultSet.getString(1);
                String root = resultSet.getString(2) == null ? "" : resultSet.getString(2);
                equationList.add(equation + ": " + root);
            }
            return equationList;
        }
    }

    public static void deleteTablesEquationsAndRoots() throws SQLException {
        statement.executeUpdate("truncate table equations_roots");
        statement.executeUpdate("truncate table equations");
    }
}
