package controllers;

import connections.connectToDB;
import erdmodel.countries;
import erdmodel.customers;
import erdmodel.firstleveldivisions;
import erdmodel.users;
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
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class customermenucontroller implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField autoGenLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField customerNameLabel;

    @FXML
    private TextField customerAddressLabel;

    @FXML
    private TextField customerPostalLabel;

    @FXML
    private TextField phoneNumberLabel;

    @FXML
    private ComboBox<String> cityComboBox;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private TableView<customers> customerTreeTable;

    @FXML
    private TableColumn<customers, Integer> custID;

    @FXML
    private TableColumn<customers, String> custName;

    @FXML
    private TableColumn<customers, String> custPhone;

    @FXML
    private TableColumn<customers, String> custPostal;

    @FXML
    private TableColumn<customers, String> custAddress;

    @FXML
    private TableColumn<customers, String> custCountry;

    @FXML
    private TableColumn<customers, Date> custLastUpdate;

    @FXML
    private Button updateButton;

    @FXML
    private Button backButton;

    @FXML
    private Button deleteButton;


    java.sql.Connection conn = connectToDB.conn;
    private boolean customerUpdate = false; //booleans so that some items can be disabled/enabled
    private boolean customerAdd = false; //booleans so that some items can be disabled/enabled
    ObservableList<String> cityOptions = FXCollections.observableArrayList();
    ObservableList<String> countryOptions = FXCollections.observableArrayList();
    ObservableList<customers> customerStorage = FXCollections.observableArrayList();
    ObservableList<String> unitedCities = FXCollections.observableArrayList();
    ObservableList<String> kingdomCities = FXCollections.observableArrayList();
    ObservableList<String> canadaCities = FXCollections.observableArrayList();
    users users = new users();

    //cancel button
    /**
     * Cancel button, resets fields
     * @param event when the button is clicked event happens
     */
    @FXML
    void cancelButtonAction(ActionEvent event) {
        disableCustomerFields();
        customerAdd = false;
        customerUpdate = false;
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
        addButton.setDisable(false);
        infoLabel.setVisible(false);
        customerTreeTable.setDisable(true);
    }

    //add button
    /**
     * Add button, enables customer fields
     * @param event when the button is clicked event happens
     */
    @FXML
    void addButtonAction(ActionEvent event) throws Exception {
        enableCustomerFields();
        customerAdd = true;
        customerUpdate = false;
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    //back button
    /**
     * Back button. Goes back to main menu
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

    @FXML
    void autoGenLabelAction(ActionEvent event) {

    }

    @FXML
    void cityComboBoxAction(ActionEvent event) {

    }

    @FXML
    void customerAddressAction(ActionEvent event) {

    }

    @FXML
    void customerNameAction(ActionEvent event) {

    }

    @FXML
    void phoneNumberAction(ActionEvent event) {

    }

    @FXML
    void customerPostalAction(ActionEvent event) {

    }
    //delete button
    /**
     * Delete button if treetable selection model and treetable is not null
     * @param event when the button is clicked event happens
     */
    @FXML
    void deleteButtonAction(ActionEvent event) throws Exception {
        //if the tree table is not empty, disable buttons so that it wont be confusing for the user
        if (!isTreeTableEmpty()) {
            customerTreeTable.setDisable(false);
            addButton.setDisable(true);
            updateButton.setDisable(true);
            infoLabel.setVisible(true);
        }
        //deleting customers
        if (customerTreeTable.getSelectionModel().getSelectedItem() != null && customerTreeTable != null) {
            disableCustomerFields();
            customers cust = customerTreeTable.getSelectionModel().getSelectedItem();
            Alert alerts = new Alert(Alert.AlertType.CONFIRMATION);
            alerts.setTitle("Confirmation Required");
            alerts.setHeaderText(null);
            alerts.setContentText("Are you sure you want to delete " + cust.getCustomerID() + " ?");
            Optional<ButtonType> results = alerts.showAndWait();
            if (results.get() == ButtonType.OK) {
                System.out.println("Deleting customer...");
                deleteCustomer(cust);
                System.out.println("CustomerID: " + cust.getCustomerID() + " has been deleted!");

                clearCustomerFields();
                disableCustomerFields();
                customerTreeTable.getItems().clear();
                fillCustomerTable();
                customerTreeTable.setDisable(true);
                addButton.setDisable(false);
                updateButton.setDisable(false);
                infoLabel.setVisible(false);
            } else {
                return;
            }
        }

    }
    //save button action
    /**
     * Save button. If customeradd or customerupdate, check if theyre valid then add customer or update.
     * @param event when the button is clicked event happens
     */
    @FXML
    void saveButtonAction(ActionEvent event) throws Exception {
        if (customerAdd || customerUpdate) {
            if (isValidCustomer()) { //if valid customer, initiate customerAdd method
                if (customerAdd) {
                    addCustomer();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful!");
                    alert.setHeaderText(null);
                    alert.setContentText("Customer added");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        //disabling certain buttons to clear confusion
                        customerTreeTable.getItems().clear();
                        fillCustomerTable();
                        clearCustomerFields();
                        disableCustomerFields();
                        addButton.setDisable(false);
                        updateButton.setDisable(false);
                        deleteButton.setDisable(false);
                        customerTreeTable.setDisable(true);
                        alert.close();
                    }
                } else if (customerUpdate) { //for customer update
                        updateCustomer(); //use updateCustomer method
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successful!");
                        alert.setHeaderText(null);
                        alert.setContentText("Customer updated");

                    Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            //disabling buttons to clear confusion
                            customerTreeTable.getItems().clear();
                            fillCustomerTable();
                            clearCustomerFields();
                            disableCustomerFields();
                            addButton.setDisable(false);
                            updateButton.setDisable(false);
                            deleteButton.setDisable(false);
                            customerTreeTable.setDisable(true);
                            alert.close();
                        }
                    }
                }
            } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Unexpected Error Occured");
        }

        }
    //country combo box action
    /**
     * Filters cities based on selected country
     * @param event when the button is clicked event happens
     */
    @FXML
    void countryComboBoxAction(ActionEvent event) throws Exception {
        //filters country combo box based off selection, I wanted to use if statements instead of a filteredlist for this one, both works
        String currentCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (currentCountry.equals("Canada")) {
            fillCanadianCities();
        } else if (currentCountry.equals("United States")) {
            fillUSCities();
        } else if (currentCountry.equals("United Kingdom")) {
            fillUKCities();
        } else {
            fillCityBox();
            fillCountryBox();
        }
    }

    @FXML
    void phoneComboBoxAction(ActionEvent event) {

    }
    //update button action
    /**
     * Update button if treetable is not empty
     * @param event when the button is clicked event happens
     */
    @FXML
    void updateButtonAction(ActionEvent event) {
        if (!isTreeTableEmpty()) {
            enableCustomerFields();
            customerTreeTable.setDisable(false);
            customerAdd = false;
            customerUpdate = true;
            addButton.setDisable(true);
            deleteButton.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("You may now select a customer to update");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.equals(ButtonType.OK)) {
                alert.close();
            }
        }
    }
    //boolean to see if the table is empty
    public boolean isTreeTableEmpty() {
        ObservableList<?> items = customerTreeTable.getItems();
        if (items.isEmpty()) {
            return true;
        }
        return false;
    }
    //customer listener
    /**
     * Sets text from SQL statement
     * @param customer passes customer paramater
     * @exception Exception
     * @return No return value.
     */
    public void customerListener(customers customer) throws SQLException, Exception {
        customers cust = new customers();
        cust = customer;
        int custId = cust.getCustomerID();

        customerUpdate = true;
        customerAdd = false;
        enableCustomerFields();
        //sql statement joining tables
        String sqlStatement = "SELECT * FROM WJ07zyf.customers, WJ07zyf.first_level_divisions, WJ07zyf.countries  WHERE customers.Customer_ID = ? AND customers.Division_ID = first_level_divisions.Division_ID AND countries.Country_ID = first_level_divisions.Country_ID ";
        PreparedStatement ps = conn.prepareStatement(sqlStatement);
        ps.setInt(1, custId);
        ResultSet result = ps.executeQuery();
        //setting text
        while (result.next()) {
            autoGenLabel.setPromptText(Integer.toString(result.getInt("Customer_ID")));
            customerNameLabel.setText(result.getString("Customer_Name"));
            customerAddressLabel.setText(result.getString("Address"));
            customerPostalLabel.setText(result.getString("Postal_Code"));
            phoneNumberLabel.setText(result.getString("Phone"));
            cityComboBox.getSelectionModel().select(result.getString("Division"));
            countryComboBox.setValue(result.getString("Country"));
        }
    }
    //delete customer
    /**
     * Deletes customers based off Customer ID through SQL statement
     * @exception Exception
     * @return No return value.
     */
    private void deleteCustomer(customers customer) throws Exception {
        //sql statement to delete customers based off customer ID
        try {
            String sqlStatement = "DELETE customers.* FROM customers WHERE customers.customer_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sqlStatement);
            ps.setInt(1, customer.getCustomerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        clearCustomerFields();
        disableCustomerFields();
        fillCustomerTable();
    }
    //update customer button
    /**
     * Updated customer using SQL statement
     * @exception Exception
     * @return No return value.
     */
    private void updateCustomer() throws Exception {
        //sql update statement
        try {
            String currentCity = cityComboBox.getSelectionModel().getSelectedItem();
            String sqlStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = CURRENT_TIMESTAMP, Created_By = ?, Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, customerNameLabel.getText());
            ps.setString(2, customerAddressLabel.getText());
            ps.setString(3, customerPostalLabel.getText());
            ps.setString(4, phoneNumberLabel.getText());
            ps.setString(5, users.getUsername());
            ps.setString(6, users.getUsername());
            ps.setInt(7, getCityID(currentCity));
            ps.setString(8, autoGenLabel.getPromptText());
            ps.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e);
        }
        clearCustomerFields();
        disableCustomerFields();
        fillCustomerTable();

        customerAdd = false;
        customerUpdate = false;
    }
    //adding customer
    /**
     * Adds customer using SQL statement
     * @exception Exception
     * @return No return value.
     */
    private void addCustomer() throws SQLException, Exception {
        //sql insert statement by getting text from boxes
        try {
            String currentCity = cityComboBox.getSelectionModel().getSelectedItem();
            String sqlStatement = ("INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?, ?)");
            PreparedStatement ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, customerNameLabel.getText());
            ps.setString(2, customerAddressLabel.getText());
            ps.setString(3, customerPostalLabel.getText());
            ps.setString(4, phoneNumberLabel.getText());
            ps.setString(5, users.getUsername());
            ps.setString(6, users.getUsername());
            ps.setInt(7, getCityID(currentCity));
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
        customerAdd = false;
        customerUpdate = false;
    }
    //checking if the customer is valid
    /**
     * Checks if customer is valid
     * @return No return value.
     */
    private boolean isValidCustomer() {
        String customerName = customerNameLabel.getText().trim();
        String address = customerAddressLabel.getText().trim();
        String city = cityComboBox.getValue();
        String country = countryComboBox.getValue();
        String postalCode = customerPostalLabel.getText().trim();
        String phone = phoneNumberLabel.getText();

        String errorMessage = "";
        //first checks to see if inputs are null
        if (customerName == null || customerName.length() == 0) {
            errorMessage += "Please enter customer name\n";
        }
        if (address == null || address.length() == 0) {
            errorMessage += "Please enter an address\n";
        }
        if (city == null) {
            errorMessage += "Please select a city\n";
        }
        if ((city.equals(unitedCities.toString()) || city.equals(kingdomCities.toString()) && country.equals("Canada"))) {
            errorMessage += "Please select a city from the United Kingdom";
        }
        if ((city.equals(kingdomCities.toString()) || city.equals(canadaCities.toString())) && (country.equals("United States"))) {
            errorMessage += "Please select a city from the United States";
        }
        if ((city.equals(kingdomCities.toString()) || city.equals(unitedCities.toString())) && (country.equals("Canada"))) {
            errorMessage += "Please select a city from Canada";
        }
            if (country == null) {
                errorMessage += "Please select a country\n";
            }
            if (postalCode == null || postalCode.length() == 0) {
                errorMessage += "Please enter a valid postal code\n";
            } else if (postalCode.length() > 10 || postalCode.length() < 5) {
                errorMessage += "Postal code must be between 5 and 10 characters\n";
            }
            if (phone == null || phone.length() == 0) {
                errorMessage += "Please enter a Phone Number including area code).";
            } else if (phone.length() < 10 || phone.length() > 15) {
                errorMessage += "Please enter a valid phone number including area code)\n";
            }
            if (errorMessage.length() == 0) {
                return true;
            } else {
                // Show the error message.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText(errorMessage);
                Optional<ButtonType> result = alert.showAndWait();

                return false;
            }
        }
        //getting the cityID
    /**
     * Gets city ID using sql query
     * @param city Passes string city to see if division matches city
     */
    private int getCityID(String city) throws SQLException, Exception {
        //getting city ID using a SELECT sql statement
        int cityID = -1;
        String sqlStatement = "SELECT Division_ID FROM WJ07zyf.first_level_divisions WHERE first_level_divisions.Division = '" + city + "'";

        PreparedStatement pst = conn.prepareStatement(sqlStatement);

        ResultSet result = pst.executeQuery(sqlStatement);
        //resultset
        while (result.next()) {
            cityID = result.getInt("Division_ID");
        }
        return cityID;
    }
    //filling the customer table
    /**
     * Fills customer table using sql SELECT query and setting customers.
     */
    public void fillCustomerTable() throws SQLException {
    //filling customer table with a SELECT sql statement
        String sqlStatement = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, customers.Last_Update, customers.Division_ID  FROM WJ07zyf.customers ORDER BY customers.Customer_Name";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        ResultSet rs = pst.executeQuery(sqlStatement);
        // setting the parameters from the sql parameters
        while (rs.next()) {
            customers customer = new customers();

            customer.setCustomerID(rs.getInt("customers.Customer_ID"));
            customer.setCustomerName(rs.getString("customers.Customer_Name"));
            customer.setAddress(rs.getString("customers.Address"));
            customer.setPostalCode(rs.getString("customers.Postal_Code"));
            customer.setPhone(rs.getString("customers.Phone"));
            customer.setLastUpdate(rs.getDate("customers.Last_Update"));
            customer.setDivisionID(rs.getInt("customers.Division_ID"));
            customerStorage.addAll(customer);
        }
        //setting the tree table
        customerTreeTable.setItems(customerStorage);
    }
    //filling the country box
    /**
     * Fills country combobox using SELECT sql query
     */
    public void fillCountryBox() throws SQLException, Exception {
        //fills country box using SELECT sql statement
        String sqlStatement = "SELECT Country FROM WJ07zyf.countries";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        ResultSet rs = pst.executeQuery(sqlStatement);

        while (rs.next()) {
            countries country = new countries();
            country.setCountry(rs.getString("Country"));
            countryOptions.add(country.getCountry());
            countryComboBox.setItems(countryOptions);
        }
        pst.close();
        rs.close();
    }
    //filling city box
    /**
     * Fills city box using SELECT sql query
     */
    public void fillCityBox() throws SQLException, Exception {
        //filling city box using SELECT sql statement
        String sqlStatement = "SELECT Division FROM WJ07zyf.first_level_divisions";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        ResultSet rs = pst.executeQuery(sqlStatement);
        //setting parameters from sql statement columns
        while (rs.next()) {
            firstleveldivisions city = new firstleveldivisions();
            city.setDivision(rs.getString("Division"));
            cityOptions.add(city.getDivision());
            cityComboBox.setItems(cityOptions);
        }
        pst.close();
        rs.close();
    }
    /**
     * Fills Canadian cities with SQL query
     */
    //filling canadian cities
    public void fillCanadianCities () throws SQLException, Exception {
        //filling canadian cities using sql SELECT statement where the country ID is a canadian city
        String sqlStatement = "SELECT Division FROM WJ07zyf.first_level_divisions WHERE Country_ID = 38";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        ResultSet rs = pst.executeQuery(sqlStatement);
        //setting the parameters
        while (rs.next()) {
            firstleveldivisions city = new firstleveldivisions();
            city.setDivision(rs.getString("Division"));
            canadaCities.add(city.getDivision());

            cityComboBox.setItems(canadaCities);

        }
        pst.close();
        rs.close();
    }
    /**
     * Fills UK cities with SQL query
     */
    //filling UK cities
    public void fillUKCities() throws SQLException, Exception {
        //filling UK cities where the country id is from the UK
        String sqlStatement = "SELECT Division FROM WJ07zyf.first_level_divisions WHERE Country_ID = 230";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        ResultSet rs = pst.executeQuery(sqlStatement);
        //setting the parameters
        while (rs.next()) {
            firstleveldivisions city = new firstleveldivisions();
            city.setDivision(rs.getString("Division"));
            unitedCities.add(city.getDivision());

            cityComboBox.setItems(kingdomCities);
        }
        pst.close();
        rs.close();
    }
    /**
     * Fills US cities with SQL query
     */
    //filling us cities
    public void fillUSCities() throws SQLException, Exception {
        //filling us cities using US country id
        String sqlStatement = "SELECT Division FROM WJ07zyf.first_level_divisions WHERE Country_ID = 231";
        PreparedStatement pst = conn.prepareStatement(sqlStatement);
        ResultSet rs = pst.executeQuery(sqlStatement);
        //setting parameters
        while (rs.next()) {
            firstleveldivisions city = new firstleveldivisions();
            city.setDivision(rs.getString("Division"));
            unitedCities.add(city.getDivision());

            cityComboBox.setItems(unitedCities);
        }
        pst.close();
        rs.close();
    }
    //use these methods to easily clear fields so that it can look cleaner
    //clear customers
    public void clearCustomerFields(){
        autoGenLabel.clear();
        customerNameLabel.clear();
        customerAddressLabel.clear();
        customerPostalLabel.clear();
        phoneNumberLabel.clear();
    }
    //disable customers
    public void disableCustomerFields() {
        customerNameLabel.setDisable(true);
        customerAddressLabel.setDisable(true);
        customerPostalLabel.setDisable(true);
        phoneNumberLabel.setDisable(true);
        cityComboBox.setDisable(true);
        countryComboBox.setDisable(true);
    }
    //enable customers
    public void enableCustomerFields(){
        customerNameLabel.setDisable(false);
        customerAddressLabel.setDisable(false);
        cityComboBox.setDisable(false);
        countryComboBox.setDisable(false);
        customerPostalLabel.setDisable(false);
        phoneNumberLabel.setDisable(false);
    }
    /**
     * Lambda listener to show customer name from tree table
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initializing table cells
        PropertyValueFactory<customers, String> custNameFactory = new PropertyValueFactory<>("CustomerName");
        PropertyValueFactory<customers, String> custPhoneFactory = new PropertyValueFactory<>("phone");
        PropertyValueFactory<customers, Integer> custCustomerIDFactory = new PropertyValueFactory<>("CustomerID");
        PropertyValueFactory<customers, String> custPostalFactory = new PropertyValueFactory<>("postalCode");
        PropertyValueFactory<customers, String> custAddressFactory = new PropertyValueFactory<>("address");
        PropertyValueFactory<customers, String> custCountryFactory = new PropertyValueFactory<>("divisionID");
        PropertyValueFactory<customers, Date> custLastUpdateFactory = new PropertyValueFactory<>("lastUpdate");
        //initializing table cells
        custID.setCellValueFactory(custCustomerIDFactory);
        custName.setCellValueFactory(custNameFactory);
        custPhone.setCellValueFactory(custPhoneFactory);
        custPostal.setCellValueFactory(custPostalFactory);
        custAddress.setCellValueFactory(custAddressFactory);
        custCountry.setCellValueFactory(custCountryFactory);
        custLastUpdate.setCellValueFactory(custLastUpdateFactory);
        custID.setText("Auto ID");
        customerTreeTable.setDisable(true);

        try {
            //filling boxes
            fillCityBox();
            fillCountryBox();
            fillCustomerTable();
            disableCustomerFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
        infoLabel.setVisible(false);
        //customer listener using lambda

        customerTreeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        customerListener(newValue);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });
    }
}


