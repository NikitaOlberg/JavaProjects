import java.util.ArrayList;
import java.util.List;

class WeatherUpdatedEvent {
    private final Object source;
    private final String weatherData;
    private final String timestamp;

    public WeatherUpdatedEvent(Object source, String weatherData, String timestamp) {
        this.source = source;
        this.weatherData = weatherData;
        this.timestamp = timestamp;
    }

    public Object getSource() {
        return source;
    }

    public String getWeatherData() {
        return weatherData;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

interface WeatherListener {
    void onWeatherUpdated(WeatherUpdatedEvent event);
}

class WeatherStation {
    private final List<WeatherListener> listeners = new ArrayList<>();
    private String currentWeather;

    public void addWeatherListener(WeatherListener listener) {
        listeners.add(listener);
    }

    public void removeWeatherListener(WeatherListener listener) {
        listeners.remove(listener);
    }

    public void setWeather(String newWeather) {
        this.currentWeather = newWeather;
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        WeatherUpdatedEvent event = new WeatherUpdatedEvent(this, newWeather, timestamp);
        notifyListeners(event);
    }

    private void notifyListeners(WeatherUpdatedEvent event) {
        for (WeatherListener listener : listeners) {
            listener.onWeatherUpdated(event);
        }
    }

    public String getCurrentWeather() {
        return currentWeather;
    }
}

class ConsoleDisplay implements WeatherListener {
    @Override
    public void onWeatherUpdated(WeatherUpdatedEvent event) {
        System.out.println("=== ОБНОВЛЕНИЕ ПОГОДЫ ===");
        System.out.println("Время: " + event.getTimestamp());
        System.out.println("Погода: " + event.getWeatherData());
        System.out.println("========================\n");
    }
}

class WeatherLogger implements WeatherListener {
    @Override
    public void onWeatherUpdated(WeatherUpdatedEvent event) {
        System.out.println("Записано обновление погоды: " + event.getWeatherData() + " в " + event.getTimestamp());
    }
}

class WeatherAlert implements WeatherListener {
    @Override
    public void onWeatherUpdated(WeatherUpdatedEvent event) {
        String weather = event.getWeatherData().toLowerCase();
        if (weather.contains("шторм") || weather.contains("ураган") || weather.contains("гроза")) {
            System.out.println("ВНИМАНИЕ! Опасные погодные условия: " + event.getWeatherData());
        }
    }
}

public class WeatherMonitoringSystem {
    public static void main(String[] args) {
        WeatherStation weatherStation = new WeatherStation();

        ConsoleDisplay display = new ConsoleDisplay();
        WeatherLogger logger = new WeatherLogger();
        WeatherAlert alert = new WeatherAlert();

        weatherStation.addWeatherListener(display);
        weatherStation.addWeatherListener(logger);
        weatherStation.addWeatherListener(alert);

        System.out.println("=== СИСТЕМА МОНИТОРИНГА ПОГОДЫ ===\n");

        weatherStation.setWeather("Солнечно, +25°C, ветер 5 м/с");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        weatherStation.setWeather("Облачно, +18°C, ветер 8 м/с");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        weatherStation.setWeather("Дождь, +12°C, ветер 12 м/с");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        weatherStation.setWeather("Сильная гроза, +8°C, ветер 25 м/с");

        System.out.println("\n--- Удаляем логгер из списка слушателей ---");
        weatherStation.removeWeatherListener(logger);

        weatherStation.setWeather("Ясно, +22°C, ветер 3 м/с");
    }
}