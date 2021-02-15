package controllers;

import connections.connectToDB;
import erdmodel.appointments;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class appointmentsmenucontroller implements Initializable {
    @FXML
    private TableView<appointments> appointmentTableView;

    @FXML
    private TableColumn<appointments, String> appointmentIDColumn;

    @FXML
    private TableColumn<appointments, String> titleColumn;

    @FXML
    private TableColumn<appointments, String> descriptionColumn;

    @FXML
    private TableColumn<appointments, String> locationColumn;

    @FXML
    private TableColumn<appointments, String> contactColumn;

    @FXML
    private TableColumn<appointments, String> typeColumn;

    @FXML
    private TableColumn<appointments, String> startColumn;

    @FXML
    private TableColumn<appointments, String> endColumn;

    @FXML
    private TableColumn<appointments, String> customerIDColumn;

    @FXML
    private Button addMenuButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button filterByMonthButton;

    @FXML
    private Button filterByWeekButton;


    java.sql.Connection conn = connectToDB.conn;
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //format the date time
    ObservableList<appointments> appointmentsContainer = FXCollections.observableArrayList();
    private static int updateAppointmentIndex; //for updates page
    private static appointments appointmentUp = new appointments(); // for updates page
    LocalDateTime utcStartDTTwo;
    LocalDateTime utcEndDTTwo;
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    private final ZoneId localZoneID = ZoneId.systemDefault();

    //menu button
    /**
     * add menu button, pops menu window up
     * @param event when the button is clicked event happens
     */
    @FXML
    void addMenuButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/addappointmentsmenu.fxml"));
        Stage stages = new Stage();
        stages.setTitle("Appointments menu");
        stages.setScene(new Scene(root));
        stages.setResizable(false);
        stages.show();
        Stage stage = (Stage) addMenuButton.getScene().getWindow();
        stage.close();
    }
    //back button
    /**
     * back button goes back to main menu
     * @param event when the button is clicked event happens
     */
    @FXML
    void backButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you would like to go back?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("../views/mainmenu.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main menu");
            stage.setResizable(false);
            stage.show();
        } else {
            return;
        }
    }
    //delete button
    /**
     * Deleted selected appointment from appointment table using appointment ID
     */
    @FXML
    void deleteButtonAction(ActionEvent event) throws Exception {
        //getting selected item
        if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            //setting selected item to appointments
            appointments appt = appointmentTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Required");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE appointment ID " + appt.getAppointmentID() + " ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                //deleting appointment where appointment id is the same as in the database
                deleteAppointments(appt.getAppointmentID());
                ObservableList<appointments> filteredApts = FXCollections.observableArrayList();
                for (appointments a : getAllAppointments()) {
                    filteredApts.add(a);
                }
                appointmentTableView.setItems(filteredApts); //setting table again(reset)
            } else {
                return;
            }
        } else {
            //confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Required");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to delete");
        }
    }
    //delete appointments method
    /**
     * delete appointments sql query
     * @param appointment Used for appointment ID
     */
    void deleteAppointments(int appointment) throws Exception {
        try {
            //sql query to delete appointments based off appointment id
            String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sqlStatement);
            ps.setInt(1, appointment);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
        setAppointmentsTable();
    }
    /**
     * Start of month and week filters
     * @param event triggers when the button is pressed
     * @exception Exception
     * @return No return value.
     */
    //resetting the filter
    /**
     * resets the table
     *@param event when the button is clicked event happens
     */
    @FXML
    void resetButtonAction(ActionEvent event) throws Exception {
        ObservableList<appointments> filteredApts = FXCollections.observableArrayList();
        //for loop to make a new observablelist
        for (appointments a : getAllAppointments()) {
            filteredApts.add(a);
        }
        appointmentTableView.setItems(filteredApts);
    }

    //filtering by month
    /**
     * filters table by month using localdatetime
     * @param event when the button is clicked event happens
     */
    @FXML
    void filterByMonthButtonAction(ActionEvent event) throws Exception {
        ObservableList<appointments> filteredApts = FXCollections.observableArrayList();
        //for loop to make a new observablelist
        for (appointments a : getAllAppointments()) {
            //parsing getStart because it is a string
            LocalDateTime newTime = LocalDateTime.parse(a.getStart(), datetimeDTF);
            if (newTime.isAfter(LocalDateTime.now()) && newTime.isBefore(LocalDateTime.now().plusMonths(1))) {
                filteredApts.add(a);
            }
        }

        appointmentTableView.setItems(filteredApts);
    }
    //filtering by week
    /**
     * filters table by week using localdatetime
     * @param event When the button is clicked event happens
     */
    @FXML
    void filterByWeekButtonAction(ActionEvent event) throws Exception {
        ObservableList<appointments> filteredApts = FXCollections.observableArrayList();
        //for loop to make a new observablelist
        for (appointments a : getAllAppointments()){
            //parsing getStart because it is a string
            LocalDateTime newTime = LocalDateTime.parse(a.getStart(), datetimeDTF);
            if (newTime.isAfter(LocalDateTime.now()) && newTime.isBefore(LocalDateTime.now().plusWeeks(1))){
                filteredApts.add(a);
            }
        }
        appointmentTableView.setItems(filteredApts);
    }
    //update button
    /**
     * update button action. Gets selection model from the table and puts it in a container to set items in the update page.
     */
    @FXML
    void updateButtonAction(ActionEvent actionEvent) throws SQLException, IOException {
        if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            appointmentUp = appointmentTableView.getSelectionModel().getSelectedItem();
            //using the selectionmodels index to update the appointmentindex to fill the updated items in the next window
            updateAppointmentIndex = appointmentsContainer.indexOf(appointmentUp);
            Parent root = FXMLLoader.load(getClass().getResource("../views/updateappointmentsmenu.fxml"));
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Appointments");
            stage.setResizable(false);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment Selection Error");
            alert.setHeaderText("No Appointment Selected For Modification");
            alert.setContentText("Please Select a Appointment to Update");
            alert.showAndWait();
        }
    }
    //return method to be used in the Updates page
    public static appointments getAppointmentUpdates() {
        return appointmentUp;
    }
    //setting the appointments table using an observable list and sql statement
    /**
     * Sets appointment paramaters to an observablelist so they can be retrieved on the updates page
     */
    public void setAppointmentsTable() throws SQLException, Exception {
        String sqlStatement = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.Contact_ID FROM WJ07zyf.appointments ORDER BY appointments.Title";
        PreparedStatement ps = conn.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery(sqlStatement);
        while (rs.next()) {
            appointments appointment = new appointments();
            appointment.setAppointmentID(rs.getInt("appointments.Appointment_ID"));
            appointment.setTitle(rs.getString("appointments.Title"));
            appointment.setDescription(rs.getString("appointments.Description"));
            appointment.setLocation(rs.getString("appointments.Location"));
            appointment.setType(rs.getString("appointments.Type"));
            appointment.setCustomerID(rs.getInt("appointments.Customer_ID"));
            appointment.setContactID(rs.getInt("appointments.Contact_ID"));
            String startUTC = rs.getString("start").substring(0, 19); //getting the string from database
            String endUTC = rs.getString("end").substring(0, 19); //getting the string from database
            System.out.println(startUTC + " " + endUTC);
            LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF); // parsing string to localdatetime using datetimeDTF format
            LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF); // parsing string to localdatetime using datetimeDTF format
            System.out.println(utcStartDT + " " + utcEndDT);
            ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID); //converting to zoneddatetime
            ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID); //converting to zoneddatetime
            System.out.println(localZoneStart + " " + localZoneEnd);
            String localStartDT = localZoneStart.format(datetimeDTF); //converting back to string using localZoneStart format
            String localEndDT = localZoneEnd.format(datetimeDTF); //converting back to string using localZoneStart format
            System.out.println(localStartDT + " " + localEndDT);
            appointment.setStart(localStartDT);
            appointment.setEnd(localEndDT);
            appointmentsContainer.addAll(appointment);

        }
        //setting the table
        appointmentTableView.setItems(appointmentsContainer);
        System.out.println(appointmentsContainer);
    }
    //geting all the appointments with a sql query
    /**
     * Returns all the appointments in the ObservableList through SQL query
     * @exception SQLException
     * @return ObservableList
     */
    public ObservableList<appointments> getAllAppointments() throws SQLException {

        ObservableList<appointments> allAppointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.Contact_ID FROM WJ07zyf.appointments ORDER BY appointments.Title";

        PreparedStatement ps = conn.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery(sqlStatement);

        while (rs.next()){
            appointments appointmentResults = new appointments();
            appointmentResults.setAppointmentID(rs.getInt("appointments.Appointment_ID"));
            appointmentResults.setTitle(rs.getString("appointments.Title"));
            appointmentResults.setDescription(rs.getString("appointments.Description"));
            appointmentResults.setLocation(rs.getString("appointments.Location"));
            appointmentResults.setType(rs.getString("appointments.Type"));
            appointmentResults.setCustomerID(rs.getInt("appointments.Customer_ID"));
            appointmentResults.setContactID(rs.getInt("appointments.Contact_ID"));
            appointmentResults.setStart(rs.getString("appointments.Start").substring(0, 19));
            appointmentResults.setEnd(rs.getString("appointments.End").substring(0, 19));

            utcStartDTTwo = LocalDateTime.parse(appointmentResults.getStart(), datetimeDTF);
            utcEndDTTwo = LocalDateTime.parse(appointmentResults.getEnd(), datetimeDTF);

            allAppointments.add(appointmentResults);
            System.out.println(allAppointments.size());
            System.out.println(appointmentResults.getStart());
        }
        return allAppointments;
    }
    /**
     * cell initialization and setting the appointments table
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initializing cells
            PropertyValueFactory<appointments, String> apptStartFactory = new PropertyValueFactory<>("Start");
            PropertyValueFactory<appointments, String> apptEndFactory = new PropertyValueFactory<>("End");
            PropertyValueFactory<appointments, String> apptTitleFactory = new PropertyValueFactory<>("title");
            PropertyValueFactory<appointments, String> apptTypeFactory = new PropertyValueFactory<>("type");
            PropertyValueFactory<appointments, String> apptDescriptionFactory = new PropertyValueFactory<>("description");
            PropertyValueFactory<appointments, String> apptLocationFactory = new PropertyValueFactory<>("location");
            PropertyValueFactory<appointments, String> apptIDFactory = new PropertyValueFactory<>("appointmentID");
            PropertyValueFactory<appointments, String> apptCustomerFactory = new PropertyValueFactory<>("customerID");
            PropertyValueFactory<appointments, String> apptContactFactory = new PropertyValueFactory<>("contactID");
            //setting cells
            appointmentIDColumn.setCellValueFactory(apptIDFactory);
            locationColumn.setCellValueFactory(apptLocationFactory);
            contactColumn.setCellValueFactory(apptContactFactory);
            descriptionColumn.setCellValueFactory(apptDescriptionFactory);
            startColumn.setCellValueFactory(apptStartFactory);
            endColumn.setCellValueFactory(apptEndFactory);
            titleColumn.setCellValueFactory(apptTitleFactory);
            typeColumn.setCellValueFactory(apptTypeFactory);
            customerIDColumn.setCellValueFactory(apptCustomerFactory);

        try {
            //setting appointment table on startup
            setAppointmentsTable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

