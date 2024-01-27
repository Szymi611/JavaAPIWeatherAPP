module AccuWeatherAppi {
	requires javafx.controls;
	requires org.apache.logging.log4j;
	requires javafx.fxml;
	requires java.net.http;
	requires javafx.base;
	requires com.google.gson;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml, com.google.gson;
}
