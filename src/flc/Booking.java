package flc;

public class Booking {
    private final String bookingId;
    private final Member member;
    private Lesson lesson;
    private BookingStatus status;
    private String review;
    private int rating;

    public Booking(String bookingId, Member member, Lesson lesson) {
        this.bookingId = bookingId;
        this.member    = member;
        this.lesson    = lesson;
        this.status    = BookingStatus.BOOKED;
        this.rating    = 0;
    }

    public String getBookingId(){
        return bookingId;
    }
    public Member getMember(){
        return member;
    }
    public Lesson getLesson(){
        return lesson;
    }
    public BookingStatus getStatus(){
        return status;
    }
    public int getRating(){
        return rating;
    }
    public void setLesson(Lesson lesson){
        this.lesson = lesson;
    }
    public void setStatus(BookingStatus s){
        this.status = s;
    }
    public void attend(String review, int rating) {
        this.review = review;
        this.rating = rating;
        this.status = BookingStatus.ATTENDED;
    }

    @Override
    public String toString() {
        return String.format("Booking[%s] Member:%s Lesson:%s Status:%s",
                bookingId, member.getName(), lesson.getLessonId(), status);
    }
}