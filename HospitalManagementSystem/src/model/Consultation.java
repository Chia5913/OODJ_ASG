package model;

/**
 * Medical consultation record written by a Doctor after seeing a Patient.
 * Contains vital signs and clinical notes.
 */
public class Consultation {
    private String consultationId;
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String dateTime;
    private String vitalSigns;      // e.g. BP 120/80, HR 72, Temp 36.8
    private String clinicalNotes;
    private String diagnosis;

    public Consultation() {}

    public Consultation(String consultationId, String appointmentId, String patientId,
                        String doctorId, String dateTime, String vitalSigns,
                        String clinicalNotes, String diagnosis) {
        this.consultationId = consultationId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.vitalSigns = vitalSigns;
        this.clinicalNotes = clinicalNotes;
        this.diagnosis = diagnosis;
    }

    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getVitalSigns() { return vitalSigns; }
    public void setVitalSigns(String vitalSigns) { this.vitalSigns = vitalSigns; }

    public String getClinicalNotes() { return clinicalNotes; }
    public void setClinicalNotes(String clinicalNotes) { this.clinicalNotes = clinicalNotes; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String toFileString() {
        return consultationId + "|" + appointmentId + "|" + patientId + "|" + doctorId + "|"
                + dateTime + "|" + (vitalSigns == null ? "" : vitalSigns) + "|"
                + (clinicalNotes == null ? "" : clinicalNotes) + "|"
                + (diagnosis == null ? "" : diagnosis);
    }

    public static Consultation fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 8) return null;

        Consultation c = new Consultation();
        c.setConsultationId(p[0]);
        c.setAppointmentId(p[1]);
        c.setPatientId(p[2]);
        c.setDoctorId(p[3]);
        c.setDateTime(p[4]);
        c.setVitalSigns(p[5]);
        c.setClinicalNotes(p[6]);
        c.setDiagnosis(p[7]);
        return c;
    }
}
