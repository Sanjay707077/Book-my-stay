// Version 5.0

import java.util.*;

// ---------------------- RESERVATION CLASS ----------------------
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

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// ---------------------- BOOKING REQUEST QUEUE ----------------------
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request Added: ");
        reservation.displayRequest();
        System.out.println("---------------------------");
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n=== Booking Request Queue (FIFO Order) ===\n");

        for (Reservation r : queue) {
            r.displayRequest();
        }
    }

    // Peek next request (no removal)
    public Reservation peekNext() {
        return queue.peek();
    }
}

// ---------------------- MAIN CLASS ----------------------
public class bookmystayapp {

    public static void main(String[] args) {

        // Initialize Booking Queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate Guest Requests (Arrival Order)
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests to queue (FIFO)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display all queued requests
        bookingQueue.displayQueue();

        // Peek next request (First-Come)
        System.out.println("\nNext Request to Process:");
        Reservation next = bookingQueue.peekNext();
        if (next != null) {
            next.displayRequest();
        }

        System.out.println("\nApplication Terminated.");
    }
}