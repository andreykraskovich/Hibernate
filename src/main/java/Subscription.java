import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Subscriptions")
public class Subscription {
    @EmbeddedId
    private Id id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", updatable = false, insertable = false)
    private Students student;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", updatable = false, insertable = false)
    private Course course;
    @Column(name = "subscription_date")
    private Date subscriptionDate;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

@Embeddable
public static class Id implements Serializable {


    @Column(name = "student_id")
    private int studentId;


    @Column(name = "course_id")
    private int courseId;

    public Id() {
    }

    public Id(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Id that = (Id) o;
        return studentId == that.studentId &&
                courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }
}
}