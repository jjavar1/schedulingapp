package erdmodel;

import java.time.LocalDateTime;

public class users {
    private static int userID;
    private static String username;
    private static String password;

    public users(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public users() {
    }

    public users(int userId, String userName, String password, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
    }

    public users(int userID) {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        users.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public  void setUsername(String username) {
        users.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        users.password = password;
    }
}
