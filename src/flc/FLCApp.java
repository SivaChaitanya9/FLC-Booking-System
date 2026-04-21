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
                case "1" -> bookLesson();
                case "2" -> changeOrCancelBooking();
                case "3" -> attendLesson();
                case "4" -> monthlyLessonReport();
                case "5" -> monthlyChampionReport();
                case "0" -> { System.out.println("\nGoodbye!"); running = false; }
                default  -> System.out.println("  Invalid option. Try again.");
            }
        }
    }

    private void bookLesson() {
        System.out.println("\n── BOOK A LESSON ──────────────────────────────");
        Member member = selectMember();
        if (member == null) return;

        List<Lesson> lessons = selectLessonsFromTimetable();
        if (lessons == null) return;

        Timetable.printLessons(lessons);
        String lessonId = prompt("Enter Lesson ID to book (or 0 to cancel)");
        if (lessonId.equals("0")) return;

        Lesson lesson = timetable.findById(lessonId);
        if (lesson == null) { System.out.println("  Lesson not found."); return; }

        Booking booking = manager.book(member, lesson);
        if (booking == null) {

        } else {
            System.out.printf("  Booking successful! ID: %s%n", booking.getBookingId());
        }
    }

    private void changeOrCancelBooking() {
        System.out.println("\n── CHANGE / CANCEL BOOKING ─────────────────────");
        Member member = selectMember();
        if (member == null) return;

        manager.printBookingsForMember(member);

        String bookingId = prompt("Enter Booking ID to modify (or 0 to cancel)");
        if (bookingId.equals("0")) return;

        Booking booking = manager.findById(bookingId);
        if (booking == null || !booking.getMember().equals(member)) {
            System.out.println("  Invalid booking.");
            return;
        }
        if (booking.getStatus() == BookingStatus.ATTENDED) {
            System.out.println("  Cannot change or cancel an attended booking.");
            return;
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            System.out.println("  Booking already cancelled.");
            return;
        }
        System.out.println("  " + booking);
        System.out.println("  1. Change to a new lesson");
        System.out.println("  2. Cancel this booking");
        String choice = prompt("Select").trim();

        if (choice.equals("2")) {
            boolean ok = manager.cancelBooking(bookingId);
            System.out.println(ok ? "  Booking cancelled." : "  Cannot cancel this booking.");
            return;
        }

        if (choice.equals("1")) {
            List<Lesson> lessons = selectLessonsFromTimetable();
            if (lessons == null) return;
            Timetable.printLessons(lessons);
            String newId = prompt("Enter new Lesson ID (or 0 to cancel)");
            if (newId.equals("0")) return;
            Lesson newLesson = timetable.findById(newId);
            if (newLesson == null) { System.out.println("  Lesson not found."); return; }
            boolean ok = manager.changeBooking(bookingId, newLesson);
            System.out.println(ok ? "  Booking changed." : "  Change failed: lesson full or duplicate.");
        }
    }

    private void attendLesson() {
        System.out.println("\n── ATTEND A LESSON ──────────────────────────────");
        Member member = selectMember();
        if (member == null) return;

        manager.printBookingsForMember(member);

        String bookingId = prompt("Enter Booking ID to attend (or 0 to cancel)");
        if (bookingId.equals("0")) return;

        Booking booking = manager.findById(bookingId);
        if (booking == null || !booking.getMember().equals(member)) {
            System.out.println("  Invalid booking.");
            return;
        };
        if (booking.getStatus() == BookingStatus.ATTENDED) {
            System.out.println("  Already attended."); return;
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            System.out.println("  Booking is cancelled."); return;
        }


        String review = prompt("Write your review");
        int rating = 0;
        while (rating < 1 || rating > 5) {
            try {
                rating = Integer.parseInt(prompt("Rating (1=Very Dissatisfied … 5=Very Satisfied)").trim());
            } catch (NumberFormatException e) { /* retry */ }
            if (rating < 1 || rating > 5) System.out.println("  Please enter a number between 1 and 5.");
        }

        boolean ok = manager.attendLesson(bookingId, review, rating);
        System.out.println(ok ? "  Lesson attended. Thank you for your review!" : "  Failed to mark attendance.");
    }

    private void monthlyLessonReport() {
        int month = promptMonth();
        if (month == -1) return;
        manager.printMonthlyLessonReport(month, timetable);
    }

    private void monthlyChampionReport() {
        int month = promptMonth();
        if (month == -1) return;
        manager.printMonthlyChampionReport(month, timetable);
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

    private Member selectMember() {
        registry.printMembers();
        String id = prompt("Enter Member ID");
        Member m = registry.findById(id);
        if (m == null) System.out.println("  Member not found.");
        return m;
    }

    private List<Lesson> selectLessonsFromTimetable() {
        System.out.println("  View timetable by:");
        System.out.println("    1. Day (Saturday / Sunday)");
        System.out.println("    2. Exercise type");
        String choice = prompt("Select").trim();

        if (choice.equals("1")) {
            String day = prompt("Enter day (Saturday or Sunday)").trim();
            if (!day.equalsIgnoreCase("Saturday") && !day.equalsIgnoreCase("Sunday")) {
                System.out.println("  Invalid day.");
                return null;
            }
            List<Lesson> list = timetable.findByDay(day);
            if (list.isEmpty()) System.out.println("  No lessons found.");
            return list.isEmpty() ? null : list;
        } else if (choice.equals("2")) {
            System.out.println("  Exercise types: YOGA, ZUMBA, AQUACISE, BOX_FIT, BODY_BLITZ");
            String typeName = prompt("Enter exercise type").trim().toUpperCase().replace(" ","_");
            ExerciseType et = ExerciseType.fromString(typeName);
            if (et == null) { System.out.println("  Unknown exercise type."); return null; }
            List<Lesson> list = timetable.findByExerciseType(et);
            if (list.isEmpty()) System.out.println("  No lessons found.");
            return list.isEmpty() ? null : list;
        }
        System.out.println("  Invalid option.");
        return null;
    }

    private int promptMonth() {
        System.out.println("  Months: 1 = Month 1 (Weeks 1-4), 2 = Month 2 (Weeks 5-8)");
        String raw = prompt("Enter month number (1 or 2)").trim();
        try {
            int m = Integer.parseInt(raw);
            if (m == 1 || m == 2) return m;
        } catch (NumberFormatException ignored) {}
        System.out.println("  Invalid month.");
        return -1;
    }
    private void preloadReviews() {
        List<Member> members = new ArrayList<>(registry.getAllMembers());
        List<Lesson> lessons = timetable.getAllLessons();

        String[] sampleReviews = {
                "Excellent class!",
                "Very enjoyable session",
                "Instructor was great",
                "Good workout",
                "Too intense but fun",
                "Loved it!",
                "Not bad",
                "Would join again"
        };

        int reviewCount = 0;

        for (Lesson lesson : lessons) {
            for (int i = 0; i < 2; i++) { // 2 reviews per lesson
                Member m = members.get((reviewCount + i) % members.size());

                Booking b = manager.book(m, lesson);

                if (b != null) {
                    manager.attendLesson(
                            b.getBookingId(),
                            sampleReviews[reviewCount % sampleReviews.length],
                            (reviewCount % 5) + 1 // rating 1–5
                    );
                    reviewCount++;
                }

                if (reviewCount >= 25) return;
            }
        }
    }

    private String prompt(String msg) {
        System.out.print("  > " + msg + ": ");
        return scanner.nextLine();
    }
}