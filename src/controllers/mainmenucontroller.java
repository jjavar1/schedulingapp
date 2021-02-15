package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class mainmenucontroller {
    @FXML
    private Button addCustomerButton;

    @FXML
    private Button calendarButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label addCustomerLabel;

    @FXML
    private Label calendarLabel;

    @FXML
    private Label reportsLabel;

    @FXML
    private Label exitLabel;

    //the main menu is where the user can select any action they need to do
    //add customer
    /**
     * Goes to add customer page
     * @param event when button is clicked event happens
     */
    @FXML
    void addCustomerButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/customersmenu.fxml"));
        Stage stages = new Stage();
        stages.setTitle("Customer menu");
        stages.setScene(new Scene(root));
        stages.setResizable(false);
        stages.show();
        Stage stage = (Stage) addCustomerButton.getScene().getWindow();
        stage.close();
    }
    //appointments calender
    /**
     * Goes to appointments page
     * @param event when button is clicked event happens
     */
    @FXML
    void calendarButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/appointmentsmenu.fxml"));
        Stage stages = new Stage();
        stages.setTitle("Appointments menu");
        stages.setScene(new Scene(root));
        stages.setResizable(false);
        stages.show();
        Stage stage = (Stage) calendarButton.getScene().getWindow();
        stage.close();
    }
    //exit button
    /**
     * Exits program
     * @param event when button is clicked event happens
     */
    @FXML
    void exitButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you would like to exit the application?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        } else {
            return;
        }
    }
    //report button
    /**
     * Goes to reports page
     * @param event when button is clicked event happens
     */
    @FXML
    void reportsButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/reportsmenu.fxml"));
        Stage stages = new Stage();
        stages.setTitle("Reports menu");
        stages.setScene(new Scene(root));
        stages.setResizable(false);
        stages.show();
        Stage stage = (Stage) reportsButton.getScene().getWindow();
        stage.close();
    }
}
