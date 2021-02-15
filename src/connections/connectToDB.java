package connections;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectToDB {
        //variables used to connect to database
        private static final String DB_URL = "jdbc:mysql://wgudb.ucertify.com/WJ07zyf";
        private static final String driver = "com.mysql.cj.jdbc.Driver";
        private static final String username = "U07zyf";
        private static final String password = "53689179861";
        public static Connection conn;

        //establishes connection to the database
        public static Connection makeConnection() throws ClassNotFoundException, SQLException, Exception{
            Class.forName(driver);
            conn = DriverManager.getConnection(DB_URL, username, password);
            System.out.println("Connection successful.");
            return conn;
        }

        //closes database connection
        public static void closeConnection() throws SQLException{
            conn.close();
            System.out.println("Connection closed.");
        }
    }
