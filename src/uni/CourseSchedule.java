package uni;

import java.time.LocalTime;

/**
 * CourseSchedule class for course meeting times
 */
public class CourseSchedule {
    private int scheduleId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;

    /**
     * Constructor for CourseSchedule class
     * 
     * @param scheduleId Schedule ID
     * @param dayOfWeek  Day of the week
     * @param startTime  Start time
     * @param endTime    End time
     * @param location   Room or location
     */
    public CourseSchedule(int scheduleId, String dayOfWeek, LocalTime startTime, LocalTime endTime, String location) {
        this.scheduleId = scheduleId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;

        // Validate time range
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    // Getters and setters
    public int getScheduleId() {
        return scheduleId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        if (endTime != null && (endTime.isBefore(startTime) || endTime.equals(startTime))) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        if (startTime != null && (endTime.isBefore(startTime) || endTime.equals(startTime))) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Check if this schedule conflicts with another schedule
     * 
     * @param other Other schedule to check
     * @return true if conflict exists, false otherwise
     */
    public boolean conflictsWith(CourseSchedule other) {
        // Different days, no conflict
        if (!dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }

        // Same day, check for time overlap
        return !(endTime.isBefore(other.startTime) || startTime.isAfter(other.endTime));
    }

    @Override
    public String toString() {
        return "CourseSchedule{" +
                "scheduleId=" + scheduleId +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                '}';
    }
}