// Version 9.0

import java.util.*;

// ---------------------- CUSTOM EXCEPTION ----------------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ---------------------- RESERVATION ----------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ---------------------- INVENTORY ----------------------
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0); // unavailable
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void decrementRoom(String roomType) throws InvalidBookingException {
        int current = getAvailability(roomType);

        if (current < 0) {
            throw new InvalidBookingException("Invalid Room Type: " + roomType);
        }

        if (current == 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("\n=== Current Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// ---------------------- VALIDATOR ----------------------
class InvalidBookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (r.getRoomType() == null || r.getRoomType().trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        int availability = inventory.getAvailability(r.getRoomType());

        if (availability == -1) {
            throw new InvalidBookingException("Room type does not exist: " + r.getRoomType());
        }

        if (availability <= 0) {
            throw new InvalidBookingException("No availability for room type: " + r.getRoomType());
        }
    }
}

// ---------------------- BOOKING SERVICE ----------------------
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {

        try {
            // Fail-Fast Validation
            InvalidBookingValidator.validate(r, inventory);

            // Safe allocation
            inventory.decrementRoom(r.getRoomType());

            System.out.println("✅ Booking Confirmed for " + r.getGuestName()
                    + " (" + r.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("❌ Booking Failed: " + e.getMessage());
        }
    }
}

// ---------------------- MAIN CLASS ----------------------
public class bookmystayapp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Test cases (valid + invalid)
        Reservation r1 = new Reservation("Alice", "Single Room");   // valid
        Reservation r2 = new Reservation("", "Double Room");        // invalid name
        Reservation r3 = new Reservation("Bob", "Suite Room");      // no availability
        Reservation r4 = new Reservation("Charlie", "King Room");   // invalid type

        // Process bookings
        service.processBooking(r1);
        service.processBooking(r2);
        service.processBooking(r3);
        service.processBooking(r4);

        // Show final inventory (state remains valid)
        inventory.displayInventory();

        System.out.println("\nApplication Terminated.");
    }
}