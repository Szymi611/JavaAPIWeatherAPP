// &copy Piotr Stasiak Szymon Domagała

package application;

import org.apache.logging.log4j.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;

public class Main extends Application {
	@Override
	public void start(Stage stage) {

		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			demoLogger.error("Unhandled exception", throwable);
		});

		try {
			Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
			Scene scene = new Scene(root);

			Image icon = new Image("weatherIcon.png");
			stage.getIcons().add(icon);
			stage.setScene(scene);
			stage.show();

			stage.setOnCloseRequest(event -> {
				event.consume();
				logout(stage);
			});

		} catch (Exception e) {
			demoLogger.error("Wystąpił błąd podczas uruchamiania aplikacji", e);
		}
	}

	public void logout(Stage stage) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("You're about to logout!");
		alert.setContentText("Do you want to save before exiting? ");

		if (alert.showAndWait().get() == ButtonType.OK) {
			demoLogger.info("User logged out.");
			stage.close();
		} else {
			demoLogger.info("Logout canceled.");
		}
	}

	public static Logger demoLogger = LogManager.getLogger(Main.class.getName());

	public static void main(String[] args) {
		demoLogger.info("Uruchamianie aplikacji");
		launch(args);
	}
}