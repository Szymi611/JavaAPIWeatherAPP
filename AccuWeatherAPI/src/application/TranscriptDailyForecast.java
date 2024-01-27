// &copy Piotr Stasiak Szymon Domagała

package application;

import java.util.List;

class Headline {
    private String Text; //to fajne

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}
}

class Temperature {
	private TemperatureValue Minimum;
    private TemperatureValue Maximum;

    // Getters and setters
    public TemperatureValue getMinimum() {
		return Minimum;
	}
	public void setMinimum(TemperatureValue minimum) {
		Minimum = minimum;
	}
	public TemperatureValue getMaximum() {
		return Maximum;
	}
	public void setMaximum(TemperatureValue maximum) {
		Maximum = maximum;
	}
}

class TemperatureValue {
    private double Value;
    
	public int getValue() {
		// ona jest w F wiec trzeba zamienic na C imo bo kto zna na frajerhanty
		return (int) Math.round((Value-32)*5/9.0);
	}
	public void setValue(double value) {
		Value = value;
	}
}

class DayNight {
    private String IconPhrase;
    private boolean HasPrecipitation;
    private String PrecipitationType;
    private String PrecipitationIntensity;
    private int PrecipitationProbability;

    // Getters and setters
	public String getIconPhrase() {
		return IconPhrase;
	}
	public void setIconPhrase(String iconPhrase) {
		IconPhrase = iconPhrase;
	}
	public boolean isHasPrecipitation() {
		return HasPrecipitation;
	}
	public void setHasPrecipitation(boolean hasPrecipitation) {
		HasPrecipitation = hasPrecipitation;
	}
	public String getPrecipitationType() {
		return PrecipitationType;
	}
	public void setPrecipitationType(String precipitationType) {
		PrecipitationType = precipitationType;
	}
	public String getPrecipitationIntensity() {
		return PrecipitationIntensity;
	}
	public void setPrecipitationIntensity(String precipitationIntensity) {
		PrecipitationIntensity = precipitationIntensity;
	}
	public int getPrecipitationProbability() {
		return PrecipitationProbability;
	}
	public void setPrecipitationProbability(int precipitationProbability) {
		PrecipitationProbability = precipitationProbability;
	}
}

class DailyForecast {
    private String Date;
    private Temperature Temperature;
    private DayNight Day;
    private DayNight Night;

    // Getters and setters
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public Temperature getTemperature() {
		return Temperature;
	}
	public void setTemperature(Temperature temperature) {
		Temperature = temperature;
	}
	public DayNight getDay() {
		return Day;
	}
	public void setDay(DayNight day) {
		Day = day;
	}
	public DayNight getNight() {
		return Night;
	}
	public void setNight(DayNight night) {
		Night = night;
	}
}

class WeatherResponse {
    private Headline Headline;
    private List<DailyForecast> DailyForecasts;
    private String cityName;
    
	public Headline getHeadline() {
		return Headline;
	}
	public void setHeadline(Headline headline) {
		Headline = headline;
	}
	public List<DailyForecast> getDailyForecasts() {
		return DailyForecasts;
	}
	public void setDailyForecasts(List<DailyForecast> dailyForecasts) {
		DailyForecasts = dailyForecasts;
	}
	public String getCityName() {
		// TODO Auto-generated method stub
		return cityName;
	}
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
