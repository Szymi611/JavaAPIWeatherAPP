// &copy Piotr Stasiak Szymon Domagała

package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerLoadingScene {

	@FXML
	private Button submitButton;
	@FXML
	private Label loginLabel;
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private Label registerLabel;
	@FXML
	private Label copyrightLabel;
	String username;
	String password;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	

	public void initialize() {
		// Set focus on usernameTextField when the application starts
		Platform.runLater(() -> usernameTextField.requestFocus());
		copyrightLabel.setText("\u00a9" + "Piotr Stasiak Szymon Domagała");
        // Add key event listener to usernameTextField
        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                submit(new ActionEvent(passwordTextField, null)); // Call the submit method when Enter is pressed
            } else if (event.getCode().equals(KeyCode.TAB)) {
                passwordTextField.requestFocus(); // Move focus to passwordTextField on Tab press
            }
        });


        // Add key event listener to passwordTextField
        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                submit(new ActionEvent(passwordTextField, null)); // Call the submit method when Enter is pressed
            }
        });
    }

	
	public void submit(ActionEvent event) {
        try {
            username = usernameTextField.getText();
            password = passwordTextField.getText();

            UsernameAndPasswordCheck usernameAndPasswordCheck = new UsernameAndPasswordCheck();
            if(usernameAndPasswordCheck.authenticateUser(username, password)) {
                Main.demoLogger.info("Successful login for user: " + username);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Login");
                alert.setHeaderText("Login was successful");
                alert.setContentText("Press OK to proceed.");
                
                if(alert.showAndWait().get() == ButtonType.OK) {
                    changeScene((Node)event.getSource(), "ChoosingCityScene.fxml");
                }
            } else {
                Main.demoLogger.warn("Failed login attempt for user: " + username);

                ControllerRegisterScene.showAlert(AlertType.ERROR, null, "Incorrect login or password!",
                		"Press OK to try again.");
            }
        } catch (IOException e) {
            Main.demoLogger.error("Error loading ChoosingCityScene.fxml", e);
            ControllerRegisterScene.showAlert(AlertType.ERROR, "Error", null, "An error occurred while loading the next scene.");
        } catch (Exception e) {
            Main.demoLogger.error("Unexpected error during login", e);
            ControllerRegisterScene.showAlert(AlertType.ERROR, "Error", null, "An unexpected error occurred.");
            
        }
    }

    public void register(MouseEvent event) {
        try {
            changeScene((Node)event.getSource(), "RegisterScene.fxml");
        } catch (IOException e) {
            Main.demoLogger.error("Error loading RegisterScene.fxml", e);
            ControllerRegisterScene.showAlert(AlertType.ERROR, "Error", null, "An error occurred while loading the registration scene.");
        }
    }

    private void changeScene(Node source, String fxmlFile) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage)source.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
	
}
