package model;

/**
 * Clinical department / specialty (e.g. Cardiology).
 * Used by Medical Managers (create/update) and Admin (reference).
 * Teammate can extend fields as needed.
 */
public class Department {
    private String departmentId;
    private String name;
    private String description;
    private String managerId; // MedicalManager in charge

    public Department() {}

    public Department(String departmentId, String name, String description, String managerId) {
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
        this.managerId = managerId;
    }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }

    /** Format: departmentId|name|description|managerId */
    public String toFileString() {
        return departmentId + "|" + name + "|"
                + (description == null ? "" : description) + "|"
                + (managerId == null ? "" : managerId);
    }

    public static Department fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 4) return null;
        Department d = new Department();
        d.setDepartmentId(p[0]);
        d.setName(p[1]);
        d.setDescription(p[2]);
        d.setManagerId(p[3]);
        return d;
    }
}
