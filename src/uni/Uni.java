package uni;

import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class for University Management System
 * Serves as the entry point and handles authentication and navigation
 */
public class Uni {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static FileManager fileManager = new FileManager();
    private static University university = new University("Alexandria University");

    public static void main(String[] args) {
        // Initialize the system
        initializeSystem();

        // Main application loop
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                // Not logged in - show authentication menu
                showAuthMenu();
            } else {
                // Logged in - show appropriate dashboard based on user role
                showDashboard();
            }
        }

        scanner.close();
        System.out.println("Thank you for using the University Management System!");
    }

    /**
     * Initialize the system by loading all data from files
     */
    private static void initializeSystem() {
        try {
            System.out.println("Initializing University Management System...");

            // Load all necessary data from files
            fileManager.loadUsers(university);
            fileManager.loadDepartments(university);
            fileManager.loadCourses(university);
            fileManager.loadEnrollments(university);

            System.out.println("System initialized successfully!");
        } catch (IOException e) {
            System.out.println("Error initializing system: " + e.getMessage());
            System.out.println("Creating new data files...");

            // Create initial admin user if no data exists
            createInitialAdmin();
        }
    }

    /**
     * Create initial admin user if no users exist in the system
     */
    private static void createInitialAdmin() {
        try {
            SystemAdmin admin = new SystemAdmin("admin", "admin123", "System", "Administrator",
                    "admin@alexandria.edu", "123-456-7890");
            university.addUser(admin);
            fileManager.saveUsers(university);
            System.out.println("Initial admin created with username: 'admin' and password: 'admin123'");
        } catch (IOException e) {
            System.out.println("Error creating initial admin: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Show authentication menu for login and account recovery
     */
    private static void showAuthMenu() {
        System.out.println("\n===== Alexandria University Management System =====");
        System.out.println("1. Login");
        System.out.println("2. Reset Password");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                resetPassword();
                break;
            case 3:
                System.out.println("Exiting system. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Handle user login process
     */
    private static void login() {
        System.out.println("\n===== Login =====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Validate credentials
        User user = university.authenticateUser(username, password);

        if (user != null) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + user.getName() + "!");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    /**
     * Handle password reset functionality
     */
    private static void resetPassword() {
        System.out.println("\n===== Password Reset =====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter email address: ");
        String email = scanner.nextLine();

        User user = university.findUserByUsernameAndEmail(username, email);

        if (user != null) {
            System.out.print("Enter new password (minimum 6 characters): ");
            String newPassword = scanner.nextLine();

            if (newPassword.length() >= 6) {
                user.setPassword(newPassword);
                try {
                    fileManager.saveUsers(university);
                    System.out.println("Password reset successful!");
                } catch (IOException e) {
                    System.out.println("Error saving new password: " + e.getMessage());
                }
            } else {
                System.out.println("Password must be at least 6 characters long.");
            }
        } else {
            System.out.println("User not found or email doesn't match.");
        }
    }

    /**
     * Show the appropriate dashboard based on user role
     */
    private static void showDashboard() {
        if (currentUser instanceof Student) {
            showStudentDashboard((Student) currentUser);
        } else if (currentUser instanceof Faculty) {
            showFacultyDashboard((Faculty) currentUser);
        } else if (currentUser instanceof AdminStaff) {
            showAdminDashboard((AdminStaff) currentUser);
        } else if (currentUser instanceof SystemAdmin) {
            showSystemAdminDashboard((SystemAdmin) currentUser);
        }
    }

    /**
     * Show student dashboard with student-specific options
     */
    private static void showStudentDashboard(Student student) {
        boolean dashboardActive = true;

        while (dashboardActive) {
            System.out.println("\n===== Student Dashboard =====");
            System.out.println("Welcome, " + student.getName() + " (ID: " + student.getUserId() + ")");
            System.out.println("1. View Profile");
            System.out.println("2. Register for Courses");
            System.out.println("3. View Enrolled Courses");
            System.out.println("4. View Grades and GPA");
            System.out.println("5. Drop a Course");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewStudentProfile(student);
                    break;
                case 2:
                    registerForCourses(student);
                    break;
                case 3:
                    viewEnrolledCourses(student);
                    break;
                case 4:
                    viewGradesAndGPA(student);
                    break;
                case 5:
                    dropCourse(student);
                    break;
                case 6:
                    changePassword();
                    break;
                case 7:
                    currentUser = null;
                    dashboardActive = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Show faculty dashboard with faculty-specific options
     */
    private static void showFacultyDashboard(Faculty faculty) {
        boolean dashboardActive = true;

        while (dashboardActive) {
            System.out.println("\n===== Faculty Dashboard =====");
            System.out.println("Welcome, Professor " + faculty.getName() + " (ID: " + faculty.getUserId() + ")");
            System.out.println("1. View Profile");
            System.out.println("2. View Teaching Courses");
            System.out.println("3. Manage Course (View/Add Students, Grades)");
            System.out.println("4. Set Office Hours");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewFacultyProfile(faculty);
                    break;
                case 2:
                    viewTeachingCourses(faculty);
                    break;
                case 3:
                    manageCourse(faculty);
                    break;
                case 4:
                    setOfficeHours(faculty);
                    break;
                case 5:
                    changePassword();
                    break;
                case 6:
                    currentUser = null;
                    dashboardActive = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Show admin dashboard with administrative options
     */
    private static void showAdminDashboard(AdminStaff admin) {
        boolean dashboardActive = true;

        while (dashboardActive) {
            System.out.println("\n===== Admin Dashboard =====");
            System.out.println("Welcome, " + admin.getName() + " (ID: " + admin.getUserId() + ")");
            System.out.println("1. View Profile");
            System.out.println("2. Register New Student");
            System.out.println("3. Manage Courses");
            System.out.println("4. Manage Faculty");
            System.out.println("5. Generate Reports");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewAdminProfile(admin);
                    break;
                case 2:
                    registerNewStudent();
                    break;
                case 3:
                    manageCourses();
                    break;
                case 4:
                    manageFaculty();
                    break;
                case 5:
                    generateReports();
                    break;
                case 6:
                    changePassword();
                    break;
                case 7:
                    currentUser = null;
                    dashboardActive = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Show system admin dashboard with system-wide options
     */
    private static void showSystemAdminDashboard(SystemAdmin admin) {
        boolean dashboardActive = true;

        while (dashboardActive) {
            System.out.println("\n===== System Admin Dashboard =====");
            System.out.println("Welcome, System Admin " + admin.getName() + " (ID: " + admin.getUserId() + ")");
            System.out.println("1. View Profile");
            System.out.println("2. Create User Account");
            System.out.println("3. Create Department");
            System.out.println("4. Backup System Data");
            System.out.println("5. Restore System Data");
            System.out.println("6. Manage System Settings");
            System.out.println("7. Change Password");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewSystemAdminProfile(admin);
                    break;
                case 2:
                    createUserAccount();
                    break;
                case 3:
                    createDepartment();
                    break;
                case 4:
                    backupSystemData();
                    break;
                case 5:
                    restoreSystemData();
                    break;
                case 6:
                    manageSystemSettings();
                    break;
                case 7:
                    changePassword();
                    break;
                case 8:
                    currentUser = null;
                    dashboardActive = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Student dashboard methods
    private static void viewStudentProfile(Student student) {
        System.out.println("\n===== Student Profile =====");
        System.out.println("ID: " + student.getUserId());
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Contact: " + student.getContactInfo());
        System.out.println("Admission Date: " + student.getAdmissionDate());
        System.out.println("Academic Status: " + student.getAcademicStatus());
        System.out.println("Current GPA: " + student.calculateGPA());
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void registerForCourses(Student student) {
        System.out.println("\n===== Course Registration =====");
        List<Course> availableCourses = university.getAvailableCoursesForStudent(student);

        if (availableCourses.isEmpty()) {
            System.out.println("No available courses for registration at this time.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Available Courses:");
        for (int i = 0; i < availableCourses.size(); i++) {
            Course course = availableCourses.get(i);
            System.out.println((i + 1) + ". " + course.getCourseId() + " - " + course.getTitle() +
                    " (" + course.getCreditHours() + " credits)");
        }

        System.out.print("Enter course number to register (0 to cancel): ");
        int choice = getIntInput();

        if (choice > 0 && choice <= availableCourses.size()) {
            Course selectedCourse = availableCourses.get(choice - 1);
            try {
                if (student.registerForCourse(selectedCourse)) {
                    fileManager.saveEnrollments(university);
                    System.out.println("Successfully registered for " + selectedCourse.getTitle());
                } else {
                    System.out.println("Registration failed. Check prerequisites or enrollment capacity.");
                }
            } catch (IOException e) {
                System.out.println("Error saving enrollment: " + e.getMessage());
            }
        } else if (choice != 0) {
            System.out.println("Invalid course number.");
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void viewEnrolledCourses(Student student) {
        System.out.println("\n===== Enrolled Courses =====");
        List<Enrollment> enrollments = student.getEnrollments();

        if (enrollments.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
        } else {
            System.out.println("Currently enrolled courses:");
            for (Enrollment enrollment : enrollments) {
                Course course = enrollment.getCourse();
                System.out.println("- " + course.getCourseId() + " - " + course.getTitle() +
                        " (" + course.getCreditHours() + " credits)");
                System.out.println("  Instructor: "
                        + (course.getInstructor() != null ? course.getInstructor().getName() : "Not assigned"));
                System.out.println("  Schedule: " + course.getSchedule());
            }
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void viewGradesAndGPA(Student student) {
        System.out.println("\n===== Grades and GPA =====");
        List<Enrollment> enrollments = student.getEnrollments();

        if (enrollments.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
        } else {
            System.out.println("Course Grades:");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-10s %-25s %-10s\n", "Course ID", "Course Title", "Grade");
            System.out.println("--------------------------------------------------");

            for (Enrollment enrollment : enrollments) {
                Course course = enrollment.getCourse();
                System.out.printf("%-10s %-25s %-10s\n",
                        course.getCourseId(),
                        course.getTitle(),
                        enrollment.getGrade() != null ? enrollment.getGrade() : "Not graded");
            }

            System.out.println("--------------------------------------------------");
            System.out.println("Current GPA: " + student.calculateGPA());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void dropCourse(Student student) {
        System.out.println("\n===== Drop Course =====");
        List<Enrollment> enrollments = student.getEnrollments();

        if (enrollments.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
        } else {
            System.out.println("Your enrolled courses:");
            for (int i = 0; i < enrollments.size(); i++) {
                Course course = enrollments.get(i).getCourse();
                System.out.println((i + 1) + ". " + course.getCourseId() + " - " + course.getTitle());
            }

            System.out.print("Enter course number to drop (0 to cancel): ");
            int choice = getIntInput();

            if (choice > 0 && choice <= enrollments.size()) {
                Enrollment selectedEnrollment = enrollments.get(choice - 1);
                try {
                    student.dropCourse(selectedEnrollment.getCourse());
                    fileManager.saveEnrollments(university);
                    System.out.println("Course dropped successfully.");
                } catch (IOException e) {
                    System.out.println("Error saving changes: " + e.getMessage());
                }
            } else if (choice != 0) {
                System.out.println("Invalid course number.");
            }
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    // Faculty dashboard methods
    private static void viewFacultyProfile(Faculty faculty) {
        System.out.println("\n===== Faculty Profile =====");
        System.out.println("ID: " + faculty.getUserId());
        System.out.println("Name: " + faculty.getName());
        System.out.println("Email: " + faculty.getEmail());
        System.out.println("Contact: " + faculty.getContactInfo());
        System.out.println("Department: "
                + (faculty.getDepartment() != null ? faculty.getDepartment().getName() : "Not assigned"));
        System.out.println("Expertise: " + faculty.getExpertise());
        System.out.println("Office Hours: " + faculty.getOfficeHours());
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void viewTeachingCourses(Faculty faculty) {
        System.out.println("\n===== Teaching Courses =====");
        List<Course> teachingCourses = faculty.getCoursesTeaching();

        if (teachingCourses.isEmpty()) {
            System.out.println("You are not teaching any courses currently.");
        } else {
            System.out.println("Current teaching assignments:");
            for (Course course : teachingCourses) {
                System.out.println("- " + course.getCourseId() + " - " + course.getTitle());
                System.out.println("  Schedule: " + course.getSchedule());
                System.out.println("  Enrolled Students: " + course.getEnrolledStudents().size() +
                        "/" + course.getMaxCapacity());
            }
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void manageCourse(Faculty faculty) {
        System.out.println("\n===== Manage Course =====");
        List<Course> teachingCourses = faculty.getCoursesTeaching();

        if (teachingCourses.isEmpty()) {
            System.out.println("You are not teaching any courses currently.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Select a course to manage:");
        for (int i = 0; i < teachingCourses.size(); i++) {
            Course course = teachingCourses.get(i);
            System.out.println((i + 1) + ". " + course.getCourseId() + " - " + course.getTitle());
        }

        System.out.print("Enter course number (0 to cancel): ");
        int courseChoice = getIntInput();

        if (courseChoice > 0 && courseChoice <= teachingCourses.size()) {
            Course selectedCourse = teachingCourses.get(courseChoice - 1);
            boolean managingCourse = true;

            while (managingCourse) {
                System.out.println("\n===== Managing " + selectedCourse.getTitle() + " =====");
                System.out.println("1. View Student Roster");
                System.out.println("2. Assign Grades");
                System.out.println("3. Return to Faculty Dashboard");
                System.out.print("Enter your choice: ");

                int choice = getIntInput();

                switch (choice) {
                    case 1:
                        viewStudentRoster(selectedCourse);
                        break;
                    case 2:
                        assignGrades(faculty, selectedCourse);
                        break;
                    case 3:
                        managingCourse = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } else if (courseChoice != 0) {
            System.out.println("Invalid course number.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void viewStudentRoster(Course course) {
        System.out.println("\n===== Student Roster for " + course.getTitle() + " =====");
        List<Student> students = course.getEnrolledStudents();

        if (students.isEmpty()) {
            System.out.println("No students enrolled in this course.");
        } else {
            System.out.println("Enrolled Students (" + students.size() + "):");
            System.out.println("------------------------------------------------");
            System.out.printf("%-10s %-20s %-20s\n", "ID", "Name", "Email");
            System.out.println("------------------------------------------------");

            for (Student student : students) {
                System.out.printf("%-10s %-20s %-20s\n",
                        student.getUserId(),
                        student.getName(),
                        student.getEmail());
            }
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void assignGrades(Faculty faculty, Course course) {
        System.out.println("\n===== Assign Grades for " + course.getTitle() + " =====");
        List<Enrollment> enrollments = university.getEnrollmentsForCourse(course);

        if (enrollments.isEmpty()) {
            System.out.println("No students enrolled in this course.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Select a student to assign grade:");
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            Student student = enrollment.getStudent();
            System.out.println((i + 1) + ". " + student.getUserId() + " - " + student.getName() +
                    " (Current Grade: " + (enrollment.getGrade() != null ? enrollment.getGrade() : "Not graded") + ")");
        }

        System.out.print("Enter student number (0 to cancel): ");
        int studentChoice = getIntInput();

        if (studentChoice > 0 && studentChoice <= enrollments.size()) {
            Enrollment selectedEnrollment = enrollments.get(studentChoice - 1);
            System.out.println("Enter grade for " + selectedEnrollment.getStudent().getName() +
                    " (A, A-, B+, B, B-, C+, C, C-, D, F): ");
            String grade = scanner.nextLine().toUpperCase();

            // Validate grade format
            if (isValidGrade(grade)) {
                try {
                    faculty.assignGrade(selectedEnrollment, grade);
                    fileManager.saveEnrollments(university);
                    System.out.println("Grade assigned successfully.");
                } catch (IOException e) {
                    System.out.println("Error saving grade: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid grade format. Please use A, A-, B+, B, B-, C+, C, C-, D, F.");
            }
        } else if (studentChoice != 0) {
            System.out.println("Invalid student number.");
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static boolean isValidGrade(String grade) {
        return grade.matches("A|A-|B\\+|B|B-|C\\+|C|C-|D|F");
    }

    private static void setOfficeHours(Faculty faculty) {
        System.out.println("\n===== Set Office Hours =====");
        System.out.println("Current Office Hours: " + faculty.getOfficeHours());
        System.out.println("Enter new office hours (e.g., 'Monday 10-12, Wednesday 2-4'): ");
        String officeHours = scanner.nextLine();

        try {
            faculty.setOfficeHours(officeHours);
            fileManager.saveUsers(university);
            System.out.println("Office hours updated successfully.");
        } catch (IOException e) {
            System.out.println("Error saving office hours: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    // Admin dashboard methods
    private static void viewAdminProfile(AdminStaff admin) {
        System.out.println("\n===== Admin Profile =====");
        System.out.println("ID: " + admin.getUserId());
        System.out.println("Name: " + admin.getName());
        System.out.println("Email: " + admin.getEmail());
        System.out.println("Contact: " + admin.getContactInfo());
        System.out.println(
                "Department: " + (admin.getDepartment() != null ? admin.getDepartment().getName() : "Not assigned"));
        System.out.println("Role: " + admin.getRole());
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void registerNewStudent() {
        System.out.println("\n===== Register New Student =====");

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        // Check if username already exists
        if (university.findUserByUsername(username) != null) {
            System.out.println("Username already exists. Please choose another username.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter Password (min 6 characters): ");
        String password = scanner.nextLine();
        if (password.length() < 6) {
            System.out.println("Password must be at least 6 characters long.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Contact Number: ");
        String contactInfo = scanner.nextLine();

        System.out.print("Enter Admission Date (YYYY-MM-DD): ");
        String admissionDate = scanner.nextLine();

        try {
            // Create and register the new student
            Student newStudent = new Student(username, password, firstName, lastName, email, contactInfo);
            newStudent.setAdmissionDate(admissionDate);
            newStudent.setAcademicStatus("Active");

            university.addUser(newStudent);
            fileManager.saveUsers(university);

            System.out.println("Student registered successfully with ID: " + newStudent.getUserId());
        } catch (IOException e) {
            System.out.println("Error registering student: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void manageCourses() {
        boolean managingCourses = true;

        while (managingCourses) {
            System.out.println("\n===== Manage Courses =====");
            System.out.println("1. View All Courses");
            System.out.println("2. Create New Course");
            System.out.println("3. Assign Faculty to Course");
            System.out.println("4. Return to Admin Dashboard");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewAllCourses();
                    break;
                case 2:
                    createNewCourse();
                    break;
                case 3:
                    assignFacultyToCourse();
                    break;
                case 4:
                    managingCourses = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewAllCourses() {
        System.out.println("\n===== All Courses =====");
        List<Course> courses = university.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses available in the system.");
        } else {
            System.out.println("------------------------------------------------");
            System.out.printf("%-10s %-25s %-10s %-15s\n", "Course ID", "Title", "Credits", "Instructor");
            System.out.println("------------------------------------------------");

            for (Course course : courses) {
                System.out.printf("%-10s %-25s %-10d %-15s\n",
                        course.getCourseId(),
                        course.getTitle(),
                        course.getCreditHours(),
                        (course.getInstructor() != null ? course.getInstructor().getName() : "Not assigned"));
            }
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void createNewCourse() {
        System.out.println("\n===== Create New Course =====");

        System.out.print("Enter Course ID (e.g., CS101): ");
        String courseId = scanner.nextLine();

        // Check if course ID already exists
        if (university.findCourseById(courseId) != null) {
            System.out.println("Course ID already exists. Please choose another ID.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter Course Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Course Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Credit Hours: ");
        int creditHours = getIntInput();

        System.out.print("Enter Maximum Enrollment Capacity: ");
        int maxCapacity = getIntInput();

        System.out.print("Enter Schedule (e.g., 'Mon/Wed 10:00-11:30'): ");
        String schedule = scanner.nextLine();

        System.out.println("Select Department:");
        List<Department> departments = university.getAllDepartments();

        if (departments.isEmpty()) {
            System.out.println("No departments available. Please create a department first.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        for (int i = 0; i < departments.size(); i++) {
            System.out.println((i + 1) + ". " + departments.get(i).getName());
        }

        System.out.print("Enter department number: ");
        int deptChoice = getIntInput();

        if (deptChoice <= 0 || deptChoice > departments.size()) {
            System.out.println("Invalid department selection.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        Department selectedDept = departments.get(deptChoice - 1);

        try {
            // Create and add the new course
            Course newCourse = new Course(courseId, title, description, creditHours);
            newCourse.setMaxCapacity(maxCapacity);
            newCourse.setSchedule(schedule);

            university.addCourse(newCourse);
            selectedDept.addCourse(newCourse);
            fileManager.saveCourses(university);
            fileManager.saveDepartments(university);

            System.out.println("Course created successfully: " + courseId + " - " + title);
        } catch (IOException e) {
            System.out.println("Error creating course: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void assignFacultyToCourse() {
        System.out.println("\n===== Assign Faculty to Course =====");

        // Get all courses
        List<Course> courses = university.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available in the system.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Select a course:");
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.println((i + 1) + ". " + course.getCourseId() + " - " + course.getTitle() +
                    " (Current instructor: "
                    + (course.getInstructor() != null ? course.getInstructor().getName() : "None") + ")");
        }

        System.out.print("Enter course number: ");
        int courseChoice = getIntInput();

        if (courseChoice <= 0 || courseChoice > courses.size()) {
            System.out.println("Invalid course selection.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        Course selectedCourse = courses.get(courseChoice - 1);

        // Get all faculty members
        List<Faculty> facultyMembers = university.getAllFaculty();
        if (facultyMembers.isEmpty()) {
            System.out.println("No faculty members available in the system.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Select a faculty member to assign:");
        for (int i = 0; i < facultyMembers.size(); i++) {
            Faculty faculty = facultyMembers.get(i);
            System.out.println((i + 1) + ". " + faculty.getName() + " (Department: " +
                    (faculty.getDepartment() != null ? faculty.getDepartment().getName() : "None") + ")");
        }

        System.out.print("Enter faculty number: ");
        int facultyChoice = getIntInput();

        if (facultyChoice <= 0 || facultyChoice > facultyMembers.size()) {
            System.out.println("Invalid faculty selection.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        Faculty selectedFaculty = facultyMembers.get(facultyChoice - 1);

        try {
            // Check if the course already has an instructor
            Faculty currentInstructor = selectedCourse.getInstructor();
            if (currentInstructor != null) {
                currentInstructor.removeCourseTeaching(selectedCourse);
            }

            // Assign new instructor
            selectedCourse.setInstructor(selectedFaculty);
            selectedFaculty.addCourseTeaching(selectedCourse);

            fileManager.saveCourses(university);
            fileManager.saveUsers(university);

            System.out.println("Faculty assigned successfully. " + selectedFaculty.getName() +
                    " is now teaching " + selectedCourse.getTitle());
        } catch (IOException e) {
            System.out.println("Error assigning faculty: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void manageFaculty() {
        boolean managingFaculty = true;

        while (managingFaculty) {
            System.out.println("\n===== Manage Faculty =====");
            System.out.println("1. View All Faculty");
            System.out.println("2. Add New Faculty");
            System.out.println("3. Assign Faculty to Department");
            System.out.println("4. Return to Admin Dashboard");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewAllFaculty();
                    break;
                case 2:
                    addNewFaculty();
                    break;
                case 3:
                    assignFacultyToDepartment();
                    break;
                case 4:
                    managingFaculty = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewAllFaculty() {
        System.out.println("\n===== All Faculty Members =====");
        List<Faculty> facultyMembers = university.getAllFaculty();

        if (facultyMembers.isEmpty()) {
            System.out.println("No faculty members available in the system.");
        } else {
            System.out.println("------------------------------------------------");
            System.out.printf("%-10s %-20s %-20s %-15s\n", "ID", "Name", "Email", "Department");
            System.out.println("------------------------------------------------");

            for (Faculty faculty : facultyMembers) {
                System.out.printf("%-10s %-20s %-20s %-15s\n",
                        faculty.getUserId(),
                        faculty.getName(),
                        faculty.getEmail(),
                        (faculty.getDepartment() != null ? faculty.getDepartment().getName() : "Not assigned"));
            }
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void addNewFaculty() {
        System.out.println("\n===== Add New Faculty =====");

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        // Check if username already exists
        if (university.findUserByUsername(username) != null) {
            System.out.println("Username already exists. Please choose another username.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter Password (min 6 characters): ");
        String password = scanner.nextLine();
        if (password.length() < 6) {
            System.out.println("Password must be at least 6 characters long.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Contact Number: ");
        String contactInfo = scanner.nextLine();

        System.out.print("Enter Expertise: ");
        String expertise = scanner.nextLine();

        try {
            // Create and register the new faculty member
            Faculty newFaculty = new Faculty(username, password, firstName, lastName, email, contactInfo);
            newFaculty.setExpertise(expertise);

            university.addUser(newFaculty);
            fileManager.saveUsers(university);

            System.out.println("Faculty member added successfully with ID: " + newFaculty.getUserId());
        } catch (IOException e) {
            System.out.println("Error adding faculty: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void assignFacultyToDepartment() {
        System.out.println("\n===== Assign Faculty to Department =====");

        // Get faculty without departments
        List<Faculty> facultyMembers = new ArrayList<>();
        for (Faculty faculty : university.getAllFaculty()) {
            if (faculty.getDepartment() == null) {
                facultyMembers.add(faculty);
            }
        }

        if (facultyMembers.isEmpty()) {
            System.out.println("No faculty members without departments available.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Select a faculty member:");
        for (int i = 0; i < facultyMembers.size(); i++) {
            Faculty faculty = facultyMembers.get(i);
            System.out.println((i + 1) + ". " + faculty.getName() + " (ID: " + faculty.getUserId() + ")");
        }

        System.out.print("Enter faculty number: ");
        int facultyChoice = getIntInput();

        if (facultyChoice <= 0 || facultyChoice > facultyMembers.size()) {
            System.out.println("Invalid faculty selection.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        Faculty selectedFaculty = facultyMembers.get(facultyChoice - 1);

        // Get all departments
        List<Department> departments = university.getAllDepartments();
        if (departments.isEmpty()) {
            System.out.println("No departments available in the system.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("Select a department:");
        for (int i = 0; i < departments.size(); i++) {
            Department dept = departments.get(i);
            System.out.println((i + 1) + ". " + dept.getName());
        }

        System.out.print("Enter department number: ");
        int deptChoice = getIntInput();

        if (deptChoice <= 0 || deptChoice > departments.size()) {
            System.out.println("Invalid department selection.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        Department selectedDept = departments.get(deptChoice - 1);

        try {
            selectedDept.addFaculty(selectedFaculty);
            selectedFaculty.setDepartment(selectedDept);

            fileManager.saveUsers(university);
            fileManager.saveDepartments(university);

            System.out.println("Faculty assigned successfully to department: " + selectedDept.getName());
        } catch (IOException e) {
            System.out.println("Error assigning faculty to department: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void generateReports() {
        boolean generatingReports = true;

        while (generatingReports) {
            System.out.println("\n===== Generate Reports =====");
            System.out.println("1. Student Enrollment Report");
            System.out.println("2. Course Registration Report");
            System.out.println("3. Department Report");
            System.out.println("4. Return to Admin Dashboard");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    generateStudentEnrollmentReport();
                    break;
                case 2:
                    generateCourseRegistrationReport();
                    break;
                case 3:
                    generateDepartmentReport();
                    break;
                case 4:
                    generatingReports = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void generateStudentEnrollmentReport() {
        System.out.println("\n===== Student Enrollment Report =====");
        List<Student> students = university.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students in the system.");
        } else {
            System.out.println("Total Students: " + students.size());

            // Count academic status
            int active = 0;
            int onProbation = 0;
            int graduated = 0;

            for (Student student : students) {
                String status = student.getAcademicStatus();
                if (status.equalsIgnoreCase("Active")) {
                    active++;
                } else if (status.equalsIgnoreCase("On Probation")) {
                    onProbation++;
                } else if (status.equalsIgnoreCase("Graduated")) {
                    graduated++;
                }
            }

            System.out.println("\nAcademic Status Breakdown:");
            System.out.println("- Active: " + active + " (" +
                    String.format("%.1f", (double) active / students.size() * 100) + "%)");
            System.out.println("- On Probation: " + onProbation + " (" +
                    String.format("%.1f", (double) onProbation / students.size() * 100) + "%)");
            System.out.println("- Graduated: " + graduated + " (" +
                    String.format("%.1f", (double) graduated / students.size() * 100) + "%)");
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void generateCourseRegistrationReport() {
        System.out.println("\n===== Course Registration Report =====");
        List<Course> courses = university.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses in the system.");
        } else {
            System.out.println("Total Courses: " + courses.size());
            System.out.println("\nEnrollment by Course:");
            System.out.println("---------------------------------------------------------");
            System.out.printf("%-10s %-30s %-10s %-10s\n", "Course ID", "Title", "Enrolled", "Capacity");
            System.out.println("---------------------------------------------------------");

            for (Course course : courses) {
                int enrolled = course.getEnrolledStudents().size();
                int capacity = course.getMaxCapacity();
                double fillRate = capacity > 0 ? (double) enrolled / capacity * 100 : 0;

                System.out.printf("%-10s %-30s %-10d %-10d (%.1f%%)\n",
                        course.getCourseId(),
                        course.getTitle(),
                        enrolled,
                        capacity,
                        fillRate);
            }
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void generateDepartmentReport() {
        System.out.println("\n===== Department Report =====");
        List<Department> departments = university.getAllDepartments();

        if (departments.isEmpty()) {
            System.out.println("No departments in the system.");
        } else {
            System.out.println("Total Departments: " + departments.size());
            System.out.println("\nDepartment Details:");

            for (Department dept : departments) {
                System.out.println("\n" + dept.getName() + " Department:");
                System.out.println("  Faculty Members: " + dept.getFaculty().size());
                System.out.println("  Courses Offered: " + dept.getOfferedCourses().size());

                // List faculty
                if (!dept.getFaculty().isEmpty()) {
                    System.out.println("  Faculty:");
                    for (Faculty faculty : dept.getFaculty()) {
                        System.out.println("  - " + faculty.getName() + " (" + faculty.getExpertise() + ")");
                    }
                }

                // List courses
                if (!dept.getOfferedCourses().isEmpty()) {
                    System.out.println("  Courses:");
                    for (Course course : dept.getOfferedCourses()) {
                        System.out.println("  - " + course.getCourseId() + ": " + course.getTitle());
                    }
                }
            }
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    // System Admin dashboard methods
    private static void viewSystemAdminProfile(SystemAdmin admin) {
        System.out.println("\n===== System Admin Profile =====");
        System.out.println("ID: " + admin.getUserId());
        System.out.println("Name: " + admin.getName());
        System.out.println("Email: " + admin.getEmail());
        System.out.println("Contact: " + admin.getContactInfo());
        System.out.println("Security Level: " + admin.getSecurityLevel());
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void createUserAccount() {
        System.out.println("\n===== Create User Account =====");
        System.out.println("Select user type:");
        System.out.println("1. Student");
        System.out.println("2. Faculty");
        System.out.println("3. Admin Staff");
        System.out.println("4. System Admin");
        System.out.print("Enter your choice: ");

        int userType = getIntInput();

        if (userType < 1 || userType > 4) {
            System.out.println("Invalid user type.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        // Check if username already exists
        if (university.findUserByUsername(username) != null) {
            System.out.println("Username already exists. Please choose another username.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter Password (min 6 characters): ");
        String password = scanner.nextLine();
        if (password.length() < 6) {
            System.out.println("Password must be at least 6 characters long.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Contact Number: ");
        String contactInfo = scanner.nextLine();

        try {
            User newUser = null;

            switch (userType) {
                case 1: // Student
                    newUser = new Student(username, password, firstName, lastName, email, contactInfo);
                    System.out.print("Enter Admission Date (YYYY-MM-DD): ");
                    String admissionDate = scanner.nextLine();
                    ((Student) newUser).setAdmissionDate(admissionDate);
                    ((Student) newUser).setAcademicStatus("Active");
                    break;
                case 2: // Faculty
                    newUser = new Faculty(username, password, firstName, lastName, email, contactInfo);
                    System.out.print("Enter Expertise: ");
                    String expertise = scanner.nextLine();
                    ((Faculty) newUser).setExpertise(expertise);
                    break;
                case 3: // Admin Staff
                    newUser = new AdminStaff(username, password, firstName, lastName, email, contactInfo);
                    System.out.print("Enter Role: ");
                    String role = scanner.nextLine();
                    ((AdminStaff) newUser).setRole(role);
                    break;
                case 4: // System Admin
                    newUser = new SystemAdmin(username, password, firstName, lastName, email, contactInfo);
                    System.out.print("Enter Security Level (1-5): ");
                    int securityLevel = getIntInput();
                    ((SystemAdmin) newUser).setSecurityLevel(securityLevel);
                    break;
            }

            university.addUser(newUser);
            fileManager.saveUsers(university);

            System.out.println("User account created successfully with ID: " + newUser.getUserId());
        } catch (IOException e) {
            System.out.println("Error creating user account: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void createDepartment() {
        System.out.println("\n===== Create Department =====");

        System.out.print("Enter Department Name: ");
        String name = scanner.nextLine();

        // Check if department already exists
        for (Department dept : university.getAllDepartments()) {
            if (dept.getName().equalsIgnoreCase(name)) {
                System.out.println("Department already exists.");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }
        }

        try {
            Department newDept = new Department(name);
            university.addDepartment(newDept);
            fileManager.saveDepartments(university);

            System.out.println("Department created successfully: " + name);
        } catch (IOException e) {
            System.out.println("Error creating department: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void backupSystemData() {
        System.out.println("\n===== Backup System Data =====");
        System.out.print("Enter backup directory path (leave empty for default): ");
        String backupDir = scanner.nextLine();

        if (backupDir.isEmpty()) {
            backupDir = "backup";
        }

        try {
            fileManager.backupData(university, backupDir);
            System.out.println("System data backed up successfully to: " + backupDir);
        } catch (IOException e) {
            System.out.println("Error backing up data: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void restoreSystemData() {
        System.out.println("\n===== Restore System Data =====");
        System.out.println("Warning: This will overwrite all current data! Are you sure? (y/n)");
        String confirm = scanner.nextLine().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println("Restoration cancelled.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter backup directory path to restore from: ");
        String backupDir = scanner.nextLine();

        try {
            fileManager.restoreData(university, backupDir);
            System.out.println("System data restored successfully from: " + backupDir);
        } catch (IOException e) {
            System.out.println("Error restoring data: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void manageSystemSettings() {
        System.out.println("\n===== Manage System Settings =====");
        System.out.println("1. View System Settings");
        System.out.println("2. Change Data Storage Path");
        System.out.println("3. Return to System Admin Dashboard");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                System.out.println("\nCurrent System Settings:");
                System.out.println("Data Storage Path: " + fileManager.getDataPath());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                break;
            case 2:
                System.out.print("Enter new data storage path: ");
                String newPath = scanner.nextLine();
                fileManager.setDataPath(newPath);
                System.out.println("Data storage path updated successfully.");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice.");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
        }
    }

    // Common methods
    private static void changePassword() {
        System.out.println("\n===== Change Password =====");
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (!currentUser.getPassword().equals(currentPassword)) {
            System.out.println("Current password is incorrect.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter new password (min 6 characters): ");
        String newPassword = scanner.nextLine();

        if (newPassword.length() < 6) {
            System.out.println("Password must be at least 6 characters long.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        try {
            currentUser.setPassword(newPassword);
            fileManager.saveUsers(university);
            System.out.println("Password changed successfully.");
        } catch (IOException e) {
            System.out.println("Error changing password: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Helper method to safely get integer input from the user
     */
    private static int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // Invalid input
        }
    }
}