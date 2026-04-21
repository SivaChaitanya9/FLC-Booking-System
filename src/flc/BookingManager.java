package flc;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookingManager {

    private final Map<String, Booking> bookings = new LinkedHashMap<>();
    private int bookingCounter = 1;

    public Booking book(Member member, Lesson lesson) {
        for (Booking b : bookings.values()) {
            if (b.getMember().equals(member)
                    && b.getLesson().equals(lesson)
                    && b.getStatus() != BookingStatus.CANCELLED) {
                System.out.println("  Duplicate booking not allowed.");
                return null;
            }
        }
        for (Booking b : bookings.values()) {
            if (b.getMember().equals(member)
                    && b.getLesson().getDay().equals(lesson.getDay())
                    && b.getLesson().getTimeSlot() == lesson.getTimeSlot()
                    && b.getStatus() != BookingStatus.CANCELLED
                    && b.getStatus() != BookingStatus.ATTENDED) {
                System.out.println(" Time conflict: you already have a booking on" + lesson.getDay() + " " + lesson.getTimeSlot());
                return null;
            }
        }
        if (lesson.isFull()) {
            System.out.println("  Lesson is full.");
            return null;
        }
        String id = String.format("B%d", bookingCounter++);
        lesson.addMember(member);
        Booking booking = new Booking(id, member, lesson);
        bookings.put(id, booking);

        return booking;
    }

    public boolean changeBooking(String bookingId, Lesson newLesson) {
        Booking b = bookings.get(bookingId);
        if (b == null || b.getStatus() == BookingStatus.CANCELLED
                || b.getStatus() == BookingStatus.ATTENDED) return false;
        if (newLesson.isFull()) return false;
        for (Booking existing : bookings.values()) {
            if (!existing.getBookingId().equals(bookingId)
                    && existing.getMember().equals(b.getMember())
                    && existing.getLesson().equals(newLesson)
                    && existing.getStatus() != BookingStatus.CANCELLED) {
                return false;
            }
        }
        b.getLesson().removeMember(b.getMember());
        newLesson.addMember(b.getMember());
        b.setLesson(newLesson);
        b.setStatus(BookingStatus.CHANGED);
        return true;
    }
    public void printBookingsForMember(Member m) {
        List<Booking> list = getBookingsForMember(m);

        if (list.isEmpty()) {
            System.out.println("  No bookings found.");
            return;
        }

        System.out.println("\n-------------------------------------------------------------");
        System.out.println("| Booking ID | Lesson ID | Day      | Time      | Status     |");
        System.out.println("-------------------------------------------------------------");

        for (Booking b : list) {
            Lesson l = b.getLesson();
            System.out.printf("| %-10s | %-9s | %-8s | %-9s | %-10s |%n",
                    b.getBookingId(),
                    l.getLessonId(),
                    l.getDay(),
                    l.getTimeSlot(),
                    b.getStatus());
        }
    }
    public boolean cancelBooking(String bookingId) {
        Booking b = bookings.get(bookingId);
        if (b == null || b.getStatus() == BookingStatus.CANCELLED
                || b.getStatus() == BookingStatus.ATTENDED) return false;
        b.getLesson().removeMember(b.getMember());
        b.setStatus(BookingStatus.CANCELLED);
        return true;
    }

    public boolean attendLesson(String bookingId, String review, int rating) {
        Booking b = bookings.get(bookingId);
        if (b == null || b.getStatus() == BookingStatus.CANCELLED
                || b.getStatus() == BookingStatus.ATTENDED) return false;
        b.attend(review, rating);
        return true;
    }
    public Booking findById(String id){
        return bookings.get(id);
    }
    public Collection<Booking> getAllBookings(){
        return bookings.values();
    }

    public List<Booking> getBookingsForMember(Member m) {
        return bookings.values().stream()
                .filter(b -> b.getMember().equals(m))
                .collect(Collectors.toList());
    }

    public List<Booking> getAttendedBookingsForLesson(Lesson l) {
        return bookings.values().stream()
                .filter(b -> b.getLesson().equals(l)
                        && b.getStatus() == BookingStatus.ATTENDED)
                .collect(Collectors.toList());
    }

    public void printMonthlyLessonReport(int month, Timetable timetable) {
        List<Lesson> monthLessons = timetable.findByMonth(month);
        System.out.println("\n=====================================================================================");
        System.out.printf( "                 MONTHLY LESSON REPORT — Month %d      %n", month);
        System.out.println("======================================================================================");
        System.out.println(" ID    |Week    |Day   |Time   |Exercise   |Attended  |Avg Rating");

        for (Lesson l : monthLessons) {
            List<Booking> attended = getAttendedBookingsForLesson(l);
            int count = attended.size();
            double avgRating = attended.stream()
                    .mapToInt(Booking::getRating)
                    .average().orElse(0.0);
            System.out.printf(
                    " %-6s  %-4d  %-8s  %-9s  %-12s  %-9d  %-12s %n",
                    l.getLessonId(), l.getWeekNumber(), l.getDay(),
                    l.getTimeSlot(), l.getExerciseType(),
                    count,
                    count == 0 ? "N/A" : String.format("%.2f / 5", avgRating));
        }
    }
    public void printMonthlyChampionReport(int month, Timetable timetable) {
        List<Lesson> monthLessons = timetable.findByMonth(month);

        Map<ExerciseType, Double> incomeMap = new LinkedHashMap<>();
        for (ExerciseType et : ExerciseType.values()) incomeMap.put(et, 0.0);

        for (Lesson l : monthLessons) {
            List<Booking> attended = getAttendedBookingsForLesson(l);
            double income = attended.size() * l.getPrice();
            incomeMap.merge(l.getExerciseType(), income, Double::sum);
        }

        ExerciseType champion = incomeMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);

        System.out.println("\n===========================================================");
        System.out.printf( "   MONTHLY CHAMPION EXERCISE TYPE REPORT — Month %d %n", month);
        System.out.println("=============================================================");
        System.out.println("Exercise Type| Total Income ");

        for (Map.Entry<ExerciseType, Double> e : incomeMap.entrySet()) {
            System.out.printf(" %-16s  %-13.2f  %n",e.getKey(), e.getValue());
        }
        if (champion != null)
            System.out.printf("  Champion: %s with highest income in Month %d%n%n", champion, month);
    }
}