// &copy Piotr Stasiak Szymon Domagała

package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerRegisterScene {

	@FXML
	private Button submitButton;
	@FXML
	private Button registerButton;
	@FXML
	private Label fillInLabel;
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private TextField repeatPasswordTextField;
	@FXML
	private Label loginLabel;

	String username;
	String password;
	String repeatPassword;
	static boolean canTryToAuthenticate;

	private Stage stage;
	private Scene scene;
	private Parent root;

	public void initialize() {
		// Set focus on usernameTextField when the application starts
		Platform.runLater(() -> usernameTextField.requestFocus());

		// Add key event listener to usernameTextField
		usernameTextField.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				submit(null); // Call the submit method when Enter is pressed
			} else if (event.getCode().equals(KeyCode.TAB)) {
				passwordTextField.requestFocus(); // Move focus to passwordTextField on Tab press
			}
		});

		// Add key event listener to passwordTextField
		repeatPasswordTextField.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				submit(new ActionEvent(passwordTextField, null)); // Call the submit method when Enter is pressed
			}
		});
	}

    public void submit(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String repeatPassword = repeatPasswordTextField.getText();

        if (validateInput(username, password, repeatPassword)) {
            try {
                UsernameAndPasswordCheck usernameAndPasswordCheck = new UsernameAndPasswordCheck();
                if (usernameAndPasswordCheck.registerUser(username, password)) {
                    Main.demoLogger.info("User {} registered successfully.", username);
                    showAlert(AlertType.INFORMATION, "Registration", "Registration was successful!", "Press OK to proceed.");
                    changeScene(event, "ChoosingCityScene.fxml");
                } else {
                    Main.demoLogger.warn("Usersername {} is already taken.", username);
                    showAlert(AlertType.ERROR, "Register", "Username already taken.", "Press OK to try again.");
                }
            } catch (IOException e) {
                Main.demoLogger.error("Error loading ChoosingCityScene.fxml", e);
                showAlert(AlertType.ERROR, "Error", "An error occurred while loading the next scene.", null);
            } catch (Exception e) {
                Main.demoLogger.error("Unexpected error during registration", e);
                showAlert(AlertType.ERROR, "Error", "An unexpected error occurred.", null);
            }
        }
    }

    private boolean validateInput(String username, String password, String repeatPassword) {
        if (username.contains(" ") || password.contains(" ")) {
            fillInLabel.setText("Username or password contains invalid character!");
            return false;
        }
        if (password.isEmpty() || username.isEmpty() || repeatPassword.isEmpty()) {
            fillInLabel.setText("Fields cannot be empty!");
            return false;
        }
        if (password.length() < 5) {
            fillInLabel.setText("Your password is too short.");
            return false;
        }
        if (!password.equals(repeatPassword)) {
            fillInLabel.setText("Passwords do not match.");
            return false;
        }
        return true;
    }

    public void login(MouseEvent event) throws IOException {
        changeScene(event, "LoginScene.fxml");
    }

    private void changeScene(Event event, String fxmlFile) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
