package uni;

import java.util.ArrayList;
import java.util.List;

/**
 * Faculty class that extends User
 */
public class Faculty extends User {
    private String facultyId;
    private Department department;
    private String expertise;
    private List<CourseOffering> coursesTeaching;

    /**
     * Constructor for Faculty class
     * 
     * @param userId      Unique identifier
     * @param username    Login username
     * @param password    User password
     * @param name        Full name
     * @param email       Email address
     * @param contactInfo Contact information
     * @param facultyId   Faculty ID
     * @param department  Department the faculty belongs to
     * @param expertise   Areas of expertise
     */
    public Faculty(String userId, String username, String password, String name, String email, String contactInfo,
            String facultyId, Department department, String expertise) {
        super(userId, username, password, name, email, contactInfo);
        this.facultyId = facultyId;
        this.department = department;
        this.expertise = expertise;
        this.coursesTeaching = new ArrayList<>();
    }

    // Getters and setters
    public String getFacultyId() {
        return facultyId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public List<CourseOffering> getCoursesTeaching() {
        return new ArrayList<>(coursesTeaching); // Return a copy to preserve encapsulation
    }

    /**
     * Assign a course to this faculty
     * 
     * @param courseOffering Course to assign
     * @return true if assignment successful, false otherwise
     */
    public boolean assignCourse(CourseOffering courseOffering) {
        // Check if already teaching this course
        for (CourseOffering offering : coursesTeaching) {
            if (offering.getOfferingId() == courseOffering.getOfferingId()) {
                return false; // Already assigned to this course
            }
        }

        coursesTeaching.add(courseOffering);
        courseOffering.setFaculty(this);
        return true;
    }

    /**
     * Remove a course from this faculty
     * 
     * @param courseOffering Course to remove
     * @return true if removal successful, false otherwise
     */
    public boolean removeCourse(CourseOffering courseOffering) {
        boolean result = coursesTeaching.remove(courseOffering);
        if (result) {
            courseOffering.setFaculty(null);
        }
        return result;
    }

    /**
     * Assign grade to a student enrollment
     * 
     * @param enrollment Student enrollment
     * @param grade      Grade to assign
     * @return true if grade assignment successful, false otherwise
     */
    public boolean assignGrade(Enrollment enrollment, String grade) {
        // Check if this faculty teaches the course
        CourseOffering offering = enrollment.getCourseOffering();
        if (!coursesTeaching.contains(offering)) {
            return false; // Not teaching this course
        }

        // Validate grade format
        if (!isValidGrade(grade)) {
            return false; // Invalid grade
        }

        enrollment.setGrade(grade);
        return true;
    }

    /**
     * Helper method to validate grade format
     * 
     * @param grade Grade to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidGrade(String grade) {
        return grade != null &&
                (grade.equals("A") || grade.equals("A-") ||
                        grade.equals("B+") || grade.equals("B") || grade.equals("B-") ||
                        grade.equals("C+") || grade.equals("C") || grade.equals("C-") ||
                        grade.equals("D+") || grade.equals("D") || grade.equals("F"));
    }

    /**
     * Get list of students enrolled in a specific course
     * 
     * @param courseOffering Course to check
     * @return List of students
     */
    public List<Student> getStudentRoster(CourseOffering courseOffering) {
        if (!coursesTeaching.contains(courseOffering)) {
            return new ArrayList<>(); // Not teaching this course
        }

        List<Student> students = new ArrayList<>();
        for (Enrollment enrollment : courseOffering.getEnrollments()) {
            if (enrollment.getStatus().equals("Enrolled")) {
                students.add(enrollment.getStudent());
            }
        }
        return students;
    }

    @Override
    public String getUserType() {
        return "Faculty";
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "facultyId='" + facultyId + '\'' +
                ", department=" + (department != null ? department.getName() : "none") +
                ", expertise='" + expertise + '\'' +
                ", coursesTeaching=" + coursesTeaching.size() +
                "} " + super.toString();
    }
}