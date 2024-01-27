// &copy Piotr Stasiak Szymon Domagała

package application;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.EventObject;
import java.util.ResourceBundle;
import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;

public class ControllerChoosingCity implements Initializable {

	@FXML
	private Button submitButton;
	@FXML
	private TextField cityTextField;
	@FXML
	private ChoiceBox<String> optionsChoiceBox;

	protected static String[] options = { "Hourly forecast", "Daily forecast" };
	private String userOption;
	public final static String API_KEY = "8rv7Efr0eGhJJI9vMVpNXo1gIIdChm9P";
	// or second one if previous one is unathorized due to too many requests:
	// 8rv7Efr0eGhJJI9vMVpNXo1gIIdChm9P
	// DikFOdh3Wezo7LimdB6SnsPj9A8zAIuI
	private String localizationKeyValue = "";
	private Stage stage;
	private Scene scene;
	private Parent root;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		Platform.runLater(() -> cityTextField.requestFocus());
		
		optionsChoiceBox.getItems().addAll(options);
		optionsChoiceBox.setOnAction(this::getUserOption);
	}

	public void getUserOption(ActionEvent event) {
		userOption = optionsChoiceBox.getValue();
	}

	public void submit(ActionEvent event) {
		String userCity = cityTextField.getText().trim();
		boolean readyToChangeScene = false;
		final String GET_LOCALIZATION_KEY_URL = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey="
				+ API_KEY + "&q=";

		if (userCity.isEmpty()) {
			Main.demoLogger.error("User left the field empty");
			ControllerRegisterScene.showAlert(AlertType.ERROR, "Error", "This field cannot be empty!", "Press OK to proceed.");

		} else if (containsOnlyLettersAndSpaces(userCity)) {
			userCity = userCity.replace(" ", "%20");
			// zapytanie http
			HttpClient httpClient = HttpClient.newHttpClient();
			HttpRequest getLocalizationKeyRequest;
			try {
				getLocalizationKeyRequest = HttpRequest.newBuilder().uri(new URI(GET_LOCALIZATION_KEY_URL + userCity))
						.GET().build();
				HttpResponse<String> getResponse;
				getResponse = httpClient.send(getLocalizationKeyRequest, BodyHandlers.ofString());

				Gson gson = new Gson();
				TranscriptHourlyForecast[] transcripts = gson.fromJson(getResponse.body(),
						TranscriptHourlyForecast[].class);

				// Get the first value of "Key" from the array
				if (transcripts != null && transcripts.length > 0) {
					localizationKeyValue = transcripts[0].getKey();
					Main.demoLogger.info("Successful retrieval of the key");

					if (userOption == null) {
						Main.demoLogger.error("User did not chose any option");
						ControllerRegisterScene.showAlert(AlertType.ERROR, "Choose option", "You have to choose one of the options!", "Press OK to proceed.");

					} else
						readyToChangeScene = true;

				} else {
					Main.demoLogger.error("Unsuccessful retrieval of the key");
					ControllerRegisterScene.showAlert(AlertType.ERROR, "There is no city with that name", "We couldn't find any city with the name you provided. Please try again", "Press OK to proceed.");
					cityTextField.setText("");
				}

			 } catch (URISyntaxException e) {
	                Main.demoLogger.error("URI syntax error", e);
	                ControllerRegisterScene.showAlert(AlertType.ERROR, "Error", "URI syntax error. Please try again.", null);
	            } catch (IOException | InterruptedException e) {
	                Main.demoLogger.error("Error during HTTP request", e);
	                ControllerRegisterScene.showAlert(AlertType.ERROR, "Error", "An error occurred while retrieving data. Please try again.", null);
	            }
		} else {
			Main.demoLogger.error("Invalid characters in city name");
			ControllerRegisterScene.showAlert(AlertType.ERROR, "Invalid character", "City name can only contains letters!", "Press OK to proceed.");
		}

		// jesli użytkownik wpisał poprawną nazwę miasta to przechodzimy dalej
		if (readyToChangeScene)
			changeScene(localizationKeyValue, event);
	}

	private void changeScene(String localizationKeyValue, ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayingResultsScene.fxml"));

		try {
			root = loader.load();
		} catch (IOException e) {
			Main.demoLogger.error("Error loading DisplayingResultsScene.fxml", e);
		    ControllerRegisterScene.showAlert(AlertType.ERROR, "Error", "An error occurred while loading the results scene.", null);
		}

		ControllerDisplayingResults controllerDisplayingResults = loader.getController();

		controllerDisplayingResults.setCityLabel(cityTextField.getText().toUpperCase());

	    controllerDisplayingResults.display(localizationKeyValue, userOption);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public boolean containsOnlyLettersAndSpaces(String input) {
		return input.matches("[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]+");
	}
}