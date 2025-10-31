import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/emss?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";            // change if different
    private static final String PASSWORD = "abi@10102006"; // change this!

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found. Add the connector jar to classpath.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
