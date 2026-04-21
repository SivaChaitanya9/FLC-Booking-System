package flc;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    public enum TimeSlot { MORNING, AFTERNOON, EVENING }

    private static final int MAX_CAPACITY = 4;

    private final String lessonId;
    private final ExerciseType exerciseType;
    private final String day;
    private final int weekNumber;
    private final int monthNumber;
    private final TimeSlot timeSlot;
    private final List<Member> bookedMembers = new ArrayList<>();

    public Lesson(String lessonId, ExerciseType exerciseType,
                  String day, int weekNumber, int monthNumber, TimeSlot timeSlot) {
        this.lessonId    = lessonId;
        this.exerciseType = exerciseType;
        this.day          = day;
        this.weekNumber   = weekNumber;
        this.monthNumber  = monthNumber;
        this.timeSlot     = timeSlot;
    }

    public String     getLessonId(){
        return lessonId;
    }
    public ExerciseType getExerciseType(){ return exerciseType; }
    public String getDay(){
        return day;
    }
    public int getWeekNumber(){
        return weekNumber;
    }
    public int getMonthNumber() {
        return monthNumber;
    }
    public TimeSlot getTimeSlot(){
        return timeSlot;
    }
    public int getAvailableSpaces() {
        return MAX_CAPACITY - bookedMembers.size();
    }
    public boolean isFull(){
        return bookedMembers.size() >= MAX_CAPACITY;
    }
    public List<Member> getBookedMembers() {
        return bookedMembers;
    }
    public boolean addMember(Member m) {
        if (isFull() || bookedMembers.contains(m)) return false;
        bookedMembers.add(m);
        return true;
    }

    public boolean removeMember(Member m) {
        return bookedMembers.remove(m);
    }

    public double getPrice() { return exerciseType.getPrice(); }

    @Override
    public String toString() {
        return String.format("[%s] Week%d %s %s %s | Price: £%.2f | Spaces: %d/%d",
                lessonId, weekNumber, day, timeSlot, exerciseType,
                getPrice(), bookedMembers.size(), MAX_CAPACITY);
    }
}