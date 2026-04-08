// Version 2.1

abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double size;
    private double price;

    // Constructor
    public Room(String roomType, int numberOfBeds, double size, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    // Getters (Encapsulation)
    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    // Abstract method
    public abstract void displayDetails();
}

// Single Room Class
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200.0, 3000.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

// Double Room Class
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350.0, 5000.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

// Suite Room Class
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 9000.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

// Main Application Class
public class bookmystayapp {

    public static void main(String[] args) {

        // Creating Room Objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static Availability (Simple Variables)
        int singleAvailability = 10;
        int doubleAvailability = 5;
        int suiteAvailability = 2;

        System.out.println("=== Hotel Room Details ===\n");

        // Display Single Room
        single.displayDetails();
        System.out.println("Available: " + singleAvailability);
        System.out.println("---------------------------");

        // Display Double Room
        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailability);
        System.out.println("---------------------------");

        // Display Suite Room
        suite.displayDetails();
        System.out.println("Available: " + suiteAvailability);
        System.out.println("---------------------------");

        System.out.println("\nApplication Terminated.");
    }
}