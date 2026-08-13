package model;

/**
 * Patient inherits from Person.
 */
public class Patient extends Person {
    private static final long serialVersionUID = 1L;

    private String bloodType;
    private String allergies;
    private String insuranceProvider;
    private String emergencyContact;

    public Patient() {
        super();
        this.role = "PATIENT";
    }

    public Patient(String id, String name, String email, String phone, String password,
                   String bloodType, String allergies, String insuranceProvider, String emergencyContact) {
        super(id, name, email, phone, password, "PATIENT");
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.insuranceProvider = insuranceProvider;
        this.emergencyContact = emergencyContact;
    }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    @Override
    public String getDisplayInfo() {
        return name + " (Blood: " + bloodType + ")";
    }

    public String toFileString() {
        return id + "|" + name + "|" + email + "|" + phone + "|" + password + "|" + role + "|"
                + bloodType + "|" + allergies + "|" + insuranceProvider + "|" + emergencyContact;
    }

    public static Patient fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 10) return null;

        Patient pt = new Patient();
        pt.setId(p[0]);
        pt.setName(p[1]);
        pt.setEmail(p[2]);
        pt.setPhone(p[3]);
        pt.setPassword(p[4]);
        pt.setRole(p[5]);
        pt.setBloodType(p[6]);
        pt.setAllergies(p[7]);
        pt.setInsuranceProvider(p[8]);
        pt.setEmergencyContact(p[9]);
        return pt;
    }
}
