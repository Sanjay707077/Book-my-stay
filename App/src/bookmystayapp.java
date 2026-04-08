// Version 10.0

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
}

// ---------------------- INVENTORY ----------------------
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
    }

    public void decrementRoom(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n=== Current Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// ---------------------- BOOKING STORE ----------------------
class BookingStore {

    // Active bookings
    private Map<String, Reservation> activeBookings = new HashMap<>();

    // Stack for rollback (LIFO)
    private Stack<String> releasedRoomIds = new Stack<>();

    public void addBooking(Reservation r) {
        activeBookings.put(r.getReservationId(), r);
    }

    public Reservation getBooking(String id) {
        return activeBookings.get(id);
    }

    public void removeBooking(String id) {
        activeBookings.remove(id);
    }

    public boolean exists(String id) {
        return activeBookings.containsKey(id);
    }

    public void pushReleasedRoom(String roomId) {
        releasedRoomIds.push(roomId);
    }

    public void displayReleasedRooms() {
        System.out.println("\nRollback Stack (LIFO): " + releasedRoomIds);
    }
}

// ---------------------- CANCELLATION SERVICE ----------------------
class CancellationService {

    private RoomInventory inventory;
    private BookingStore store;

    public CancellationService(RoomInventory inventory, BookingStore store) {
        this.inventory = inventory;
        this.store = store;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing Cancellation: " + reservationId);

        // Validation
        if (!store.exists(reservationId)) {
            System.out.println("❌ Cancellation Failed: Reservation does not exist.");
            return;
        }

        Reservation r = store.getBooking(reservationId);

        // Step 1: Record rollback (push to stack)
        store.pushReleasedRoom(reservationId);

        // Step 2: Restore inventory
        inventory.incrementRoom(r.getRoomType());

        // Step 3: Remove booking
        store.removeBooking(reservationId);

        // Confirmation
        System.out.println("✅ Cancellation Successful!");
        System.out.println("Guest: " + r.getGuestName());
        System.out.println("Room Type Restored: " + r.getRoomType());
    }
}

// ---------------------- MAIN CLASS ----------------------
public class bookmystayapp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Booking store
        BookingStore store = new BookingStore();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("SI-101", "Alice", "Single Room");
        Reservation r2 = new Reservation("DO-201", "Bob", "Double Room");

        store.addBooking(r1);
        store.addBooking(r2);

        // Assume inventory already reduced (from allocation)
        inventory.decrementRoom("Single Room");
        inventory.decrementRoom("Double Room");

        // Initialize cancellation service
        CancellationService cancelService = new CancellationService(inventory, store);

        // Perform cancellations
        cancelService.cancelBooking("SI-101");  // valid
        cancelService.cancelBooking("SI-999");  // invalid

        // Display final state
        inventory.displayInventory();
        store.displayReleasedRooms();

        System.out.println("\nApplication Terminated.");
    }
}