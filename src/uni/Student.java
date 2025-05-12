package uni;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Student class that extends User
 */
public class Student extends User {
    private String studentId;
    private LocalDate admissionDate;
    private String academicStatus; // "Active", "On Probation", "Graduated"
    private List<Enrollment> enrollments;

    /**
     * Constructor for Student class
     * 
     * @param userId         Unique identifier
     * @param username       Login username
     * @param password       User password
     * @param name           Full name
     * @param email          Email address
     * @param contactInfo    Contact information
     * @param studentId      Student ID number
     * @param admissionDate  Date of admission
     * @param academicStatus Current academic status
     */
    public Student(String userId, String username, String password, String name, String email, String contactInfo,
            String studentId, LocalDate admissionDate, String academicStatus) {
        super(userId, username, password, name, email, contactInfo);
        this.studentId = studentId;
        this.admissionDate = admissionDate;
        setAcademicStatus(academicStatus);
        this.enrollments = new ArrayList<>();
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        // Validate status
        if (!academicStatus.equals("Active") && !academicStatus.equals("On Probation")
                && !academicStatus.equals("Graduated")) {
            throw new IllegalArgumentException("Academic status must be 'Active', 'On Probation', or 'Graduated'");
        }
        this.academicStatus = academicStatus;
    }

    public List<Enrollment> getEnrollments() {
        return new ArrayList<>(enrollments); // Return a copy to preserve encapsulation
    }

    /**
     * Register for a course
     * 
     * @param courseOffering Course to register for
     * @return true if registration successful, false otherwise
     */
    public boolean registerForCourse(CourseOffering courseOffering) {
        // Check if already enrolled
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourseOffering().getOfferingId() == courseOffering.getOfferingId() &&
                    !enrollment.getStatus().equals("Withdrawn")) {
                return false; // Already enrolled
            }
        }

        // Check course capacity
        if (courseOffering.getEnrolledStudents() >= courseOffering.getCourse().getMaxCapacity()) {
            return false; // Course is full
        }

        // Check prerequisites
        for (Course prerequisite : courseOffering.getCourse().getPrerequisites()) {
            boolean hasCompleted = false;
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourseOffering().getCourse().getCourseId().equals(prerequisite.getCourseId()) &&
                        enrollment.getStatus().equals("Completed") &&
                        (enrollment.getGrade() != null && !enrollment.getGrade().equals("F"))) {
                    hasCompleted = true;
                    break;
                }
            }
            if (!hasCompleted) {
                return false; // Prerequisite not met
            }
        }

        // Create new enrollment
        Enrollment enrollment = new Enrollment(0, this, courseOffering, LocalDate.now(), null, "Enrolled");
        enrollments.add(enrollment);
        return true;
    }

    /**
     * Drop a course
     * 
     * @param enrollment Enrollment to withdraw from
     * @return true if drop successful, false otherwise
     */
    public boolean dropCourse(Enrollment enrollment) {
        if (!enrollments.contains(enrollment)) {
            return false; // Not enrolled in this course
        }

        if (enrollment.getStatus().equals("Completed")) {
            return false; // Can't drop completed course
        }

        enrollment.setStatus("Withdrawn");
        return true;
    }

    /**
     * Calculate GPA based on completed courses
     * 
     * @return GPA value
     */
    public double calculateGPA() {
        int totalCredits = 0;
        double totalGradePoints = 0;

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStatus().equals("Completed") && enrollment.getGrade() != null) {
                int creditHours = enrollment.getCourseOffering().getCourse().getCreditHours();
                double gradePoints = convertGradeToPoints(enrollment.getGrade());

                totalCredits += creditHours;
                totalGradePoints += (gradePoints * creditHours);
            }
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }

    /**
     * Helper method to convert letter grades to GPA points
     * 
     * @param grade Letter grade (A, B, C, D, F)
     * @return GPA points
     */
    private double convertGradeToPoints(String grade) {
        switch (grade) {
            case "A":
                return 4.0;
            case "A-":
                return 3.7;
            case "B+":
                return 3.3;
            case "B":
                return 3.0;
            case "B-":
                return 2.7;
            case "C+":
                return 2.3;
            case "C":
                return 2.0;
            case "C-":
                return 1.7;
            case "D+":
                return 1.3;
            case "D":
                return 1.0;
            case "F":
                return 0.0;
            default:
                return 0.0;
        }
    }

    @Override
    public String getUserType() {
        return "Student";
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", admissionDate=" + admissionDate +
                ", academicStatus='" + academicStatus + '\'' +
                "} " + super.toString();
    }
}