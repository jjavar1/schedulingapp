package errors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class errorexitcontroller implements Initializable {
    @FXML
    private Label errorTextLabel;

    @FXML
    private Button continueLabel;

    @FXML
    void continueLabelAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            resourceBundle = ResourceBundle.getBundle("languages.logindetails", Locale.getDefault());
            errorTextLabel.setText(resourceBundle.getString("leave"));
            continueLabel.setText(resourceBundle.getString("continue"));
        } catch (MissingResourceException e){ {
            System.out.println("Missing resource");
        }

        }
    }
}
