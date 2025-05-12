package uni;

import java.util.ArrayList;
import java.util.List;

/**
 * Course class for university courses
 */
public class Course {
    private String courseId;
    private String title;
    private String description;
    private int creditHours;
    private int maxCapacity;
    private Department department;
    private List<Course> prerequisites;

    /**
     * Constructor for Course class
     * 
     * @param courseId    Course ID
     * @param title       Course title
     * @param description Course description
     * @param creditHours Number of credit hours
     * @param maxCapacity Maximum enrollment capacity
     * @param department  Department offering the course
     */
    public Course(String courseId, String title, String description, int creditHours, int maxCapacity,
            Department department) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.creditHours = creditHours;
        this.maxCapacity = maxCapacity;
        this.department = department;
        this.prerequisites = new ArrayList<>();
    }

    // Getters and setters
    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        if (creditHours <= 0) {
            throw new IllegalArgumentException("Credit hours must be positive");
        }
        this.creditHours = creditHours;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Max capacity must be positive");
        }
        this.maxCapacity = maxCapacity;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Course> getPrerequisites() {
        return new ArrayList<>(prerequisites); // Return a copy to preserve encapsulation
    }

    /**
     * Add a prerequisite course
     * 
     * @param course Prerequisite course
     * @return true if addition successful, false otherwise
     */
    public boolean addPrerequisite(Course course) {
        // Check for circular dependency
        if (course.equals(this) || course.hasPrerequisite(this)) {
            return false; // Circular dependency detected
        }

        if (prerequisites.contains(course)) {
            return false; // Already a prerequisite
        }

        prerequisites.add(course);
        return true;
    }

    /**
     * Remove a prerequisite course
     * 
     * @param course Prerequisite course to remove
     * @return true if removal successful, false otherwise
     */
    public boolean removePrerequisite(Course course) {
        return prerequisites.remove(course);
    }

    /**
     * Check if a course is a prerequisite (direct or indirect)
     * 
     * @param course Course to check
     * @return true if course is a prerequisite, false otherwise
     */
    public boolean hasPrerequisite(Course course) {
        // Direct prerequisite
        if (prerequisites.contains(course)) {
            return true;
        }

        // Indirect prerequisite (recursive check)
        for (Course prereq : prerequisites) {
            if (prereq.hasPrerequisite(course)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course course = (Course) o;
        return courseId.equals(course.courseId);
    }

    @Override
    public int hashCode() {
        return courseId.hashCode();
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", title='" + title + '\'' +
                ", creditHours=" + creditHours +
                ", maxCapacity=" + maxCapacity +
                ", department=" + (department != null ? department.getName() : "none") +
                ", prerequisites=" + prerequisites.size() +
                '}';
    }
}