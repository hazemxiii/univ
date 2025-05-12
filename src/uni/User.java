package uni;

/**
 * Abstract base class for all system users
 */
public abstract class User {
    private String userId;
    private String username;
    private String password;
    private String name;
    private String email;
    private String contactInfo;

    /**
     * Constructor for User class
     * 
     * @param userId      Unique identifier for user
     * @param username    Login username
     * @param password    User password
     * @param name        Full name of user
     * @param email       Email address
     * @param contactInfo Contact information
     */
    public User(String userId, String username, String password, String name, String email, String contactInfo) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.contactInfo = contactInfo;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Validate password
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Check if login credentials are valid
     * 
     * @param username Username to check
     * @param password Password to check
     * @return true if credentials match, false otherwise
     */
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Get the type of user (for polymorphism)
     * 
     * @return String representing user type
     */
    public abstract String getUserType();

    /**
     * Update user profile information
     * 
     * @param name        New name (if null, keeps existing)
     * @param email       New email (if null, keeps existing)
     * @param contactInfo New contact info (if null, keeps existing)
     */
    public void updateProfile(String name, String email, String contactInfo) {
        if (name != null) {
            this.name = name;
        }
        if (email != null) {
            this.email = email;
        }
        if (contactInfo != null) {
            this.contactInfo = contactInfo;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}