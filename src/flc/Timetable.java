package flc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Timetable {
    private static final Object[][] SCHEDULE = {
            // ── WEEK 1 (Month 1) ─────────────────────────────────────
            {1,"Saturday", Lesson.TimeSlot.MORNING,   ExerciseType.YOGA},
            {1,"Saturday", Lesson.TimeSlot.AFTERNOON, ExerciseType.ZUMBA},
            {1,"Saturday", Lesson.TimeSlot.EVENING,   ExerciseType.BOX_FIT},
            {1,"Sunday",   Lesson.TimeSlot.MORNING,   ExerciseType.AQUACISE},
            {1,"Sunday",   Lesson.TimeSlot.AFTERNOON, ExerciseType.BODY_BLITZ},
            {1,"Sunday",   Lesson.TimeSlot.EVENING,   ExerciseType.YOGA},

            // ── WEEK 2 (Month 1) ─────────────────────────────────────
            {2,"Saturday", Lesson.TimeSlot.MORNING,   ExerciseType.ZUMBA},
            {2,"Saturday", Lesson.TimeSlot.AFTERNOON, ExerciseType.AQUACISE},
            {2,"Saturday", Lesson.TimeSlot.EVENING,   ExerciseType.YOGA},
            {2,"Sunday",   Lesson.TimeSlot.MORNING,   ExerciseType.BOX_FIT},
            {2,"Sunday",   Lesson.TimeSlot.AFTERNOON, ExerciseType.YOGA},
            {2,"Sunday",   Lesson.TimeSlot.EVENING,   ExerciseType.ZUMBA},

            // ── WEEK 3 (Month 1) ─────────────────────────────────────
            {3,"Saturday", Lesson.TimeSlot.MORNING,   ExerciseType.BODY_BLITZ},
            {3,"Saturday", Lesson.TimeSlot.AFTERNOON, ExerciseType.BOX_FIT},
            {3,"Saturday", Lesson.TimeSlot.EVENING,   ExerciseType.ZUMBA},
            {3,"Sunday",   Lesson.TimeSlot.MORNING,   ExerciseType.YOGA},
            {3,"Sunday",   Lesson.TimeSlot.AFTERNOON, ExerciseType.AQUACISE},
            {3,"Sunday",   Lesson.TimeSlot.EVENING,   ExerciseType.BOX_FIT},

            // ── WEEK 4 (Month 1) ─────────────────────────────────────
            {4,"Saturday", Lesson.TimeSlot.MORNING,   ExerciseType.AQUACISE},
            {4,"Saturday", Lesson.TimeSlot.AFTERNOON, ExerciseType.YOGA},
            {4,"Saturday", Lesson.TimeSlot.EVENING,   ExerciseType.BODY_BLITZ},
            {4,"Sunday",   Lesson.TimeSlot.MORNING,   ExerciseType.ZUMBA},
            {4,"Sunday",   Lesson.TimeSlot.AFTERNOON, ExerciseType.BOX_FIT},
            {4,"Sunday",   Lesson.TimeSlot.EVENING,   ExerciseType.AQUACISE},

            // ── WEEK 5 (Month 2) ─────────────────────────────────────
            {5,"Saturday", Lesson.TimeSlot.MORNING,   ExerciseType.YOGA},
            {5,"Saturday", Lesson.TimeSlot.AFTERNOON, ExerciseType.BODY_BLITZ},
            {5,"Saturday", Lesson.TimeSlot.EVENING,   ExerciseType.AQUACISE},
            {5,"Sunday",   Lesson.TimeSlot.MORNING,   ExerciseType.BOX_FIT},
            {5,"Sunday",   Lesson.TimeSlot.AFTERNOON, ExerciseType.ZUMBA},
            {5,"Sunday",   Lesson.TimeSlot.EVENING,   ExerciseType.YOGA},

            // ── WEEK 6 (Month 2) ─────────────────────────────────────
            {6,"Saturday", Lesson.TimeSlot.MORNING,   ExerciseType.ZUMBA},
            {6,"Saturday", Lesson.TimeSlot.AFTERNOON, ExerciseType.BOX_FIT},
            {6,"Saturday", Lesson.TimeSlot.EVENING,   ExerciseType.YOGA},
            {6,"Sunday",   Lesson.TimeSlot.MORNING,   ExerciseType.BODY_BLITZ},
            {6,"Sunday",   Lesson.TimeSlot.AFTERNOON, ExerciseType.AQUACISE},
            {6,"Sunday",   Lesson.TimeSlot.EVENING,   ExerciseType.BOX_FIT},

            // ── WEEK 7 (Month 2) ─────────────────────────────────────
            {7,"Saturday", Lesson.TimeSlot.MORNING,   ExerciseType.BOX_FIT},
            {7,"Saturday", Lesson.TimeSlot.AFTERNOON, ExerciseType.YOGA},
            {7,"Saturday", Lesson.TimeSlot.EVENING,   ExerciseType.ZUMBA},
            {7,"Sunday",   Lesson.TimeSlot.MORNING,   ExerciseType.AQUACISE},
            {7,"Sunday",   Lesson.TimeSlot.AFTERNOON, ExerciseType.BODY_BLITZ},
            {7,"Sunday",   Lesson.TimeSlot.EVENING,   ExerciseType.YOGA},

            // ── WEEK 8 (Month 2) ─────────────────────────────────────
            {8,"Saturday", Lesson.TimeSlot.MORNING,   ExerciseType.BODY_BLITZ},
            {8,"Saturday", Lesson.TimeSlot.AFTERNOON, ExerciseType.AQUACISE},
            {8,"Saturday", Lesson.TimeSlot.EVENING,   ExerciseType.BOX_FIT},
            {8,"Sunday",   Lesson.TimeSlot.MORNING,   ExerciseType.YOGA},
            {8,"Sunday",   Lesson.TimeSlot.AFTERNOON, ExerciseType.ZUMBA},
            {8,"Sunday",   Lesson.TimeSlot.EVENING,   ExerciseType.BODY_BLITZ},
    };

    private final List<Lesson> lessons = new ArrayList<>();

    public Timetable() {
        int id = 1;
        for (Object[] row : SCHEDULE) {
            int week          = (int) row[0];
            String day        = (String) row[1];
            Lesson.TimeSlot ts= (Lesson.TimeSlot) row[2];
            ExerciseType et   = (ExerciseType) row[3];
            int month         = (week <= 4) ? 1 : 2;
            String lessonId   = String.format("L%02d", id++);
            lessons.add(new Lesson(lessonId, et, day, week, month, ts));
        }
    }

    public List<Lesson> getAllLessons() { return lessons; }

    public Lesson findById(String id) {
        return lessons.stream()
                .filter(l -> l.getLessonId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    public List<Lesson> findByDay(String day) {
        return lessons.stream()
                .filter(l -> l.getDay().equalsIgnoreCase(day))
                .collect(Collectors.toList());
    }

    public List<Lesson> findByExerciseType(ExerciseType type) {
        return lessons.stream()
                .filter(l -> l.getExerciseType() == type)
                .collect(Collectors.toList());
    }

    public List<Lesson> findByMonth(int month) {
        return lessons.stream()
                .filter(l -> l.getMonthNumber() == month)
                .collect(Collectors.toList());
    }

    public static void printLessons(List<Lesson> list) {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("| ID     | Week | Day      | Time      | Exercise     | Price | Booked  | Spaces  |");
        System.out.println("--------------------------------------------------------------------------");
        for (Lesson l : list) {
            System.out.printf("| %-6s | %-4d | %-8s | %-9s | %-12s | £%-5.2f | %-7d | %-7d |%n",
                    l.getLessonId(), l.getWeekNumber(), l.getDay(),
                    l.getTimeSlot(), l.getExerciseType(),
                    l.getPrice(),
                    4 - l.getAvailableSpaces(),
                    l.getAvailableSpaces());
        }
    }
}