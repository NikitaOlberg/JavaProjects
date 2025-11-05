import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

class WeatherUpdatedEvent extends EventObject {
    private final String weatherData;

    public WeatherUpdatedEvent(Object source, String weatherData) {
        super(source);
        this.weatherData = weatherData;
    }

    public String getWeatherData() {
        return weatherData;
    }
}

interface WeatherListener {
    void weatherUpdated(WeatherUpdatedEvent event);
}

class WeatherService {
    private final List<WeatherListener> listeners = new ArrayList<>();

    public void addWeatherListener(WeatherListener listener) {
        listeners.add(listener);
    }

    public void removeWeatherListener(WeatherListener listener) {
        listeners.remove(listener);
    }

    public void updateWeather(String newWeatherData) {
        System.out.println("\n=== Обновление данных о погоде ===");
        WeatherUpdatedEvent event = new WeatherUpdatedEvent(this, newWeatherData);
        notifyListeners(event);
    }

    private void notifyListeners(WeatherUpdatedEvent event) {
        for (WeatherListener listener : listeners) {
            listener.weatherUpdated(event);
        }
    }
}

class ConsoleWeatherDisplay implements WeatherListener {
    @Override
    public void weatherUpdated(WeatherUpdatedEvent event) {
        System.out.println("Новые данные о погоде: " + event.getWeatherData());
    }
}

public class WeatherMonitoringSystem {
    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();
        ConsoleWeatherDisplay display = new ConsoleWeatherDisplay();

        weatherService.addWeatherListener(display);

        weatherService.updateWeather("Солнечно, +25°C");
        weatherService.updateWeather("Дождь, +15°C");
        weatherService.updateWeather("Снег, -5°C");
    }
}