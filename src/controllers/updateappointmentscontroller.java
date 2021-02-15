package controllers;


import connections.connectToDB;
import erdmodel.appointments;
import erdmodel.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;


public class updateappointmentscontroller implements Initializable {
    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<Integer> contactComboBox;

    @FXML
    private ComboBox<String> locationComboBox;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private DatePicker datePickerBox;

    @FXML
    private ComboBox<String> startComboBox;

    @FXML
    private ComboBox<String> endComboBox;

    @FXML
    private Button saveCustomerButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private TextField autogenapptID;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private ComboBox<Integer> userIDComboBox;

    @FXML
    private ComboBox<Integer> customerIDComboBox;


    java.sql.Connection conn = connectToDB.conn;
    private appointments appointmentsUp;
    ObservableList<Integer> allContactIDs = FXCollections.observableArrayList();
    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm:ss");//standard time format
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//format with date
    private final ZoneId localZoneID = ZoneId.systemDefault(); //local zone id



    @FXML
    void autogenapptIDAction(ActionEvent event) {

    }

    @FXML
    void customerNameTextFieldAction(ActionEvent event) {

    }

    @FXML
    void contactComboBoxAction(ActionEvent event) {

    }

    @FXML
    void customerIDComboBoxAction(ActionEvent event) {

    }

    @FXML
    void datePickerBoxAction(ActionEvent event) {

    }

    @FXML
    void descriptionTextFieldAction(ActionEvent event) {

    }

    @FXML
    void endComboBoxAction(ActionEvent event) {

    }

    @FXML
    void locationComboBoxAction(ActionEvent event) {

    }
    /**
     * Main menu button, goes to appointmentsmenu
     * @param event when button is clicked event happens
     */
    //main menu button
    @FXML
    void mainMenuButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you would like to go back?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("../views/appointmentsmenu.fxml"));
            Stage stage = (Stage) mainMenuButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main menu");
            stage.setResizable(false);
            stage.show();
        } else {
            return;
        }
    }
    /**
     * Saves customer if appointmentbalidation boolean is true
     * @param event when button is clicked event happens
     */
    //save appointment button
    @FXML
    void saveCustomerButtonAction(ActionEvent event) throws Exception {
        //if the appointment is valid, update the appointment with SQL method
        if (aptValidation()) {
            //update appointment
            updateAppointmentSQL();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success!");
            alert.setHeaderText(null);
            alert.setContentText("Appointment added");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Unexpected error");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    @FXML
    void startComboBoxAction(ActionEvent event) {

    }

    @FXML
    void titleTextFieldAction(ActionEvent event) {

    }

    @FXML
    void typeComboBoxAction(ActionEvent event) {

    }

    @FXML
    void userIDComboBoxAction(ActionEvent event) {

    }
    /**
     * Fills types list to combobox using SQL select statement
     */
    //filling types to combobox
    private void fillTypeList() {
        appointments appointment = new appointments();
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll(appointment.getType());
        typeList.addAll("New Account", "Consultation", "Follow-Up", "Closing Account", "Trial");
        typeComboBox.setItems(typeList);
    }
    /**
     * Fills locations list to combobox using SQL select statement
     */
    //filling locations to combobox
    private void fillLocationList() {
        appointments appointment = new appointments();
        ObservableList<String> locationList = FXCollections.observableArrayList();
        locationList.addAll(appointment.getLocation());
        locationList.addAll("Phoenix", "New York", "Orlando", "Dallas", "London", "Liverpool", "Toronto", "Vancouver", "Montreal");
        locationComboBox.setItems(locationList);
    }
    /**
     * Gets start times and end times. Starts at 8AM and ends at 10PM, every 30 minutes
     */
    //getting start times and setting them
    public void getStartTime() {
        //from 8am
        LocalTime time = LocalTime.of(8, 0, 0);
        do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            //every 30 minutes
            time = time.plusMinutes(30);
            //to 10:30
        } while (!time.equals(LocalTime.of(17, 30, 0)));
        startTimes.remove(startTimes.size() - 1);
        endTimes.remove(0);

        startComboBox.setItems(startTimes);
        endComboBox.setItems(endTimes);
    }
    /**
     * Gets customer IDs using SQL Select Statement
     */
    //getting customer IDs
    public void getCustomers() throws SQLException, Exception {
        //sql statement to get customers
        String sqlStatement = "SELECT * FROM WJ07zyf.customers";
        ObservableList<Integer> allCustomerIDs = FXCollections.observableArrayList();

        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        pst.execute();
        ResultSet rs = pst.executeQuery(sqlStatement);

        while (rs.next()) {
            //setting customer ids
            int customerId = rs.getInt("Customer_ID");
            allCustomerIDs.add(customerId);
        }
        //putting them in the combobox
        customerIDComboBox.setItems(allCustomerIDs);
    }
    //getting userIds
    public void getUsers() throws SQLException, Exception {
        ObservableList<Integer> allUsers = FXCollections.observableArrayList();
        //getting userIds from database
        String sqlStatement = "SELECT User_ID FROM WJ07zyf.users";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        pst.execute();
        ResultSet rs = pst.executeQuery(sqlStatement);

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            allUsers.add(userId);
        }
        //setting userids to combobox
        userIDComboBox.setItems(allUsers);
    }
    //getting contact ids
    public void getContactIDs() throws SQLException {
        //getting contactids from database
        String sqlStatement = "SELECT Contact_ID FROM WJ07zyf.contacts";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        ResultSet rs = pst.executeQuery(sqlStatement);
        while (rs.next()) {
            contacts contact = new contacts();
            contact.setContactID(rs.getInt("Contact_ID"));
            //setting contactids to combobox
            allContactIDs.add(contact.getContactID());
            contactComboBox.setItems(allContactIDs);
        }
        pst.close();
        rs.close();
    }

    /**
     * Sets appointment boxes from the selectionmodel from the main appointments page.
     */
    //PUT THIS IN UPDATE
    public void setUpdateAppointments() throws SQLException {
        //setting appointments for update from selection model from last page
        String startLocal = appointmentsUp.getStart();
        String endLocal = appointmentsUp.getEnd();
        LocalDateTime localDateTimeStart = LocalDateTime.parse(startLocal, datetimeDTF);
        LocalDateTime localDateTimeEnd = LocalDateTime.parse(endLocal, datetimeDTF);
        LocalDate localDate = localDateTimeStart.toLocalDate();
        customerNameTextField.setText("Name not needed for update");
        customerIDComboBox.setValue(appointmentsUp.getCustomerID());
        titleTextField.setText(appointmentsUp.getTitle());
        descriptionTextField.setText(appointmentsUp.getDescription());
        typeComboBox.getSelectionModel().select(appointmentsUp.getType());
        contactComboBox.setValue(appointmentsUp.getContactID());
        locationComboBox.setValue(appointmentsUp.getLocation());
        userIDComboBox.setPromptText(String.valueOf(appointmentsUp.getUserID()));
        autogenapptID.setText(String.valueOf(appointmentsUp.getAppointmentID()));
        datePickerBox.setValue(localDate);
        startComboBox.setPromptText(localDateTimeStart.toLocalTime().format(timeDTF));
        endComboBox.setPromptText(localDateTimeEnd.toLocalTime().format(timeDTF));

    }
    /**
     * Validates appointment using if statements
     */
    //appointment validation
    public boolean aptValidation() throws SQLException {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String type = typeComboBox.getValue();
        String contact = contactComboBox.getValue().toString();
        String location = locationComboBox.getValue();
        String userID = userIDComboBox.getValue().toString();
        String customerID = customerIDComboBox.getValue().toString();
        LocalDate localDate = datePickerBox.getValue(); //getting localdate
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeDTF); //parsing a string and timDTF format
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeDTF); //parsing a string and timDTF format

        LocalDateTime startDT = LocalDateTime.of(localDate, startTime); //converting to localdatetime
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime); //converting to localdatetime
        ZonedDateTime startUTC = startDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC")); //converting to utc
        ZonedDateTime endUTC = endDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC")); //converting to utc

        String errorMessage = "";
        if (title == null || title.length() == 0) {
            errorMessage += "You must enter a title.\n";
        }
        if (description == null || description.length() == 0) {
            errorMessage += "You must enter a description.\n";
        }
        if (type == null || type.length() == 0) {
            errorMessage += "You must select a type.\n";
        }
        if (contact == null || contact.length() == 0) {
            errorMessage += "You must select a contact number.\n";
        }
        if (location == null || location.length() == 0) {
            errorMessage += "You must select a location.\n";
        }
        if (startDT == null) {
            errorMessage += "You must select a Start time";
        }
        if (endDT == null) {
            errorMessage += "You must select an End time.\n";
        }
        if (userID == null) {
            errorMessage += "You must select a User ID";
        }
        if (customerID == null) {
            errorMessage += "You must select a Customer ID";
        } else if (endUTC.equals(startUTC) || endUTC.isBefore(startUTC)) { //checking if end time is after start time
        errorMessage += "End time must be after Start time.\n";
        } else {
            try {
                if (appointmentConflict(startUTC, endUTC)) {
                    errorMessage += "Appointment times conflict with Consultant's other appointments.\n"; //checking if there are any conflicts in the database
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Appointment");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return false;
        }
    }
    /**
     * Checks for conflicts using timestamps and SQL select statement
     */
    //appointment conflict checker
    private boolean appointmentConflict(ZonedDateTime startzone, ZonedDateTime endzone) throws SQLException {
        try {
            //sql statement to check for inconsistencies
            String sqlStatement = "SELECT * FROM WJ07zyf.appointments WHERE (? BETWEEN Start AND End OR ? BETWEEN Start AND End OR ? < Start AND ? > End)";
            PreparedStatement ps = conn.prepareStatement(sqlStatement);

                ps.setTimestamp(1, Timestamp.valueOf(startzone.toLocalDateTime()));
                ps.setTimestamp(2, Timestamp.valueOf(endzone.toLocalDateTime()));
                ps.setTimestamp(3, Timestamp.valueOf(startzone.toLocalDateTime()));
                ps.setTimestamp(4, Timestamp.valueOf(endzone.toLocalDateTime()));
                ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                    return true;
                }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    /**
     * Updates appointments to database
     */
    //update appointments
    public void updateAppointmentSQL () throws SQLException, Exception {
        LocalDate localDate = datePickerBox.getValue();
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeDTF);

        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);
        ZonedDateTime startUTC = startDT.atZone(localZoneID);
        ZonedDateTime endUTC = endDT.atZone(localZoneID);
        //converting to timestamp for sql database
        Timestamp sqlStartTS = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp sqlEndTS = Timestamp.valueOf(endUTC.toLocalDateTime());
        try {
            //sql query to update appointments
            String sqlStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?, Created_By = ?, Last_Updated_By = ?, Create_Date = CURRENT_TIMESTAMP, Last_Update = CURRENT_TIMESTAMP WHERE Appointment_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sqlStatement);

            ps.setString(1, titleTextField.getText());
            ps.setString(2, descriptionTextField.getText());
            ps.setString(3, locationComboBox.getSelectionModel().getSelectedItem());
            ps.setString(4, typeComboBox.getSelectionModel().getSelectedItem());
            ps.setTimestamp(5, sqlStartTS);
            ps.setTimestamp(6, sqlEndTS);
            ps.setInt(7, customerIDComboBox.getSelectionModel().getSelectedItem());
            ps.setInt(8, userIDComboBox.getSelectionModel().getSelectedItem());
            ps.setInt(9, contactComboBox.getSelectionModel().getSelectedItem());
            ps.setInt(10, userIDComboBox.getSelectionModel().getSelectedItem());
            ps.setInt(11, userIDComboBox.getSelectionModel().getSelectedItem());
            ps.setInt(12, Integer.parseInt(autogenapptID.getText()));
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
    /** LAMBDA
     * Lambda to filter weekends out of datepicker
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setting all the appropriate items
        try {
            appointmentsUp = appointmentsmenucontroller.getAppointmentUpdates(); //for filling boxes
            setUpdateAppointments();
            getContactIDs();
            getCustomers();
            getUsers();
            getStartTime();
            fillLocationList();
            fillTypeList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //lambda to set each weekend to disabled on the datepicker

        datePickerBox.setDayCellFactory(datepick -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean none) {
                super.updateItem(date, none);
                setDisable(none || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY );
            }
        });
        }
    }

