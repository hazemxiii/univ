package uni;

import java.time.LocalDate;

/**
 * Enrollment class for student course enrollments
 */
public class Enrollment {
    private int enrollmentId;
    private Student student;
    private CourseOffering courseOffering;
    private LocalDate enrollmentDate;
    private String grade;
    private String status; // "Enrolled", "Withdrawn", "Completed"

    /**
     * Constructor for Enrollment class
     * 
     * @param enrollmentId   Enrollment ID
     * @param student        Student enrolled
     * @param courseOffering Course offering
     * @param enrollmentDate Date of enrollment
     * @param grade          Grade received (may be null)
     * @param status         Enrollment status
     */
    public Enrollment(int enrollmentId, Student student, CourseOffering courseOffering,
            LocalDate enrollmentDate, String grade, String status) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.courseOffering = courseOffering;
        this.enrollmentDate = enrollmentDate;
        this.grade = grade;
        setStatus(status);
    }

    // Getters and setters
    public int getEnrollmentId() {
        return enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public CourseOffering getCourseOffering() {
        return courseOffering;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
        // If grade is set, update status to Completed
        if (grade != null && !grade.isEmpty()) {
            this.status = "Completed";
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        // Validate status
        if (!status.equals("Enrolled") && !status.equals("Withdrawn") && !status.equals("Completed")) {
            throw new IllegalArgumentException("Status must be 'Enrolled', 'Withdrawn', or 'Completed'");
        }
        this.status = status;
    }

    /**
     * Withdraw from course
     * 
     * @return true if withdrawal successful, false otherwise
     */
    public boolean withdraw() {
        if (status.equals("Completed")) {
            return false; // Can't withdraw from completed course
        }

        status = "Withdrawn";
        return true;
    }

    /**
     * Mark enrollment as completed
     * 
     * @param finalGrade Final grade for the course
     * @return true if completion successful, false otherwise
     */
    public boolean complete(String finalGrade) {
        if (status.equals("Withdrawn")) {
            return false; // Can't complete withdrawn course
        }

        grade = finalGrade;
        status = "Completed";
        return true;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", student=" + student.getStudentId() +
                ", course=" + courseOffering.getCourse().getCourseId() +
                ", enrollmentDate=" + enrollmentDate +
                ", grade='" + (grade != null ? grade : "Not graded") + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}