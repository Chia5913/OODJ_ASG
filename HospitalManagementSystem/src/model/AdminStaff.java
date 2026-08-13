package model;

/**
 * Administrative Staff – inherits from Person.
 * Teammate: expand fields / methods as needed for Admin features
 * (CRUD users, assign doctors, manage rooms/wards, configure rates).
 */
public class AdminStaff extends Person {
    private static final long serialVersionUID = 1L;

    private String staffPosition; // e.g. Reception, Records, Billing
    private boolean active;

    public AdminStaff() {
        super();
        this.role = "ADMIN";
        this.active = true;
    }

    public AdminStaff(String id, String name, String email, String phone, String password,
                      String staffPosition) {
        super(id, name, email, phone, password, "ADMIN");
        this.staffPosition = staffPosition;
        this.active = true;
    }

    public String getStaffPosition() { return staffPosition; }
    public void setStaffPosition(String staffPosition) { this.staffPosition = staffPosition; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String getDisplayInfo() {
        return name + " (Admin – " + staffPosition + ")";
    }

    /** Format: id|name|email|phone|password|role|staffPosition|active */
    public String toFileString() {
        return id + "|" + name + "|" + email + "|" + phone + "|" + password + "|" + role + "|"
                + staffPosition + "|" + active;
    }

    public static AdminStaff fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 8) return null;
        AdminStaff a = new AdminStaff();
        a.setId(p[0]);
        a.setName(p[1]);
        a.setEmail(p[2]);
        a.setPhone(p[3]);
        a.setPassword(p[4]);
        a.setRole(p[5]);
        a.setStaffPosition(p[6]);
        a.setActive(Boolean.parseBoolean(p[7]));
        return a;
    }
}
