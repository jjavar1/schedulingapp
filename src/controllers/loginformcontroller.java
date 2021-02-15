package controllers;

import connections.connectToDB;
import erdmodel.appointments;
import erdmodel.users;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Duration;

import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class loginformcontroller implements Initializable {

    private ResourceBundle bundle;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label locationLabel;

    @FXML
    private Label whereAreYouLabel;

    @FXML
    private Label whereAreYouLabel2;

    @FXML
    private Label locationWhereLabel;

    @FXML
    private RadioButton remembermeButton;

    @FXML
    private TextField usernameTextField;

    private boolean validate;
    java.sql.Connection conn = connectToDB.conn; //database connection
    private static DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss"); //hr minute second format
    private DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //date and time format
    users userz = new users();
    File file = new File(System.getProperty("user.home")+"/Desktop/save.txt"); //txt file to remember login and pass
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    boolean noAppt;


    @FXML
    /**
     * Login method using validation and logging
     * @exception SQLException,IOException
     * @return No return value.
     */
    void loginButtonAction(ActionEvent event) throws SQLException, IOException {
        //boolean to validate
        validate_user(usernameTextField.getText(), passwordTextField.getText());
        //filewriter to remember login info
        String filename = "login_activity.txt";
        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        //if validate user is true or rememberme button is selected
        if (validate == true || (validate == true & remembermeButton.isSelected())) {
            save();
            userz.setUsername(usernameTextField.getText());
            //updating log file
            String loginActivity = "Successful Login Attempt: " + usernameTextField.getText() + " Date/Time: " + LocalDateTime.now();
            outputFile.println(loginActivity);
            outputFile.close();
            userAppointmentReminder(usernameTextField.getText());
            Parent root = FXMLLoader.load(getClass().getResource("../views/mainmenu.fxml"));
            Stage stages = new Stage();
            stages.setTitle("Main menu");
            stages.setScene(new Scene(root));
            stages.setResizable(false);
            stages.show();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.hide();
        }
        else {
            //updating log file
            String loginActivity = "Unsuccessful Login Attempt: " + usernameTextField.getText() + " Date/Time: " + LocalDateTime.now();
            outputFile.println(loginActivity);
            outputFile.close();
            Parent root = FXMLLoader.load(getClass().getResource("../views/errorforms.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Error");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
    }
    /**
     * Appointment reminder for appointments within 15 minutes
     * @exception SQLException
     */
    public void userAppointmentReminder(String username) throws SQLException {
        //for each appointment, checking if there are any appointments that are within 15 minutes
        for (appointments a : getAllAppointments()){
            LocalDateTime newTime = LocalDateTime.parse(a.getStart(), datetimeDTF);
            System.out.println(newTime);

            long timeDifference = ChronoUnit.MINUTES.between(LocalDateTime.now(), newTime);
            if (timeDifference > 0 && timeDifference <= 15){
                noAppt = true;
                LocalDateTime newTime2 = LocalDateTime.parse(a.getStart(), datetimeDTF);
                System.out.println(newTime2);

                LocalDate appointmentDate = newTime2.toLocalDate();
                LocalTime appointmentTime = newTime2.toLocalTime();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Reminder");
                alert.setHeaderText(null);
                alert.setContentText("Appointment ID: " + a.getAppointmentID() + "\n" +
                        " Date: " + appointmentDate + "\n" +
                        " Time: " + appointmentTime);
                alert.showAndWait();
            }
            }
        System.out.println(noAppt);
            if (!noAppt) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Reminder");
                alert.setHeaderText(null);
                alert.setContentText("You have no upcoming appointments");
                alert.showAndWait();
        }
    }
    /**
     * Getting all appointments from appointment table using SQL Select Statement
     */
    //getting all appointments for appointment reminder
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
            String startUTC = rs.getString("start").substring(0, 19);
            String endUTC = rs.getString("end").substring(0, 19);
            LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
            LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);
            ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
            ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
            String localStartDT = localZoneStart.format(datetimeDTF);
            String localEndDT = localZoneEnd.format(datetimeDTF);
            appointmentResults.setStart(localStartDT);
            appointmentResults.setEnd(localEndDT);

            allAppointments.add(appointmentResults);

        }
        return allAppointments;
    }
    /**
     * Getting all users using SQL SELECT statement
     */
    //getting all users for appointment reminder
    public ObservableList<users> getAllUsers() throws SQLException {

        ObservableList<users> allUsers = FXCollections.observableArrayList();

        String sqlStatement = "SELECT * FROM users";

        PreparedStatement ps = conn.prepareStatement(sqlStatement);

        ResultSet rs = ps.executeQuery(sqlStatement);

        while (rs.next()){
            users userRes = new users();
            userRes.setUserID(rs.getInt("User_ID"));
            userRes.setUsername(rs.getString("User_Name"));
            userRes.setPassword(rs.getString("Password"));

            allUsers.add(userRes);
        }
        return allUsers;
    }


    @FXML
    void passwordTextFieldAction(ActionEvent event) {

    }

    @FXML
    void usernameTextFieldAction(ActionEvent event) {

    }

    @FXML
    void remembermeButtonAction(ActionEvent event) {

    }
    /** LAMBDA
     * Initializes text and shows an information alert. Lambda is used to update whereareyoulabel
     * every 1 second using localtime.now
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Please Read");
        alert.setHeaderText(null);
        alert.setContentText("Please set locale to french in the 'loginformcontroller.java' controller instead of 'main' to test languages");
        alert.showAndWait();
        update();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setTimeZone(TimeZone.getDefault());
        Date dateObj = new Date();
        String dt = df.format(dateObj);
        //using lambdas to get current time in 24hr clock, including updating time realtime and also changing labels based on computer region
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), //lambda
                event -> whereAreYouLabel.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        whereAreYouLabel2.setText(dt);
        try {
            //setting languages based off region
            resourceBundle = ResourceBundle.getBundle("languages.logindetails", Locale.getDefault());
            loginButton.setText(resourceBundle.getString("signin"));
            usernameTextField.setPromptText(resourceBundle.getString("username"));
            passwordTextField.setPromptText(resourceBundle.getString("password"));
            locationLabel.setText(resourceBundle.getString("location"));
            remembermeButton.setText(resourceBundle.getString("remember"));
            locationWhereLabel.setText(Locale.getDefault().getLanguage());
        }
        catch (MissingResourceException e) {
            System.out.println("Missing resource");
        }
    }
    //validating user inputs to see if it is correct based off the database
    /**
     * Validates user by using WHERE User_Name=? AND Password=?
     * @param user checks if user string is same as database
     * @param password checks is password string is same as database
     */
    private void validate_user(String user, String password) throws SQLException {
            Statement statement = connectToDB.conn.createStatement();

            //write SQL statement
            String sqlStatement = "SELECT * FROM WJ07zyf.users WHERE User_Name=? AND Password=?";
            PreparedStatement state = conn.prepareStatement(sqlStatement);
            state.setString(1, user);
            state.setString(2, password);

            //create result object
            ResultSet rs = state.executeQuery();

            if (rs.next()) {
                userz.setUsername(rs.getString("User_Name"));
                userz.setPassword(rs.getString("Password"));
            }
                if (user.equals(userz.getUsername()) & password.equals(userz.getPassword())) {
                    validate = true;
                } else {
                    validate = false;
                }
            }
    //for rememberme button, writes to a textfile and retrieves it on program opening
    /**
     * Saves user and pass if rememberme is clicked
     */
    public void save() {
        try {
            if(!file.exists()) file.createNewFile();  //if the file !exist create a new one

            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            bw.write(usernameTextField.getText()); //write the name
            bw.newLine(); //leave a new Line
            bw.write(passwordTextField.getText()); //write the password
            bw.close(); //close the BufferdWriter

        } catch (IOException e) { e.printStackTrace(); }
    }
    /**
     * Updates upon opening the application
     */
    public void update(){ //UPDATE ON OPENING THE APPLICATION

        try {
            if(file.exists()){    //if this file exists

                Scanner scan = new Scanner(file);   //Use Scanner to read the File

                usernameTextField.setText(scan.nextLine());  //append the text to name field
                passwordTextField.setText(scan.nextLine()); //append the text to password field
                scan.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

