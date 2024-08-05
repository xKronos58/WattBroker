package com.wattbroker.wattbroker;

import java.sql.DriverManager;
import java.sql.SQLException;
public class DB_query {
    // TODO : Create hashing algorithm to enhance security.

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
}
