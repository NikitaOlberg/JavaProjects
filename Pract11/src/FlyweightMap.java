import java.util.HashMap;
import java.util.Map;

class Icon {
    private final String name;
    private final byte[] imageData;

    public Icon(String name) {
        this.name = name;
        this.imageData = new byte[1024];
        System.out.println("Загружена иконка: " + name);
    }

    public void draw(int x, int y) {
        System.out.printf("Рисуем '%s' в координатах (%d, %d)\n", name, x, y);
    }
}

class IconFactory {
    private static final Map<String, Icon> icons = new HashMap<>();

    public static Icon getIcon(String name) {
        Icon icon = icons.get(name);
        if (icon == null) {
            icon = new Icon(name);
            icons.put(name, icon);
        }
        return icon;
    }
    public static int getIconCacheSize() {
        return icons.size();
    }
}


class MapMarker {
    private final Icon icon;
    private final int x, y;

    public MapMarker(String iconName, int x, int y) {
        this.icon = IconFactory.getIcon(iconName);
        this.x = x;
        this.y = y;
    }

    public void draw() {
        icon.draw(x, y);
    }
}


public class FlyweightMap {
    public static void main(String[] args) {
        MapMarker[] markers = {
                new MapMarker("restaurant", 10, 20),
                new MapMarker("hotel", 30, 40),
                new MapMarker("restaurant", 50, 60),
                new MapMarker("gas_station", 70, 80),
                new MapMarker("hotel", 90, 100)
        };

        for (MapMarker marker : markers) {
            marker.draw();
        }

        System.out.println("\nВсего загружено иконок: " + IconFactory.getIconCacheSize());
    }
}