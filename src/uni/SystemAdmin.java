package uni;

/**
 * SystemAdmin class that extends User
 * Represents system administrators with privileged access
 */
public class SystemAdmin extends User {
    private String adminId;
    private int securityLevel;

    /**
     * Constructor for SystemAdmin class
     * 
     * @param userId        Unique identifier
     * @param username      Login username
     * @param password      User password
     * @param name          Full name
     * @param email         Email address
     * @param contactInfo   Contact information
     * @param adminId       Admin ID
     * @param securityLevel Security clearance level
     */
    public SystemAdmin(String userId, String username, String password, String name, String email, String contactInfo,
            String adminId, int securityLevel) {
        super(userId, username, password, name, email, contactInfo);
        this.adminId = adminId;
        setSecurityLevel(securityLevel);
    }

    // Getters and setters
    public String getAdminId() {
        return adminId;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(int securityLevel) {
        if (securityLevel < 1 || securityLevel > 5) {
            throw new IllegalArgumentException("Security level must be between 1 and 5");
        }
        this.securityLevel = securityLevel;
    }

    /**
     * Create a new user in the system
     * 
     * @param user User to create
     * @return true if creation successful, false otherwise
     */
    public boolean createUser(User user) {
        // In a real implementation, this would add the user to the database
        // For now, just simulate success
        return true;
    }

    /**
     * Modify system settings
     * 
     * @param setting Setting to modify
     * @param value   New value
     * @return true if modification successful, false otherwise
     */
    public boolean modifySystemSettings(String setting, String value) {
        // In a real implementation, this would update system settings in the database
        // For now, just simulate success
        return true;
    }

    /**
     * Backup system data
     * 
     * @param backupPath Path to backup location
     * @return true if backup successful, false otherwise
     */
    public boolean backupData(String backupPath) {
        // In a real implementation, this would create a backup of the database
        // For now, just simulate success
        return true;
    }

    /**
     * Manage user permissions
     * 
     * @param user       User to modify permissions for
     * @param permission Permission to modify
     * @param grant      Whether to grant or revoke the permission
     * @return true if permission change successful, false otherwise
     */
    public boolean managePermissions(User user, String permission, boolean grant) {
        // Check if admin has sufficient security level
        if (securityLevel < 3) {
            return false; // Insufficient privileges
        }

        // In a real implementation, this would update user permissions in the database
        // For now, just simulate success
        return true;
    }

    @Override
    public String getUserType() {
        return "SystemAdmin";
    }

    @Override
    public String toString() {
        return "SystemAdmin{" +
                "adminId='" + adminId + '\'' +
                ", securityLevel=" + securityLevel +
                "} " + super.toString();
    }
}