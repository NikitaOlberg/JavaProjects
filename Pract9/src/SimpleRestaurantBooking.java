import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

class Table {
    private int id;
    private int capacity;
    private String features;

    public Table(int id, int capacity, String features) {
        this.id = id;
        this.capacity = capacity;
        this.features = features;
    }

    public int getId() { return id; }
    public int getCapacity() { return capacity; }
    public String getFeatures() { return features; }
}

class Booking {
    private String id;
    private LocalDate date;
    private LocalTime time;
    private int guests;
    private String specialRequests;
    private Table table;
    private String customerName;

    public Booking(String id, LocalDate date, LocalTime time, int guests,
                   String specialRequests, Table table, String customerName) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.guests = guests;
        this.specialRequests = specialRequests;
        this.table = table;
        this.customerName = customerName;
    }

    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public int getGuests() { return guests; }
    public String getSpecialRequests() { return specialRequests; }
    public Table getTable() { return table; }
    public String getCustomerName() { return customerName; }
}

class ReservationRequest {
    private final LocalDate date;
    private final LocalTime time;
    private final int guests;
    private final String customerName;
    private String specialRequests;

    private ReservationRequest(Builder builder) {
        this.date = builder.date;
        this.time = builder.time;
        this.guests = builder.guests;
        this.customerName = builder.customerName;
        this.specialRequests = builder.specialRequests;
    }

    public static class Builder {
        private LocalDate date;
        private LocalTime time;
        private int guests;
        private String customerName;
        private String specialRequests = "";

        public Builder(LocalDate date, LocalTime time, int guests, String customerName) {
            this.date = date;
            this.time = time;
            this.guests = guests;
            this.customerName = customerName;
        }

        public Builder specialRequests(String requests) {
            this.specialRequests = requests;
            return this;
        }

        public ReservationRequest build() {
            return new ReservationRequest(this);
        }
    }

    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public int getGuests() { return guests; }
    public String getCustomerName() { return customerName; }
    public String getSpecialRequests() { return specialRequests; }
}

class BookingManager {
    private static BookingManager instance;
    private List<Table> tables;
    private List<Booking> bookings;
    private int bookingCounter;

    private BookingManager() {
        this.tables = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.bookingCounter = 1;
        initializeTables();
    }

    public static synchronized BookingManager getInstance() {
        if (instance == null) {
            instance = new BookingManager();
        }
        return instance;
    }

    private void initializeTables() {
        tables.add(new Table(1, 2, "Стандарт"));
        tables.add(new Table(2, 4, "У окна"));
        tables.add(new Table(3, 6, "VIP"));
        tables.add(new Table(4, 4, "В углу"));
        tables.add(new Table(5, 8, "Большой зал"));
    }

    public Booking makeReservation(ReservationRequest request) {
        Table availableTable = findAvailableTable(request);

        if (availableTable == null) {
            throw new RuntimeException("Нет доступных столиков на " + request.getDate() + " в " + request.getTime());
        }

        String bookingId = "B" + bookingCounter++;
        Booking booking = new Booking(
                bookingId,
                request.getDate(),
                request.getTime(),
                request.getGuests(),
                request.getSpecialRequests(),
                availableTable,
                request.getCustomerName()
        );

        bookings.add(booking);
        System.out.println("Бронь создана: " + bookingId + " для " + request.getCustomerName());
        return booking;
    }

    private Table findAvailableTable(ReservationRequest request) {
        Set<Integer> bookedTableIds = new HashSet<>();
        for (Booking booking : bookings) {
            if (booking.getDate().equals(request.getDate()) &&
                    booking.getTime().equals(request.getTime())) {
                bookedTableIds.add(booking.getTable().getId());
            }
        }

        for (Table table : tables) {
            if (!bookedTableIds.contains(table.getId()) &&
                    table.getCapacity() >= request.getGuests()) {

                if (request.getSpecialRequests() == null ||
                        request.getSpecialRequests().isEmpty() ||
                        table.getFeatures().toLowerCase().contains(request.getSpecialRequests().toLowerCase())) {
                    return table;
                }
            }
        }
        return null;
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public List<Table> getTables() {
        return new ArrayList<>(tables);
    }
}

public class SimpleRestaurantBooking {
    public static void main(String[] args) {
        BookingManager manager = BookingManager.getInstance();

        ReservationRequest request1 = new ReservationRequest.Builder(
                LocalDate.now().plusDays(1),
                LocalTime.of(19, 0),
                4,
                "Иван Иванов"
        ).specialRequests("окна").build();

        manager.makeReservation(request1);

        ReservationRequest request2 = new ReservationRequest.Builder(
                LocalDate.now().plusDays(1),
                LocalTime.of(20, 0),
                2,
                "Петр Петров"
        ).build();

        manager.makeReservation(request2);

        try {
            ReservationRequest request3 = new ReservationRequest.Builder(
                    LocalDate.now().plusDays(1),
                    LocalTime.of(19, 0),
                    4,
                    "Сергей Сергеев"
            ).build();

            manager.makeReservation(request3);
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nВсе бронирования:");
        for (Booking booking : manager.getBookings()) {
            System.out.println("Бронь " + booking.getId() + ": " +
                    booking.getCustomerName() + ", " +
                    booking.getTable().getId() + " столик, " +
                    booking.getGuests() + " гостей");
        }
    }
}