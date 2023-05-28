package at.halora.persistence;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.Scanner;


public class Datasource {
    private String url;
    private String wd;
    private Connection conn;


    //check if database exists, connect and/or create the database
    public Datasource() {
        this.wd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        this.url = "jdbc:sqlite:" + wd + "/halora.db";
        try {
            if (Files.exists(Path.of(wd + "/halora.db"))) {
                conn = DriverManager.getConnection(url);
                return;
            }
            init();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //loads halora.sql script and creates the database
    private void init() throws IOException {

        Scanner scanner = new Scanner(Path.of((wd + "/src/main/java/at/halora/persistence/halora.sql")));
        scanner.useDelimiter(";");

        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            if (conn != null) {
                System.out.println("Datasource connected.");
                while (scanner.hasNext()) {
                    stmt.execute(scanner.next() + ";");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet select_user_byName(String name) throws SQLException {
        String sql = "SELECT * FROM users WHERE name = ?";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, name);
        return pStmt.executeQuery();
    }

    public ResultSet select_user_byId(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE name = ?";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, id);
        return pStmt.executeQuery();
    }

    public ResultSet select_user_accounts(int user_id) throws SQLException {
        String sql = "SELECT * FROM user_accounts WHERE user_id = ?";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1,user_id);
        return pStmt.executeQuery();
    }
    public void insert_user(String name) throws SQLException {
        String sql = "INSERT INTO users (name) VALUES (?)";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, name);
        pStmt.execute();
    }

    public void insert_user_accounts(int user_id, String MessagingService, String account_id) throws SQLException {
        String sql = "INSERT INTO user_accounts (user_id, ms_id, account_id) VALUES (?, " +
                "(SELECT ms_id FROM messaging_services WHERE name = ?), ?)";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1,user_id);
        pStmt.setString(2, MessagingService);
        pStmt.setString(3, account_id);
        pStmt.execute();
    }

    public void updateReceiveAt(String name, String messagingService) throws SQLException {
        String sql = "UPDATE users SET (receiveAt) = (SELECT ms_id FROM user_accounts\n" +
                "                  WHERE user_id = (SELECT user_id FROM users WHERE name = ?)\n" +
                "                  AND ms_id = (SELECT ms_id FROM messaging_services WHERE name = ?))\n" +
                "            WHERE name = ?;";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, name);
        pStmt.setString(2, messagingService);
        pStmt.setString(3, name);
        pStmt.executeUpdate();
    }

}
