package model;

/**
 * Request issued by a Doctor for lab tests / X-rays / imaging.
 * Admin will later process these requests.
 */
public class LabRequest {
    private String requestId;
    private String consultationId;
    private String patientId;
    private String doctorId;
    private String testType;        // Blood Test, X-Ray, MRI, CT Scan, etc.
    private String urgency;         // Routine, Urgent, STAT
    private String clinicalInfo;
    private String status;          // PENDING, APPROVED, COMPLETED, REJECTED
    private String dateRequested;

    public LabRequest() {}

    public LabRequest(String requestId, String consultationId, String patientId,
                      String doctorId, String testType, String urgency,
                      String clinicalInfo, String status, String dateRequested) {
        this.requestId = requestId;
        this.consultationId = consultationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.testType = testType;
        this.urgency = urgency;
        this.clinicalInfo = clinicalInfo;
        this.status = status;
        this.dateRequested = dateRequested;
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getClinicalInfo() { return clinicalInfo; }
    public void setClinicalInfo(String clinicalInfo) { this.clinicalInfo = clinicalInfo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDateRequested() { return dateRequested; }
    public void setDateRequested(String dateRequested) { this.dateRequested = dateRequested; }

    public String toFileString() {
        return requestId + "|" + consultationId + "|" + patientId + "|" + doctorId + "|"
                + testType + "|" + urgency + "|"
                + (clinicalInfo == null ? "" : clinicalInfo) + "|"
                + status + "|" + dateRequested;
    }

    public static LabRequest fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 9) return null;

        LabRequest lr = new LabRequest();
        lr.setRequestId(p[0]);
        lr.setConsultationId(p[1]);
        lr.setPatientId(p[2]);
        lr.setDoctorId(p[3]);
        lr.setTestType(p[4]);
        lr.setUrgency(p[5]);
        lr.setClinicalInfo(p[6]);
        lr.setStatus(p[7]);
        lr.setDateRequested(p[8]);
        return lr;
    }
}
