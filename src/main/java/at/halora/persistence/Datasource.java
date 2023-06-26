package at.halora.persistence;

import at.halora.utils.MessagingServiceType;

import java.io.IOException;
import java.io.InputStream;
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

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("halora.sql");
        assert inputStream != null;

        Scanner scanner = new Scanner(inputStream);
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

    public ResultSet select_user_byName(String name) {
        String sql = "SELECT * FROM users WHERE name = ?";
        try {
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, name);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet select_user_by_accountId(String accountId) {
        String sql = "SELECT * FROM users WHERE user_id = (SELECT user_id FROM user_accounts WHERE account_id = ?)";
        try  {
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, accountId);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet select_user_accounts(int user_id) {
        String sql = "SELECT * FROM user_accounts WHERE user_id = ?";
        try {
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1,user_id);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectMSIds(MessagingServiceType messagingServiceType) {
        String sql = "SELECT * FROM user_accounts WHERE ms_id = (SELECT ms_id FROM messaging_services WHERE name = ?)";
        try {
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, messagingServiceType.getName());
            return pStmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insert_user(String name) {
        String sql = "INSERT INTO users (name, receiveAt) VALUES (?, 1)";
        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, name);
            pStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insert_user_accounts(int user_id, String MessagingService, String account_id) {
        String sql = "INSERT INTO user_accounts (user_id, ms_id, account_id) VALUES (?, " +
                "(SELECT ms_id FROM messaging_services WHERE name = ?), ?)";
        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setInt(1,user_id);
            pStmt.setString(2, MessagingService);
            pStmt.setString(3, account_id);
            pStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update_user_accounts(int user_id, String messagingService, String account_id) {
        String sql = "UPDATE user_accounts SET account_id = ? WHERE user_id = ? AND ms_id = (SELECT ms_id FROM messaging_services WHERE name = ?)";
        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, account_id);
            pStmt.setInt(2, user_id);
            pStmt.setString(3, messagingService);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateReceiveAt(String name, String messagingService) {
        String sql = "UPDATE users SET (receiveAt) = (SELECT ms_id FROM user_accounts\n" +
                "                  WHERE user_id = (SELECT user_id FROM users WHERE name = ?)\n" +
                "                  AND ms_id = (SELECT ms_id FROM messaging_services WHERE name = ?))\n" +
                "            WHERE name = ?;";
        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, name);
            pStmt.setString(2, messagingService);
            pStmt.setString(3, name);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
