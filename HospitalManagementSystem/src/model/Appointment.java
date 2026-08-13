package model;

/**
 * Represents a consultation booking between a Patient and a Doctor.
 */
public class Appointment {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String date;          // yyyy-MM-dd
    private String time;          // HH:mm
    private String status;        // PENDING, CONFIRMED, COMPLETED, CANCELLED
    private String notes;

    public Appointment() {}

    public Appointment(String appointmentId, String patientId, String doctorId,
                       String date, String time, String status, String notes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.notes = notes;
    }

    // Getters & Setters
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String toFileString() {
        return appointmentId + "|" + patientId + "|" + doctorId + "|"
                + date + "|" + time + "|" + status + "|" + (notes == null ? "" : notes);
    }

    public static Appointment fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 7) return null;

        Appointment a = new Appointment();
        a.setAppointmentId(p[0]);
        a.setPatientId(p[1]);
        a.setDoctorId(p[2]);
        a.setDate(p[3]);
        a.setTime(p[4]);
        a.setStatus(p[5]);
        a.setNotes(p[6]);
        return a;
    }
}
