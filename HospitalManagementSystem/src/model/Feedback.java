package model;

/**
 * Patient rating / comment for a doctor or clinic visit.
 * Teammate (Patient module) can expand as needed.
 */
public class Feedback {
    private String feedbackId;
    private String patientId;
    private String doctorId;
    private String appointmentId;
    private int rating;          // 1–5
    private String comments;
    private String dateSubmitted;

    public Feedback() {}

    public Feedback(String feedbackId, String patientId, String doctorId, String appointmentId,
                    int rating, String comments, String dateSubmitted) {
        this.feedbackId = feedbackId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.rating = rating;
        this.comments = comments;
        this.dateSubmitted = dateSubmitted;
    }

    public String getFeedbackId() { return feedbackId; }
    public void setFeedbackId(String feedbackId) { this.feedbackId = feedbackId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public String getDateSubmitted() { return dateSubmitted; }
    public void setDateSubmitted(String dateSubmitted) { this.dateSubmitted = dateSubmitted; }

    /** Format: feedbackId|patientId|doctorId|appointmentId|rating|comments|dateSubmitted */
    public String toFileString() {
        return feedbackId + "|" + patientId + "|" + doctorId + "|" + appointmentId + "|"
                + rating + "|" + (comments == null ? "" : comments) + "|" + dateSubmitted;
    }

    public static Feedback fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 7) return null;
        Feedback f = new Feedback();
        f.setFeedbackId(p[0]);
        f.setPatientId(p[1]);
        f.setDoctorId(p[2]);
        f.setAppointmentId(p[3]);
        try { f.setRating(Integer.parseInt(p[4])); } catch (NumberFormatException e) { f.setRating(0); }
        f.setComments(p[5]);
        f.setDateSubmitted(p[6]);
        return f;
    }
}
