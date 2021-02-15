package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import connections.connectToDB;

import java.sql.SQLException;
import java.util.Locale;

public class Main extends Application {

    public static void main(String[] args) throws SQLException, Exception {
        //database connection
        connectToDB.makeConnection();
        //launch
        launch(args);
        Locale.setDefault(new Locale("fr"));
    }
    // start login screen
    /**
     * Connects to database
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/loginform.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        Locale.setDefault(new Locale("fr"));

    }
}
