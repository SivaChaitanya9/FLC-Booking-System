import flc.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class FLCTest {

    private MemberRegistry registry;
    private Timetable timetable;
    private BookingManager manager;
    private Member alice;
    private Member bob;
    private Lesson yogaLesson;

    @BeforeEach
    void setUp() {
        registry   = new MemberRegistry();
        timetable  = new Timetable();
        manager    = new BookingManager();
        alice      = registry.findById("M1");
        bob        = registry.findById("M2");
        yogaLesson = timetable.getAllLessons().get(0);
    }
    @Test
    void testTimetableSize() {
        assertEquals(48, timetable.getAllLessons().size(),
                "Timetable must have 48 lessons (8 weekends × 6 lessons)");
    }

    @Test
    void testBookLesson() {
        Booking b = manager.book(alice, yogaLesson);
        assertNotNull(b, "Booking should be created");
        assertEquals(BookingStatus.BOOKED, b.getStatus());
        assertEquals(alice, b.getMember());
        assertEquals(yogaLesson, b.getLesson());
    }

    @Test
    void testDuplicateBookingRejected() {
        Booking first = manager.book(alice, yogaLesson);
        Booking duplicate = manager.book(alice, yogaLesson);
        assertNotNull(first);
        assertNull(duplicate);
    }

    @Test
    void testLessonCapacity() {
        Member[] members = {
            new Member("T001","Test1"), new Member("T002","Test2"),
            new Member("T003","Test3"), new Member("T004","Test4")
        };
        for (Member m : members) manager.book(m, yogaLesson);

        assertTrue(yogaLesson.isFull(), "Lesson should be full after 4 bookings");
        Booking extra = manager.book(new Member("T005","Test5"), yogaLesson);
        assertNull(extra, "5th booking must be rejected");
    }

    @Test
    void testAttendLesson() {
        Booking b = manager.book(alice, yogaLesson);
        assertNotNull(b);
        boolean ok = manager.attendLesson(b.getBookingId(), "Great session!", 5);
        assertTrue(ok, "Attend should return true");
        assertEquals(BookingStatus.ATTENDED, b.getStatus());
        assertEquals(5, b.getRating());

    }

    @Test
    void testCancelReleasesSpace() {
        Booking b = manager.book(alice, yogaLesson);
        int spacesBefore = yogaLesson.getAvailableSpaces();
        manager.cancelBooking(b.getBookingId());
        assertEquals(spacesBefore + 1, yogaLesson.getAvailableSpaces(),
                "Cancellation should free one space");
        assertEquals(BookingStatus.CANCELLED, b.getStatus());
    }

    @Test
    void testExercisePrices() {
        assertEquals(12.00, ExerciseType.YOGA.getPrice(),     0.001);
        assertEquals(10.00, ExerciseType.ZUMBA.getPrice(),    0.001);
        assertEquals( 8.00, ExerciseType.AQUACISE.getPrice(), 0.001);
        assertEquals(15.00, ExerciseType.BOX_FIT.getPrice(),  0.001);
        assertEquals(11.00, ExerciseType.BODY_BLITZ.getPrice(),0.001);
    }
}