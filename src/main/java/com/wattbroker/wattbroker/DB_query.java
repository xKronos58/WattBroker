package com.wattbroker.wattbroker;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DB_query {
    private String url = "jdbc:sqlite:identifier.sqlite";
    public static void main(String[] args) {
        DB_query dbq = new DB_query();
        System.out.println(dbq.checkCredentials("admin", "admin"));
    }

    public DB_query() {
    }

    public boolean checkCredentials(String username, String password) {
        String sql = "SELECT USERNAME, PASSWORD FROM USERLIST WHERE USERNAME = '" + username + "'";
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
             if (!rs.next())
                return false;
            return rs.getString("PASSWORD").equals(password);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean addUser(String firstName, String lastName, String username, String email, String password, String companyCode, String employeeCode) {
        String sql = "INSERT INTO USERLIST (FIRSTNAME, LASTNAME, USERNAME, EMAIL, PASSWORD, COMPANY_CODE, EMPLOYEE_CODE) VALUES ('" + firstName + "', '" + lastName + "', '" + username + "', '" + email + "', '" + password + "', '" + companyCode + "', '" + employeeCode + "')";
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public String getUsername(int id) {
        String sql = "SELECT USERNAME FROM USERLIST WHERE ID = " + id;
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            return stmt.getResultSet().getString("USERNAME");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public String getEmail(int id) {
        String sql = "SELECT EMAIL FROM USERLIST WHERE ID = " + id;
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            String email = stmt.getResultSet().getString("EMAIL");
            if(Objects.equals(email, "email")) email = "No email provided";
            return email;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public void setUsername(int id, String text) {
        String sql = "UPDATE USERLIST SET USERNAME = '" + text + "' WHERE ID = " + id;
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setPassword(int id, String text) {
        String sql = "UPDATE USERLIST SET PASSWORD = '" + text + "' WHERE ID = " + id;
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setEmail(int id, String text) {
        String sql = "UPDATE USERLIST SET EMAIL = '" + text + "' WHERE ID = " + id;
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getId(String USERNAME) {
        String sql = "SELECT ID FROM USERLIST WHERE USERNAME = \"" + USERNAME + "\"";
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            return stmt.getResultSet().getInt("ID");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }
}
