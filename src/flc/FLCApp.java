package flc;

import java.util.*;

public class FLCApp {

    private final Timetable timetable       = new Timetable();
    private final MemberRegistry registry   = new MemberRegistry();
    private final BookingManager manager    = new BookingManager();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new FLCApp().run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = prompt("Select option").trim();
            switch (choice) {
                case "0" -> { System.out.println("\nGoodbye!"); running = false; }
                default  -> System.out.println("  Invalid option. Try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\nFurzefield Leisure Centre Booking System  ");
        System.out.println(" ------------------MAIN MENU---------------------- ");
        System.out.println(" 1. Book a group exercise lesson");
        System.out.println(" 2. Change / Cancel a booking");
        System.out.println(" 3. Attend a lesson");
        System.out.println(" 4. Monthly lesson report");
        System.out.println(" 5. Monthly champion exercise type report");
        System.out.println(" 0. Exit");
    }

    private String prompt(String msg) {
        System.out.print("  > " + msg + ": ");
        return scanner.nextLine();
    }
}