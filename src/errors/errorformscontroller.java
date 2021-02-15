package errors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class errorformscontroller implements Initializable {
    @FXML
    private Button continueLabel;

    @FXML
    private Label errorTextLabel;

    @FXML
    void continueLabelAction(ActionEvent event) {
        Stage stage = (Stage) continueLabel.getScene().getWindow();

        stage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            resourceBundle = ResourceBundle.getBundle("languages.logindetails", Locale.getDefault());
            continueLabel.setText(resourceBundle.getString("continue"));
            errorTextLabel.setText(resourceBundle.getString("incorrect"));
        }
        catch (MissingResourceException e) {
            System.out.println("Missing resource");
        }
    }
}
