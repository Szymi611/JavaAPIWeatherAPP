// &copy Piotr Stasiak Szymon Domagała

package application;

public class TranscriptHourlyForecast {

	private String Key;

	private String DateTime;
	private String IconPhrase;
	private boolean HasPrecipitation;
	private boolean IsDaylight;
	private int PrecipitationProbability;
	private TemperatureData Temperature;

	public class TemperatureData {
		private double Value;

		public int getValue() {
			// ona jest w F wiec trzeba zamienic na C
			return (int) Math.round((Value-32)*5/9.0);
		}

		public void setValue(double value) {
			Value = value;
		}

	}

	// getters and setters
	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

	public boolean isHasPrecipitation() {
		return HasPrecipitation;
	}

	public void setHasPrecipitation(boolean hasPrecipitation) {
		HasPrecipitation = hasPrecipitation;
	}

	public boolean isIsDaylight() {
		return IsDaylight;
	}

	public void setIsDaylight(boolean isDaylight) {
		IsDaylight = isDaylight;
	}

	public int getPrecipitationProbability() {
		return PrecipitationProbability;
	}

	public void setPrecipitationProbability(int precipitationProbability) {
		PrecipitationProbability = precipitationProbability;
	}

	public String getIconPhrase() {
		return IconPhrase;
	}

	public void setIconPhrase(String iconPhrase) {
		IconPhrase = iconPhrase;
	}

	public TemperatureData getTemperature() {
		return Temperature;
	}

	public void setTemperature(TemperatureData temperature) {
		Temperature = temperature;
	}

}
