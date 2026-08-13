package model;

/**
 * Doctor inherits from Person (Inheritance).
 * Demonstrates Encapsulation of doctor-specific attributes.
 */
public class Doctor extends Person {
    private static final long serialVersionUID = 1L;

    private String specialty;       // e.g. Cardiology, Neurology
    private String departmentId;    // linked to Medical Department
    private String managerId;       // Medical Manager who supervises this doctor
    private double consultationFee;
    private String shift;           // Morning / Afternoon / Night
    private boolean active;

    public Doctor() {
        super();
        this.role = "DOCTOR";
        this.active = true;
    }

    public Doctor(String id, String name, String email, String phone, String password,
                  String specialty, String departmentId, String managerId,
                  double consultationFee, String shift) {
        super(id, name, email, phone, password, "DOCTOR");
        this.specialty = specialty;
        this.departmentId = departmentId;
        this.managerId = managerId;
        this.consultationFee = consultationFee;
        this.shift = shift;
        this.active = true;
    }

    // ----- Getters & Setters -----
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String getDisplayInfo() {
        return "Dr. " + name + " (" + specialty + ") - Fee: RM" + String.format("%.2f", consultationFee);
    }

    /**
     * Convert object to a single line for text-file storage.
     * Format: id|name|email|phone|password|role|specialty|departmentId|managerId|fee|shift|active
     */
    public String toFileString() {
        return id + "|" + name + "|" + email + "|" + phone + "|" + password + "|" + role + "|"
                + specialty + "|" + departmentId + "|" + managerId + "|"
                + consultationFee + "|" + shift + "|" + active;
    }

    /**
     * Reconstruct a Doctor from a line read from the text file.
     */
    public static Doctor fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 12) return null;

        Doctor d = new Doctor();
        d.setId(p[0]);
        d.setName(p[1]);
        d.setEmail(p[2]);
        d.setPhone(p[3]);
        d.setPassword(p[4]);
        d.setRole(p[5]);
        d.setSpecialty(p[6]);
        d.setDepartmentId(p[7]);
        d.setManagerId(p[8]);
        try {
            d.setConsultationFee(Double.parseDouble(p[9]));
        } catch (NumberFormatException e) {
            d.setConsultationFee(0.0);
        }
        d.setShift(p[10]);
        d.setActive(Boolean.parseBoolean(p[11]));
        return d;
    }
}
