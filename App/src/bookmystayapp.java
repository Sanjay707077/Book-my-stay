// Version 6.0

import java.util.*;

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

// ---------------------- INVENTORY SERVICE ----------------------
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
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

// ---------------------- BOOKING REQUEST QUEUE ----------------------
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO removal
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------------------- BOOKING SERVICE ----------------------
class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs globally
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → allocated room IDs
    private HashMap<String, Set<String>> allocationMap = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String base = roomType.substring(0, 2).toUpperCase();
        String id;

        do {
            id = base + "-" + (100 + new Random().nextInt(900));
        } while (allocatedRoomIds.contains(id));

        return id;
    }

    // Process booking (Atomic logical operation)
    public void processBooking(Reservation r) {

        String type = r.getRoomType();
        int available = inventory.getAvailability(type);

        System.out.println("\nProcessing: " + r.getGuestName() + " → " + type);

        if (available <= 0) {
            System.out.println("❌ Booking Failed: No rooms available.");
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(type);

        // Ensure uniqueness (Set)
        allocatedRoomIds.add(roomId);

        // Map room type → room IDs
        allocationMap.putIfAbsent(type, new HashSet<>());
        allocationMap.get(type).add(roomId);

        // Update inventory immediately
        inventory.decrementRoom(type);

        // Confirmation
        System.out.println("✅ Booking Confirmed!");
        System.out.println("Guest: " + r.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Allocated Room ID: " + roomId);
    }

    public void displayAllocations() {
        System.out.println("\n=== Allocation Summary ===");

        for (Map.Entry<String, Set<String>> entry : allocationMap.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// ---------------------- MAIN CLASS ----------------------
public class bookmystayapp {

    public static void main(String[] args) {

        // Initialize Inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize Queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Initialize Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Process Queue
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processBooking(r);
        }

        // Display results
        bookingService.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nApplication Terminated.");
    }
}