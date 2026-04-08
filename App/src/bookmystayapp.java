// Version 11.0

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

// ---------------------- THREAD-SAFE INVENTORY ----------------------
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
    }

    // Synchronized method (Critical Section)
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            return false;
        }

        // Simulate delay (to expose race conditions if unsynchronized)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.put(roomType, available - 1);
        return true;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory: " + inventory);
    }
}

// ---------------------- THREAD-SAFE QUEUE ----------------------
class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized enqueue
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    // synchronized dequeue
    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// ---------------------- BOOKING PROCESSOR (THREAD) ----------------------
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // Critical section: safely fetch request
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            System.out.println(getName() + " processing " + r.getGuestName());

            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println("✅ " + r.getGuestName() + " booked successfully.");
            } else {
                System.out.println("❌ " + r.getGuestName() + " booking failed (No rooms).");
            }
        }
    }
}

// ---------------------- MAIN CLASS ----------------------
public class bookmystayapp {

    public static void main(String[] args) {

        // Shared resources
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulate multiple booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Single Room"));

        // Multiple threads (simulate concurrent users)
        Thread t1 = new BookingProcessor(queue, inventory, "Thread-1");
        Thread t2 = new BookingProcessor(queue, inventory, "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory check
        inventory.displayInventory();

        System.out.println("\nApplication Terminated.");
    }
}