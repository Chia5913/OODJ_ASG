package dao;

import model.Appointment;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentFile {

    public List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.APPOINTMENTS_FILE)) {
            Appointment a = Appointment.fromFileString(line);
            if (a != null) list.add(a);
        }
        return list;
    }

    public List<Appointment> getByDoctorId(String doctorId) {
        return getAll().stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getByPatientId(String patientId) {
        return getAll().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public Appointment findById(String id) {
        for (Appointment a : getAll()) {
            if (a.getAppointmentId().equals(id)) return a;
        }
        return null;
    }

    public void save(Appointment appointment) {
        List<Appointment> all = getAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getAppointmentId().equals(appointment.getAppointmentId())) {
                all.set(i, appointment);
                updated = true;
                break;
            }
        }
        if (!updated) {
            all.add(appointment);
        }
        writeAll(all);
    }

    public void delete(String id) {
        List<Appointment> all = getAll();
        all.removeIf(a -> a.getAppointmentId().equals(id));
        writeAll(all);
    }

    private void writeAll(List<Appointment> list) {
        List<String> lines = new ArrayList<>();
        for (Appointment a : list) {
            lines.add(a.toFileString());
        }
        FileManager.writeAllLines(FileManager.APPOINTMENTS_FILE, FileManager.HEADER_APPOINTMENTS, lines);
    }

    /**
     * Seed a few sample appointments for the demo doctor.
     */
    public void seedDefaultIfEmpty() {
        if (!getAll().isEmpty()) return;

        Appointment a1 = new Appointment(
                FileManager.generateId("APT"),
                "PAT-001",
                "DOC-001",
                "2026-08-05",
                "09:30",
                "CONFIRMED",
                "Follow-up checkup"
        );
        save(a1);

        Appointment a2 = new Appointment(
                FileManager.generateId("APT"),
                "PAT-002",
                "DOC-001",
                "2026-08-05",
                "11:00",
                "PENDING",
                "First visit - chest pain"
        );
        save(a2);
    }
}
