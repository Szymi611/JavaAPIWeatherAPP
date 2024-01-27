// &copy Piotr Stasiak Szymon Domagała

package application;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;



public class ControllerDisplayingResults {

	@FXML
	private LineChart<String, Number> temperatureChart;

    @FXML
    private Label cityLabel;
    
    @FXML
    private Label dateLabel;

    @FXML
    private Button returnButton;
    
    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;
    
    private List<?> forecasts; 

	private Stage stage;
	private Scene scene;
	private Parent root;
    
    public void setCityLabel(String cityName) {
        cityLabel.setText(cityName);
    }
    public void setDateLabel(String date) {
        dateLabel.setText(date);
    }
    
    @FXML
    private void changeScene(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChoosingCityScene.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			Main.demoLogger.error("Error loading DisplayingResultsScene.fxml", e);
		    showAlert(AlertType.ERROR, "Error", "An error occurred while loading the results scene.", null);
		}
		
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
    
	public void display(String localizationKey, String userOption) {
		final String dailyURL = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/" + localizationKey
				+ "?apikey=" + ControllerChoosingCity.API_KEY + "&details=true";
		final String hourlyURL = "http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/" + localizationKey
				+ "?apikey=" + ControllerChoosingCity.API_KEY;
		String choosedURL;
		if (userOption.equals(ControllerChoosingCity.options[0])) {
			choosedURL = hourlyURL;
		} else {
			choosedURL = dailyURL;
		}

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest getLocalizationKeyRequest;
		try {
			getLocalizationKeyRequest = HttpRequest.newBuilder().uri(new URI(choosedURL)).GET().build();
			HttpResponse<String> getResponse;
			getResponse = httpClient.send(getLocalizationKeyRequest, BodyHandlers.ofString());
			System.out.println(getResponse.body());

			Gson gson = new Gson();
			
			if (userOption.equals(ControllerChoosingCity.options[0])) {
				TranscriptHourlyForecast[] transcriptsHourly = gson.fromJson(getResponse.body(),
					TranscriptHourlyForecast[].class);
				forecasts = Arrays.asList(transcriptsHourly);
	
				if (transcriptsHourly != null && transcriptsHourly.length > 0) {

					//Pierwszą datę z prognozy
	                String rawDate = transcriptsHourly[0].getDateTime();
	                
	                Date date = null;
					try {
						date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(rawDate);
					} catch (ParseException e) {
						Main.demoLogger.error("Error parsing date: " + rawDate, e);

					    date = new Date(); // Ustawienie dzisiejszej daty jako data domyślna
					}
	                String formattedDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
	                
	                dateLabel.setText(formattedDate);
	                
					for (TranscriptHourlyForecast transcriptHourly : transcriptsHourly) {					
						System.out.println("DateTime: " + transcriptHourly.getDateTime());
						System.out.println("IconPhrase: " + transcriptHourly.getIconPhrase());
						System.out.println("HasPrecipitation: " + transcriptHourly.isHasPrecipitation());
						System.out.println("IsDaylight: " + transcriptHourly.isIsDaylight());
						System.out.println("Temperature Value: " + transcriptHourly.getTemperature().getValue());
						System.out.println("PrecipitationProbability: " + transcriptHourly.getPrecipitationProbability());
						System.out.println("---------------------------------------------");
					}
				}
			}
			else {
		        WeatherResponse weatherResponse = gson.fromJson(getResponse.body(), WeatherResponse.class);
		        
		        forecasts = weatherResponse.getDailyForecasts();
		        Date date = null;
		        date = new Date();
		        String formattedDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
		        dateLabel.setText(formattedDate);
		        // Access parsed data from the weatherResponse object
		        System.out.println("Headline: " + weatherResponse.getHeadline().getText());

		        for (DailyForecast forecast : weatherResponse.getDailyForecasts()) {
		            System.out.println("Date: " + forecast.getDate());
		            System.out.println("Temperature: Min = " + forecast.getTemperature().getMinimum().getValue() + " stopni C"
		            		+ ", Max = " + forecast.getTemperature().getMaximum().getValue() + " stopni C");
		            System.out.println("Day: " + forecast.getDay().getIconPhrase());
		            System.out.println(forecast.getDay().isHasPrecipitation() + "\t" + forecast.getDay().getPrecipitationType() + "\t"
		            		+ forecast.getDay().getPrecipitationIntensity());
		            System.out.println(forecast.getDay().getPrecipitationProbability() + " <- opady");
		            System.out.println(forecast.getDay().getPrecipitationIntensity());

		            System.out.println("Night: " + forecast.getNight().getIconPhrase());
		            System.out.println("------------------");
		        }
			}

		} catch (URISyntaxException e) {
		    Main.demoLogger.error("URI syntax error: " + e.getMessage(), e);
		} catch (IOException e) {
		    Main.demoLogger.error("I/O exception occurred: " + e.getMessage(), e);
		} catch (InterruptedException e) {
		    Main.demoLogger.error("Operation was interrupted: " + e.getMessage(), e);
		}
		
		drawCharts(forecasts);
	}

	@SuppressWarnings("unchecked")
	public void drawCharts(List<?> forecasts) {
	    if (temperatureChart == null) {
	        System.out.println("temperatureChart is null!");
	        return;
	    }

	    XYChart.Series<String, Number> temperatureSeries = new XYChart.Series<>();
	    temperatureSeries.setName("Temperature [°C]");

	    XYChart.Series<String, Number> precipitationSeries = new XYChart.Series<>();
	    precipitationSeries.setName("Precipitation Probability [%]");

	    SimpleDateFormat inputFormat;
	    SimpleDateFormat outputFormat;

	    if (forecasts.get(0) instanceof TranscriptHourlyForecast) {
	        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	        outputFormat = new SimpleDateFormat("HH:mm");

	        for (TranscriptHourlyForecast transcriptHourly : (List<TranscriptHourlyForecast>) forecasts) {
	            String dateTimeString = transcriptHourly.getDateTime();
	            Date dateTime;
	            try {
	                dateTime = inputFormat.parse(dateTimeString);
	            } catch (ParseException e) {
	                Main.demoLogger.error("Error parsing date: " + dateTimeString + ". Error: " + e.getMessage());
	                continue; // Skip this iteration if the date is invalid
	            }

	            String formattedTime = outputFormat.format(dateTime);
	            int temperature = transcriptHourly.getTemperature().getValue();
	            temperatureSeries.getData().add(new XYChart.Data<>(formattedTime, temperature));

	            int precipitationProbability = transcriptHourly.getPrecipitationProbability();
	            precipitationSeries.getData().add(new XYChart.Data<>(formattedTime, precipitationProbability));
	        }
	    } else if (forecasts.get(0) instanceof DailyForecast) {
	        inputFormat = new SimpleDateFormat("yyyy-MM-dd");
	        outputFormat = new SimpleDateFormat("dd.MM.yyyy");

	        for (DailyForecast dailyForecast : (List<DailyForecast>) forecasts) {
	        	
	            String dateString = dailyForecast.getDate();
	            Date date;
	            try {
	                date = inputFormat.parse(dateString);
	            } catch (ParseException e) {
	                Main.demoLogger.error("Error parsing date: " + dateString + ". Error: " + e.getMessage());
	                continue; // Skip this iteration if the date is invalid
	            }

	            String formattedDate = outputFormat.format(date);
	            int averageTemperature = (dailyForecast.getTemperature().getMinimum().getValue() +
	                                      dailyForecast.getTemperature().getMaximum().getValue()) / 2;
	            temperatureSeries.getData().add(new XYChart.Data<>(formattedDate, averageTemperature));

	    	    int precipitationProbability = dailyForecast.getDay().getPrecipitationProbability();
	    	    int precipitationProbabilityValue = 0; // Domyślna wartość w przypadku problemu z parsowaniem

	    	    try {
	    	        precipitationProbabilityValue = precipitationProbability;
	    	        System.out.println("Precipitation Probability: " + precipitationProbabilityValue);
	    	    } catch (NumberFormatException e) {
	    	        System.err.println("Error parsing precipitation probability: " + e.getMessage());
	    	    }
	            precipitationSeries.getData().add(new XYChart.Data<>(formattedDate, precipitationProbabilityValue));
	        }
	    }
	    
	    temperatureChart.getData().addAll(temperatureSeries, precipitationSeries);
	    
	    
	}


    private void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}