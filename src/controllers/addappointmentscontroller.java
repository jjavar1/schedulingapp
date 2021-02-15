package controllers;

import connections.connectToDB;
import erdmodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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


public class addappointmentscontroller implements Initializable {
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

    @FXML
    private TableView<customers> appointmentTableView;

    @FXML
    private TableColumn<customers, Integer> columnCustID;

    @FXML
    private TableColumn<customers, String> columnCustName;

    java.sql.Connection conn = connectToDB.conn;
    private ObservableList<customers> customerTable = FXCollections.observableArrayList();
    ObservableList<Integer> allContactIDs = FXCollections.observableArrayList();
    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm:ss");//standard time format
    private final ZoneId localZoneID = ZoneId.systemDefault(); //local id



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

    @FXML
    //main menu
    /**
     * Main menu button action
     * Goes to main menu
     */
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
    //save customer using appointment validation method
    /**
     * Saves customer if aptValidation is true
     */
    @FXML
    void saveCustomerButtonAction(ActionEvent event) throws SQLException {
        if (aptValidation()) {
            saveAppointment();
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
    //updates customer table for name selection and viewing
    /**
     * updates customer table for name selection and viewing
     */
    public void updateCustTable() throws SQLException {
        String sqlStatement = "SELECT Customer_ID, Customer_Name FROM WJ07zyf.customers";
        PreparedStatement ps = conn.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            customers cust = new customers();
            cust.setCustomerName(rs.getString("Customer_Name"));
            cust.setCustomerID(rs.getInt("Customer_ID"));
            customerTable.add(cust);
        }
        appointmentTableView.setItems(customerTable);
    }
    //filling types
    /**
     * Fills types of appointments to combobox
     */
    private void fillTypeList() {
        appointments appointment = new appointments();
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll(appointment.getType());
        typeList.addAll("New Account", "Consultation", "Follow-Up", "Closing Account", "Trial");
        typeComboBox.setItems(typeList);
    }
    //filling locations
    /**
     * Fills locations to combobox
     */
    private void fillLocationList() {
        appointments appointment = new appointments();
        ObservableList<String> locationList = FXCollections.observableArrayList();
        locationList.addAll(appointment.getLocation());
        locationList.addAll("Phoenix", "New York", "Orlando", "Dallas", "London", "Liverpool", "Toronto", "Vancouver", "Montreal");
        locationComboBox.setItems(locationList);
    }
    //getting start times
    /**
     * Gets start times. Begins at 8AM and ends at 10PM. End times are set 30 minutes after start times.
     */
    public void getStartTime() {
        //starting at 8:00AM
        LocalTime time = LocalTime.of(8, 0, 0);
        do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            time = time.plusMinutes(30);
        } while (!time.equals(LocalTime.of(17, 30, 0))); //from 8:00AM to 10:00PM
        startTimes.remove(startTimes.size() - 1);
        endTimes.remove(0);

        startComboBox.setItems(startTimes);
        endComboBox.setItems(endTimes);
        //setting comboboxes for every 30 minutes
        startComboBox.getSelectionModel().select(LocalTime.of(8, 0, 0).format(timeDTF));
        endComboBox.getSelectionModel().select(LocalTime.of(8, 30, 0).format(timeDTF));
    }
    //getting customer ID's
    /**
     * Gets customer IDs from database using SQL query
     */
    public void getCustomers() throws SQLException, Exception {
        String sqlStatement = "SELECT * FROM WJ07zyf.customers";
        ObservableList<Integer> allCustomerIDs = FXCollections.observableArrayList();

        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        pst.execute();
        ResultSet rs = pst.executeQuery(sqlStatement);

        while (rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            allCustomerIDs.add(customerId);
        }
        customerIDComboBox.setItems(allCustomerIDs);
    }
    //getting users
    /**
     * Gets user IDs from database using SQL query
     */
    public void getUsers() throws SQLException, Exception {
        ObservableList<Integer> allUsers = FXCollections.observableArrayList();
        String sqlStatement = "SELECT User_ID FROM WJ07zyf.users";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        pst.execute();
        ResultSet rs = pst.executeQuery(sqlStatement);

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            allUsers.add(userId);
        }
        userIDComboBox.setItems(allUsers);
    }
    //getting contact IDs
    /**
     * Gets contact IDs from database using SQL query
     */
    public void getContactIDs() throws SQLException {
        String sqlStatement = "SELECT Contact_ID FROM WJ07zyf.contacts";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        ResultSet rs = pst.executeQuery(sqlStatement);
        while (rs.next()) {
            contacts contact = new contacts();
            contact.setContactID(rs.getInt("Contact_ID"));
            allContactIDs.add(contact.getContactID());
            contactComboBox.setItems(allContactIDs);
        }
        pst.close();
        rs.close();
    }
    //customer name text
    private void showCustomerName(customers newValue) {
        customers selectCust = new customers();
        customerNameTextField.setText(newValue.getCustomerName());
        selectCust = newValue;
    }
    //appointment validation method
    /**
     * Validates appointments with if statements
     */
    public boolean aptValidation() throws SQLException {

        customers customer = appointmentTableView.getSelectionModel().getSelectedItem();
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String type = typeComboBox.getValue();
        String contact = contactComboBox.getValue().toString();
        String location = locationComboBox.getValue();
        String userID = userIDComboBox.getValue().toString();
        String customerID = customerIDComboBox.getValue().toString();
        LocalDate localDate = datePickerBox.getValue();
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeDTF); //parsing starttime to localtime
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeDTF); //parsing endtime to localtime
        //being time conversions
        LocalDateTime startDT = LocalDateTime.of(localDate, startTime); //converting to localdatetime
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime); //converting to localdatetime
        ZonedDateTime startUTC = startDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC")); //converting to zoneddatetime
        ZonedDateTime endUTC = endDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC")); //converting to zoneddatetime

        String errorMessage = ""; //using empty string to make things easier and able to make multiple error strings in one error box.
        if (customer == null) {
            errorMessage += "Please select a customer.\n";
        }
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
            errorMessage += "You must select a contact.\n";
        }
        if (location == null || location.length() == 0) {
            errorMessage += "You must select a location.\n";
        }
        if (startDT == null) {
            errorMessage += "You must select a Start time\n";
        }
        if (endDT == null) {
            errorMessage += "You must select an End time.\n";
        }
        if (userID == null) {
            errorMessage += "You must select a User ID\n";
        }
        if (customerID == null) {
            errorMessage += "You must select a Customer ID\n";
        }
        if (localDate == null) {
            errorMessage += "You must select a date\n";
        } if (endUTC.equals(startUTC) || endUTC.isBefore(startUTC)) {
            errorMessage += "End time must be after start time\n";
        }
        if (appointmentConflict(startUTC, endUTC)) {
            errorMessage += "Appointment times conflict with other appointments.\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Appointment");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
    /**
     * Checks for appointment conflicts
     * @param startzone timestamp where the appointment started
     * @param endzone timestamp where the appointment ended
     */
    private boolean appointmentConflict(ZonedDateTime startzone, ZonedDateTime endzone) throws SQLException {
        try {
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
    //saves appointments to database
    /**
     * Saves appointment to database
     */
    public void saveAppointment() throws SQLException {
        LocalDate localDate = datePickerBox.getValue();
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem().toString(), timeDTF);
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem().toString(), timeDTF);
        //time conversions
        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);
        System.out.println("localStartDT: " + startDT);
        System.out.println("localEndDT: " + endDT);
        ZonedDateTime startUTC = startDT.atZone(localZoneID);
        ZonedDateTime endUTC = endDT.atZone(localZoneID);
        System.out.println("startUTC: " + startUTC);
        System.out.println("endUTC: " + endUTC);
        Timestamp startTS = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp endTS = Timestamp.valueOf(endUTC.toLocalDateTime());
        System.out.println("sqlStartTime: " + startTS);
        System.out.println("sqlEndTime: " + endTS);

        String sqlStatement = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sqlStatement);

            ps.setString(1, titleTextField.getText());
            ps.setString(2, descriptionTextField.getText());
            ps.setString(3, locationComboBox.getSelectionModel().getSelectedItem());
            ps.setString(4, typeComboBox.getSelectionModel().getSelectedItem());
            ps.setTimestamp(5, startTS);
            ps.setTimestamp(6, endTS);
            ps.setInt(7, customerIDComboBox.getSelectionModel().getSelectedItem());
            ps.setInt(8, userIDComboBox.getSelectionModel().getSelectedItem());
            ps.setInt(9, contactComboBox.getSelectionModel().getSelectedItem());
            ps.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText("Appointment added");
        alert.showAndWait();
    }
    /**
     * Validates appointments with if statements
     * @param resourceBundle
     * @param url
     *
     * Lambda listener to show customer name from the appointment table view
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setting table value factory
        PropertyValueFactory<customers, Integer> customerIDFactory = new PropertyValueFactory<>("CustomerID");
        PropertyValueFactory<customers, String> customerNameFactory = new PropertyValueFactory<>("CustomerName");

        columnCustID.setCellValueFactory(customerIDFactory);
        columnCustName.setCellValueFactory(customerNameFactory);

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("A customer must be physically selected from the table for the appointment to be added."); //alert for graders
            alert.showAndWait();
            updateCustTable();
            fillTypeList();
            fillLocationList();
            getContactIDs();
            getCustomers();
            getUsers();
            getStartTime();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //customer name listener, just makes it easier and nicer

        appointmentTableView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showCustomerName(newValue));

        datePickerBox.setDayCellFactory(datepick -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean none) {
                super.updateItem(date, none);
                setDisable(none || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY ); //sets day of week disabled if equals weekends
            }
        });
        }

    }

