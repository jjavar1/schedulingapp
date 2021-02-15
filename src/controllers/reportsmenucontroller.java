package controllers;


import connections.connectToDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class reportsmenucontroller implements Initializable {
    @FXML
    private TextArea appointmentsByMonthTextArea;

    @FXML
    private TextArea totalAppointmentsTextArea;

    @FXML
    private TextArea contactScheduleTextArea;

    @FXML
    private TextArea totalCustomersTextArea;

    @FXML
    private Button appointmentsMenuButton;

    @FXML
    private Button customerMenuButton;

    @FXML
    private Button mainMenuButton;

    java.sql.Connection conn = connectToDB.conn;
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    int jan = 0;
    int feb = 0;
    int march = 0;
    int april = 0;
    int may = 0;
    int june = 0;
    int july = 0;
    int august = 0;
    int september = 0;
    int october = 0;
    int november = 0;
    int december = 0;
    String appointmentString = "Number of appointments by month: \n";
    int account, consults, followup, closeact, trials;
    String typeString = "Number of Appointments by Type: \n";
    int phoe, newy, orlan, dall, lond, liver, toro, vanco, montr;
    String locationString = "Number of Appointments by Location: \n";
    String contactSchedule = "Appointment Schedule By Contact: \n";
    int appointmentid= 0;
    String title;
    String type;
    String description;
    String startUTC;
    String endUTC;
    LocalDateTime utcStartDT;
    LocalDateTime utcEndDT;

    //convert UTC zoneId to local zoneId
    ZonedDateTime localZoneStart;
    ZonedDateTime localZoneEnd;


    //convert ZonedDateTime to a string
    String localStartDT;
    String localEndDT;
    int customerid = 0;
    int contactid = 0;
    int numOfCols= 0;

    //appointments menu
    /**
     * Goes to appontments menu
     * @param event when button is clicked event happens
     */
    @FXML
    void appointmentsMenuButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/appointmentsmenu.fxml"));
        Stage stages = new Stage();
        stages.setTitle("Appointments menu");
        stages.setScene(new Scene(root));
        stages.setResizable(false);
        stages.show();
        Stage stage = (Stage) appointmentsMenuButton.getScene().getWindow();
        stage.close();
    }
    //customer menu
    /**
     * Goes to add customer page
     * @param event when button is clicked event happens
     */
    @FXML
    void customerMenuButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/customersmenu.fxml"));
        Stage stages = new Stage();
        stages.setTitle("Customer menu");
        stages.setScene(new Scene(root));
        stages.setResizable(false);
        stages.show();
        Stage stage = (Stage) customerMenuButton.getScene().getWindow();
        stage.close();
    }
    //main menu button
    /**
     * Goes to main menu page
     * @param event when button is clicked event happens
     */
    @FXML
    void mainMenuButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/mainmenu.fxml"));
        Stage stages = new Stage();
        stages.setTitle("Main menu");
        stages.setScene(new Scene(root));
        stages.setResizable(false);
        stages.show();
        Stage stage = (Stage) mainMenuButton.getScene().getWindow();
        stage.close();
    }
    //getting dates, start and end
    /**
     * Gets dates from SQL statement and parses them
     */
    public String getDates() throws SQLException {
    //sql statement to select start and end
        String sqlStatement = "SELECT Start, End FROM WJ07zyf.appointments";
        PreparedStatement ps = conn.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery(sqlStatement);

        while (rs.next()) {
            String startUTC = rs.getString("Start").substring(0, 19);
            //setting locatdatetime by parsing a string
            LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
            //getting the zoneddatetime
            ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
            //converting to string
            String localStartDT = localZoneStart.format(datetimeDTF);
            //getting the month using a substring
            String monthPar = localStartDT.substring(5, 7);
            //parsing month
            int month = Integer.parseInt(monthPar);
            month = month - 1;
            //incrementing months if the month parse is equal
            if (month == 0) {
                jan++;
            }
            if (month == 1) {
                feb++;
            }
            if (month == 2) {
                march++;
            }
            if (month == 3) {
                april++;
            }
            if (month == 4) {
                may++;
            }
            if (month == 5) {
                june++;
            }
            if (month == 6) {
                july++;
            }
            if (month == 7) {
                august++;
            }
            if (month == 8) {
                september++;
            }
            if (month == 9) {
                october++;
            }
            if (month == 10) {
                november++;
            }
            if (month == 11) {
                december++;
            }
        }
        //getting months using abbreviated months using a for loop
    for (int i = 0; i < 12; i++) {
        String monthName = getAbbreviatedMonth(i);
        int totalMonths = 0;
        if (i == 0) {
            totalMonths = jan;
        }
        if (i == 1) {
            totalMonths = feb;
        }
        if (i == 2) {
            totalMonths = march;
        }
        if (i == 3) {
            totalMonths = april;
        }
        if (i == 4) {
            totalMonths = may;
        }
        if (i == 5) {
            totalMonths = june;
        }
        if (i == 6) {
            totalMonths = july;
        }
        if (i == 7) {
            totalMonths = august;
        }
        if (i == 8) {
            totalMonths = september;
        }
        if (i == 9) {
            totalMonths = october;
        }
        if (i == 10) {
            totalMonths = november;
        }
        if (i == 11) {
            totalMonths = december;
        }
        //making string from for loop
        appointmentString = appointmentString + "\n" + monthName + ": " + totalMonths + "\n";
    }
        return appointmentString;
    }
    /**
     * Increments locations
     */
    public String getLocationsNumber() throws SQLException {
        //sql statement to get all locations from appointments
        String sqlStatement = "SELECT Location FROM appointments";
        PreparedStatement ps = conn.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        //incrementing the locations using .equals and seeing if they are equal in the database
        while (rs.next()) {
            String location = rs.getString("Location");
            if (location.equals("Phoenix")) {
                phoe++;
            }
            if (location.equals("New York")) {
                newy++;
            }
            if (location.equals("Orlando")) {
                orlan++;
            }
            if (location.equals("Dallas")) {
                dall++;
            }
            if (location.equals("London")) {
                lond++;
            }
            if (location.equals("Liverpool")) {
                liver++;
            }
            if (location.equals("Toronto")) {
                toro++;
            }
            if (location.equals("Montreal")) {
                montr++;
            }
            }
        //using for loop and abbreviated location numbers
        for (int i = 0; i < 9; i++) {
            String locName = getAbbreviatedLocation(i);
            int totallocs = 0;
            if (i == 0) {
                totallocs = phoe;
            }
            if (i == 1) {
                totallocs = newy;
            }
            if (i == 2) {
                totallocs = orlan;
            }
            if (i == 3) {
                totallocs = dall;
            }
            if (i == 4) {
                totallocs = lond;
            }
            if (i == 5) {
                totallocs = liver;
            }
            if (i == 6) {
                totallocs = toro;
            }
            if (i == 7) {
                totallocs = vanco;
            }
            if (i == 8) {
                totallocs = montr;
            }
            //making the string using a for loop
            locationString = locationString + "\n" + locName + ": " + totallocs + "\n";
        }
        return locationString;
    }
    //getting abbreviated location names and setting them to an integer
    public String getAbbreviatedLocation(int location) {
        String locationName = null;
        if (location == 0) {
            locationName = "Phoenix";
        }
        if (location == 1) {
            locationName = "New York";
        }
        if (location == 2) {
            locationName = "Orlando";
        }
        if (location == 3) {
            locationName = "Dallas";
        }
        if (location == 4) {
            locationName = "London";
        }
        if (location == 5) {
            locationName = "Liverpool";
        }
        if (location == 6) {
            locationName = "Toronto";
        }
        if (location == 7) {
            locationName = "Vancouver";
        }
        if (location == 8) {
            locationName = "Montreal";
        }
        return locationName;
    }

    public String getAppointmentsType() throws SQLException {
        //sql statement to get all appointment types
        String sqlStatement = "SELECT Type FROM appointments";

        PreparedStatement ps = conn.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        //incrementing the appointments types by how many there are
        while (rs.next()){

            String type = rs.getString("Type");
            if (type.equals("New Account")) {
                account++;
            }
            if (type.equals("Consultation")) {
                consults++;
            }
            if (type.equals("Follow-Up")) {
                followup++;
            }
            if (type.equals("Closing Account")) {
                closeact++;
            }
            if (type.equals("Trial")) {
                trials++;
            }
        }
        //for loop to print items using abbreviated types
        for (int i = 0; i < 5; i++) {
            String typeName = getAbbreviatedType(i);
            int totalNum = 0;
            if (i == 0) {
                totalNum = account;
            }
            if (i == 1) {
                totalNum = consults;
            }
            if (i == 2) {
                totalNum = followup;
            }
            if (i == 3) {
                totalNum = closeact;
            }
            if (i == 4) {
                totalNum = trials;
            }
            //printing out the items
            typeString = typeString + "\n" + typeName + ": " + totalNum + "\n";

        }
    return typeString;
    }

    private String getAbbreviatedType(int type) {
        //getting types and setting them to an integer
        String typeName = null;
        if (type == 0) {
            typeName = "New Account";
        }
        if (type == 1) {
            typeName = "Consultation";
        }
        if (type == 2) {
            typeName = "Follow-Up";
        }
        if (type == 3) {
            typeName = "Closing Account";
        }
        if (type == 4) {
            typeName = "Trial";
        }
        return typeName;
    }

    //getting month names and setting them to an integer
    private String getAbbreviatedMonth(int month) {
        String abbreviatedMonth = null;
        if (month == 0) {
            abbreviatedMonth = "JAN";
        }
        if (month == 1) {
            abbreviatedMonth = "FEB";
        }
        if (month == 2) {
            abbreviatedMonth = "MAR";
        }
        if (month == 3) {
            abbreviatedMonth = "APR";
        }
        if (month == 4) {
            abbreviatedMonth = "MAY";
        }
        if (month == 5) {
            abbreviatedMonth = "JUN";
        }
        if (month == 6) {
            abbreviatedMonth = "JUL";
        }
        if (month == 7) {
            abbreviatedMonth = "AUG";
        }
        if (month == 8) {
            abbreviatedMonth = "SEP";
        }
        if (month == 9) {
            abbreviatedMonth = "OCT";
        }
        if (month == 10) {
            abbreviatedMonth = "NOV";
        }
        if (month == 11) {
            abbreviatedMonth = "DEC";
        }
        return abbreviatedMonth;
    }

    /**
     * Prints schedules by contacts using SQL statement
     */
    public String scheduleByContact() throws SQLException {
        //sql statement
        String sqlStatement = "SELECT * FROM appointments, contacts WHERE appointments.Contact_ID = contacts.Contact_ID ";

        PreparedStatement ps = conn.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

                appointmentid = (rs.getInt("Appointment_ID"));
                title = rs.getString("Title");
                type = rs.getString("Type");
                description = rs.getString("Description");
                startUTC = rs.getString("Start").substring(0, 19);
                endUTC = rs.getString("End").substring(0, 19);
                utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);

                //convert UTC zoneId to local zoneId
                localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

                //convert ZonedDateTime to a string
                localStartDT = localZoneStart.format(datetimeDTF);
                localEndDT = localZoneEnd.format(datetimeDTF);
                customerid = rs.getInt("Customer_ID");
                contactid = rs.getInt("Contact_ID");
                numOfCols = rs.getRow();
                //printing out each item
                contactSchedule = contactSchedule + "Appointment ID: " + appointmentid +
                        " Title: " + title +
                        " Type: " + type +
                        " Description: " + description +
                        " Start: " + localStartDT +
                        " End: " + localEndDT +
                        " Customer ID: " + customerid + "\n";
            }
        return contactSchedule;
        }

    /**
     * Sets text areas using methods described
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setting the textboxes
        try {
            appointmentsByMonthTextArea.setText(getDates());
            totalAppointmentsTextArea.setText(getAppointmentsType());
            contactScheduleTextArea.setText(getLocationsNumber());
            totalCustomersTextArea.setText(scheduleByContact());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
