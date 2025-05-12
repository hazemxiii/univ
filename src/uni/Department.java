package uni;

import java.util.ArrayList;
import java.util.List;

/**
 * Department class for university departments
 */
public class Department {
    private String departmentId;
    private String name;
    private List<Faculty> faculty;
    private List<Course> offeredCourses;

    /**
     * Constructor for Department class
     * 
     * @param departmentId Department ID
     * @param name         Department name
     */
    public Department(String departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
        this.faculty = new ArrayList<>();
        this.offeredCourses = new ArrayList<>();
    }

    // Getters and setters
    public String getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Faculty> getFaculty() {
        return new ArrayList<>(faculty); // Return a copy to preserve encapsulation
    }

    public List<Course> getOfferedCourses() {
        return new ArrayList<>(offeredCourses); // Return a copy to preserve encapsulation
    }

    /**
     * Add a faculty member to the department
     * 
     * @param facultyMember Faculty to add
     * @return true if addition successful, false otherwise
     */
    public boolean addFaculty(Faculty facultyMember) {
        if (faculty.contains(facultyMember)) {
            return false; // Already in department
        }

        faculty.add(facultyMember);
        return true;
    }

    /**
     * Remove a faculty member from the department
     * 
     * @param facultyMember Faculty to remove
     * @return true if removal successful, false otherwise
     */
    public boolean removeFaculty(Faculty facultyMember) {
        return faculty.remove(facultyMember);
    }

    /**
     * Add a course to the department's offerings
     * 
     * @param course Course to add
     * @return true if addition successful, false otherwise
     */
    public boolean addCourse(Course course) {
        if (offeredCourses.contains(course)) {
            return false; // Already offered
        }

        offeredCourses.add(course);
        return true;
    }

    /**
     * Remove a course from the department's offerings
     * 
     * @param course Course to remove
     * @return true if removal successful, false otherwise
     */
    public boolean removeCourse(Course course) {
        return offeredCourses.remove(course);
    }

    /**
     * Get faculty list filtered by expertise
     * 
     * @param expertise Expertise to filter by
     * @return List of faculty with matching expertise
     */
    public List<Faculty> getFacultyByExpertise(String expertise) {
        List<Faculty> result = new ArrayList<>();

        for (Faculty facultyMember : faculty) {
            if (facultyMember.getExpertise().contains(expertise)) {
                result.add(facultyMember);
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId='" + departmentId + '\'' +
                ", name='" + name + '\'' +
                ", faculty=" + faculty.size() +
                ", offeredCourses=" + offeredCourses.size() +
                '}';
    }
}