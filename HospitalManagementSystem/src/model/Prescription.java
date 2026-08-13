package model;

/**
 * Digital medication prescription issued by a Doctor.
 */
public class Prescription {
    private String prescriptionId;
    private String consultationId;
    private String patientId;
    private String doctorId;
    private String medication;      // e.g. Amoxicillin 500mg
    private String dosage;          // e.g. 1 tablet twice daily
    private String duration;        // e.g. 7 days
    private String instructions;
    private String dateIssued;

    public Prescription() {}

    public Prescription(String prescriptionId, String consultationId, String patientId,
                        String doctorId, String medication, String dosage,
                        String duration, String instructions, String dateIssued) {
        this.prescriptionId = prescriptionId;
        this.consultationId = consultationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medication = medication;
        this.dosage = dosage;
        this.duration = duration;
        this.instructions = instructions;
        this.dateIssued = dateIssued;
    }

    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }

    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getDateIssued() { return dateIssued; }
    public void setDateIssued(String dateIssued) { this.dateIssued = dateIssued; }

    public String toFileString() {
        return prescriptionId + "|" + consultationId + "|" + patientId + "|" + doctorId + "|"
                + medication + "|" + dosage + "|" + duration + "|"
                + (instructions == null ? "" : instructions) + "|" + dateIssued;
    }

    public static Prescription fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 9) return null;

        Prescription pr = new Prescription();
        pr.setPrescriptionId(p[0]);
        pr.setConsultationId(p[1]);
        pr.setPatientId(p[2]);
        pr.setDoctorId(p[3]);
        pr.setMedication(p[4]);
        pr.setDosage(p[5]);
        pr.setDuration(p[6]);
        pr.setInstructions(p[7]);
        pr.setDateIssued(p[8]);
        return pr;
    }
}
