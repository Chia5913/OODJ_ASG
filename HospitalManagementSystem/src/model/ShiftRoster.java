package model;

/**
 * Represents one operational shift assigned to a doctor.
 *
 * One doctor can have multiple ShiftRoster records
 * on different dates and times.
 */
public class ShiftRoster {
    private String rosterId;
    private String doctorId;
    private String departmentId;
    private String shiftDate;
    private String startTime;
    private String endTime;
    private String shiftType;

    /*
     * Empty constructor.
     *
     * This follows the same style used by the
     * team's other model classes.
     */
    public ShiftRoster() {
    }

    /*
     * Full constructor.
     */
    public ShiftRoster(String rosterId, String doctorId, String departmentId, String shiftDate, String startTime, String endTime, String shiftType) {
        this.rosterId = rosterId;
        this.doctorId = doctorId;
        this.departmentId = departmentId;
        this.shiftDate = shiftDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftType = shiftType;
    }

    // ----- Getters & Setters -----
    public String getRosterId() {
        return rosterId;
    }

    public void setRosterId(String rosterId) {
        this.rosterId = rosterId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    /**
     * Convert this ShiftRoster object into one line
     * for storage inside shift_rosters.txt.
     *
     * Format:
     * rosterId|doctorId|departmentId|shiftDate|
     * startTime|endTime|shiftType
     */
    public String toFileString() {
        return rosterId + "|" + doctorId + "|" + departmentId + "|" + shiftDate + "|" + startTime + "|" + endTime + "|" + shiftType;
    }

    /**
     * Reconstruct a ShiftRoster object
     * from one line in shift_rosters.txt.
     */
    public static ShiftRoster fromFileString(String line) {

        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\|", -1);

        if (parts.length < 7) {
            return null;
        }

        ShiftRoster roster = new ShiftRoster();

        roster.setRosterId(parts[0]);
        roster.setDoctorId(parts[1]);
        roster.setDepartmentId(parts[2]);
        roster.setShiftDate(parts[3]);
        roster.setStartTime(parts[4]);
        roster.setEndTime(parts[5]);
        roster.setShiftType(parts[6]);
        return roster;
    }

    @Override
    public String toString() {

        return rosterId
                + " - "
                + doctorId
                + " - "
                + shiftDate
                + " "
                + startTime
                + "-"
                + endTime;
    }
}