// Version 8.0

import java.util.*;

// ---------------------- RESERVATION ----------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// ---------------------- BOOKING HISTORY ----------------------
class BookingHistory {

    // List preserves insertion order
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("Added to History: " + r.getReservationId());
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }

    // Display full history
    public void displayHistory() {
        System.out.println("\n=== Booking History ===\n");

        for (Reservation r : history) {
            r.display();
        }
    }
}

// ---------------------- REPORT SERVICE ----------------------
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Generate summary report
    public void generateSummaryReport() {

        List<Reservation> reservations = history.getAllReservations();

        System.out.println("\n=== Booking Summary Report ===\n");

        System.out.println("Total Bookings: " + reservations.size());

        // Count bookings per room type
        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomCount.put(r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\nBookings by Room Type:");
        for (Map.Entry<String, Integer> entry : roomCount.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// ---------------------- MAIN CLASS ----------------------
public class bookmystayapp {

    public static void main(String[] args) {

        // Initialize Booking History
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("SI-101", "Alice", "Single Room"));
        history.addReservation(new Reservation("SI-102", "Bob", "Single Room"));
        history.addReservation(new Reservation("SU-201", "Charlie", "Suite Room"));
        history.addReservation(new Reservation("DO-301", "David", "Double Room"));

        // Display booking history
        history.displayHistory();

        // Generate report
        BookingReportService reportService = new BookingReportService(history);
        reportService.generateSummaryReport();

        System.out.println("\nApplication Terminated.");
    }
}