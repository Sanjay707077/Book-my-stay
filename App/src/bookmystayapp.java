// Version 2.1
// Version 3.1

import java.util.HashMap;
import java.util.Map;

// Inventory Class (Centralized State Management)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor - Initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (increase/decrease)
    public void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + change);
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("=== Centralized Room Inventory ===\n");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey());
            System.out.println("Available: " + entry.getValue());
            System.out.println("---------------------------");
        }
    }
}

// Main Class
public class bookmystayapp {

    public static void main(String[] args) {

        // Initialize Inventory
        RoomInventory inventory = new RoomInventory();

        // Display Initial Inventory
        inventory.displayInventory();

        // Simulate booking (reduce availability)
        System.out.println("\nBooking 2 Single Rooms...");
        inventory.updateAvailability("Single Room", -2);

        // Simulate cancellation (increase availability)
        System.out.println("Cancelling 1 Suite Room...");
        inventory.updateAvailability("Suite Room", +1);

        // Display Updated Inventory
        System.out.println("\n=== Updated Inventory ===\n");
        inventory.displayInventory();

        System.out.println("\nApplication Terminated.");
    }
}