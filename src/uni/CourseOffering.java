package uni;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseOffering class for instances of courses in specific semesters
 */
public class CourseOffering {
    private int offeringId;
    private Course course;
    private String semester;
    private int year;
    private Faculty faculty;
    private List<Enrollment> enrollments;
    private List<CourseSchedule> schedules;

    /**
     * Constructor for CourseOffering class
     * 
     * @param offeringId Unique offering ID
     * @param course     Course being offered
     * @param semester   Semester (e.g., "Fall", "Spring", "Summer")
     * @param year       Academic year
     */
    public CourseOffering(int offeringId, Course course, String semester, int year) {
        this.offeringId = offeringId;
        this.course = course;
        this.semester = semester;
        this.year = year;
        this.enrollments = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    // Getters and setters
    public int getOfferingId() {
        return offeringId;
    }

    public Course getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }

    public int getYear() {
        return year;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public List<Enrollment> getEnrollments() {
        return new ArrayList<>(enrollments); // Return a copy to preserve encapsulation
    }

    public List<CourseSchedule> getSchedules() {
        return new ArrayList<>(schedules); // Return a copy to preserve encapsulation
    }

    /**
     * Add a schedule to this course offering
     * 
     * @param schedule Schedule to add
     * @return true if addition successful, false otherwise
     */
    public boolean addSchedule(CourseSchedule schedule) {
        // Check for time conflicts
        for (CourseSchedule existing : schedules) {
            if (existing.conflictsWith(schedule)) {
                return false; // Schedule conflict
            }
        }

        schedules.add(schedule);
        return true;
    }

    /**
     * Remove a schedule from this course offering
     * 
     * @param schedule Schedule to remove
     * @return true if removal successful, false otherwise
     */
    public boolean removeSchedule(CourseSchedule schedule) {
        return schedules.remove(schedule);
    }

    /**
     * Add an enrollment to this course offering
     * 
     * @param enrollment Enrollment to add
     * @return true if addition successful, false otherwise
     */
    public boolean addEnrollment(Enrollment enrollment) {
        if (getEnrolledStudents() >= course.getMaxCapacity()) {
            return false; // Course is full
        }

        enrollments.add(enrollment);
        return true;
    }

    /**
     * Get the number of students currently enrolled
     * 
     * @return Number of enrolled students
     */
    public int getEnrolledStudents() {
        int count = 0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStatus().equals("Enrolled")) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get the number of available seats
     * 
     * @return Number of available seats
     */
    public int getAvailableSeats() {
        return course.getMaxCapacity() - getEnrolledStudents();
    }

    /**
     * Get enrollment for a specific student
     * 
     * @param student Student to check
     * @return Enrollment object if student is enrolled, null otherwise
     */
    public Enrollment getEnrollmentForStudent(Student student) {
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getStudentId().equals(student.getStudentId()) &&
                    enrollment.getStatus().equals("Enrolled")) {
                return enrollment;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "CourseOffering{" +
                "offeringId=" + offeringId +
                ", course=" + course.getCourseId() +
                ", semester='" + semester + '\'' +
                ", year=" + year +
                ", faculty=" + (faculty != null ? faculty.getName() : "unassigned") +
                ", enrollments=" + getEnrolledStudents() + "/" + course.getMaxCapacity() +
                ", schedules=" + schedules.size() +
                '}';
    }
}