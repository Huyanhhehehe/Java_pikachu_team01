package controller;

import java.sql.*;

/**
 *
 * @author huyhoang
 */
public class DAO_pikachu {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pikachu";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "matkhau56@";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    
    public static void testConnection() {
        try {
            Connection conn = getConnection();
            System.out.println("✅ Kết nối MySQL THÀNH CÔNG!");
            System.out.println("Database: " + conn.getCatalog());
            conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối MySQL: " + e.getMessage());
        }
    }

    public void saveScore(int finalScore, Timestamp winTimestamp) {
        try {
            Connection conn = getConnection();
            String sql = "INSERT INTO scores (final_score, win_timestamp) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, finalScore);
            pstmt.setTimestamp(2, winTimestamp);
            pstmt.executeUpdate();
            System.out.println("✅ Score saved successfully! Final Score: " + finalScore);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ Error saving score: " + e.getMessage());
        }
    }
}