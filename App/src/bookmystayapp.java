// Version 12.0

import java.io.*;
import java.util.*;

// ---------------------- RESERVATION ----------------------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// ---------------------- INVENTORY ----------------------
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\n=== Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// ---------------------- BOOKING HISTORY ----------------------
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getHistory() {
        return history;
    }

    public void display() {
        System.out.println("\n=== Booking History ===");
        for (Reservation r : history) {
            r.display();
        }
    }
}

// ---------------------- PERSISTENCE SERVICE ----------------------
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public static void save(RoomInventory inventory, BookingHistory history) {

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(inventory);
            oos.writeObject(history);

            System.out.println("✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving state: " + e.getMessage());
        }
    }

    // Load state from file
    public static Object[] load() {

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();

            System.out.println("✅ System state loaded successfully.");
            return new Object[]{inventory, history};

        } catch (FileNotFoundException e) {
            System.out.println("⚠ No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error loading state: " + e.getMessage());
        }

        // fallback (safe state)
        return new Object[]{new RoomInventory(), new BookingHistory()};
    }
}

// ---------------------- MAIN CLASS ----------------------
public class bookmystayapp{

    public static void main(String[] args) {

        // Load previous state (if exists)
        Object[] data = PersistenceService.load();
        RoomInventory inventory = (RoomInventory) data[0];
        BookingHistory history = (BookingHistory) data[1];

        // Display recovered state
        inventory.display();
        history.display();

        // Simulate new booking
        System.out.println("\nAdding new booking...");
        Reservation newRes = new Reservation("SI-500", "Alice", "Single Room");

        history.addReservation(newRes);

        // Update inventory
        Map<String, Integer> inv = inventory.getInventory();
        inv.put("Single Room", inv.get("Single Room") - 1);

        // Save updated state
        PersistenceService.save(inventory, history);

        System.out.println("\nApplication Terminated.");
    }
}