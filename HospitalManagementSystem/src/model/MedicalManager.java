package model;

/**
 * Medical Manager – inherits from Person.
 * Teammate: expand for departments, shift rosters, hospital reports.
 */
public class MedicalManager extends Person {
    private static final long serialVersionUID = 1L;

    private String managedDepartmentId; // primary department they oversee
    private boolean active;

    public MedicalManager() {
        super();
        this.role = "MANAGER";
        this.active = true;
    }

    public MedicalManager(String id, String name, String email, String phone, String password,
                          String managedDepartmentId) {
        super(id, name, email, phone, password, "MANAGER");
        this.managedDepartmentId = managedDepartmentId;
        this.active = true;
    }

    public String getManagedDepartmentId() { return managedDepartmentId; }
    public void setManagedDepartmentId(String managedDepartmentId) {
        this.managedDepartmentId = managedDepartmentId;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String getDisplayInfo() {
        return name + " (Manager – " + managedDepartmentId + ")";
    }

    /** Format: id|name|email|phone|password|role|managedDepartmentId|active */
    public String toFileString() {
        return id + "|" + name + "|" + email + "|" + phone + "|" + password + "|" + role + "|"
                + managedDepartmentId + "|" + active;
    }

    public static MedicalManager fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 8) return null;
        MedicalManager m = new MedicalManager();
        m.setId(p[0]);
        m.setName(p[1]);
        m.setEmail(p[2]);
        m.setPhone(p[3]);
        m.setPassword(p[4]);
        m.setRole(p[5]);
        m.setManagedDepartmentId(p[6]);
        m.setActive(Boolean.parseBoolean(p[7]));
        return m;
    }
}
