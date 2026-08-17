package service;

import dao.AppointmentFile;
import dao.DepartmentFile;
import dao.DoctorFile;
import dao.ShiftRosterFile;

import model.Appointment;
import model.Doctor;

import java.util.List;

public class HospitalReportService {

    private final DepartmentFile departmentFile = new DepartmentFile();

    private final DoctorFile doctorFile = new DoctorFile();

    private final AppointmentFile appointmentFile = new AppointmentFile();

    private final ShiftRosterFile shiftRosterFile = new ShiftRosterFile();

    //=========================================
    //HOSPITAL METRICS
    //=========================================

    public int getTotalDepartments() {
        return departmentFile.getAll().size();
    }

    public int getTotalDoctors() {
        return doctorFile.getAll().size();
    }

    public int getTotalAppointments() {
        return appointmentFile.getAll().size();
    }

    public int getCompletedAppointments() {
        int count = 0;
        for (Appointment appointment : appointmentFile.getAll()) {
            if ("COMPLETED".equalsIgnoreCase(appointment.getStatus())) {
                count++;
            }
        }

        return count;
    }

    public int getTotalShiftRosters() {
        return shiftRosterFile.getAll().size();
    }

    /*
     * =========================================
     * REVENUE
     * =========================================
     *
     * Revenue is currently calculated from:
     *
     * COMPLETED appointment
     *        ↓
     * Doctor
     *        ↓
     * Doctor consultation fee
     *
     * This can later be replaced by a proper
     * Payment/Billing module.
     */
    public double getTotalConsultationRevenue() {

        double totalRevenue = 0.0;

        List<Appointment> appointments = appointmentFile.getAll();

        for (Appointment appointment : appointments) {

            //Revenue only counts when the consultation appointment has actually been completed.
            if (!"COMPLETED".equalsIgnoreCase(appointment.getStatus())) {
                continue;
            }

            Doctor doctor = doctorFile.findById(appointment.getDoctorId());

            //If Doctor record cannot be found, ignore this appointment instead of crashing the report.
            if (doctor != null) {
                totalRevenue += doctor.getConsultationFee();
            }
        }

        return totalRevenue;
    }

    public double getAverageRevenuePerCompletedAppointment() {

        int completed = getCompletedAppointments();

        if (completed == 0) {
            return 0.0;
        }

        return getTotalConsultationRevenue() / completed;
    }
}