package dao;

import model.Doctor;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Data-Access Object for Doctor entities.
 * All persistence is done via plain text files (no database).
 */
public class DoctorFile {

    public List<Doctor> getAll() {
        List<Doctor> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.DOCTORS_FILE)) {
            Doctor d = Doctor.fromFileString(line);
            if (d != null) list.add(d);
        }
        return list;
    }

    public Doctor findById(String id) {
        for (Doctor d : getAll()) {
            if (d.getId().equalsIgnoreCase(id)) return d;
        }
        return null;
    }

    public Doctor findByEmail(String email) {
        for (Doctor d : getAll()) {
            if (d.getEmail().equalsIgnoreCase(email)) return d;
        }
        return null;
    }

    /**
     * Login helper - returns Doctor if credentials match, otherwise null.
     */
    public Doctor authenticate(String email, String password) {
        Doctor d = findByEmail(email);
        if (d != null && d.getPassword().equals(password) && d.isActive()) {
            return d;
        }
        return null;
    }

    public void save(Doctor doctor) {
        List<Doctor> all = getAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(doctor.getId())) {
                all.set(i, doctor);
                updated = true;
                break;
            }
        }
        if (!updated) {
            all.add(doctor);
        }
        writeAll(all);
    }

    public void delete(String id) {
        List<Doctor> all = getAll();
        all.removeIf(d -> d.getId().equals(id));
        writeAll(all);
    }

    private void writeAll(List<Doctor> list) {
        List<String> lines = new ArrayList<>();
        for (Doctor d : list) {
            lines.add(d.toFileString());
        }
        FileManager.writeAllLines(FileManager.DOCTORS_FILE, FileManager.HEADER_DOCTORS, lines);
    }

    /**
     * Seed a default doctor account so the system can be tested immediately.
     */
    public void seedDefaultIfEmpty() {
        if (!getAll().isEmpty()) return;

        Doctor demo = new Doctor(
                "DOC-001",
                "Aisha Rahman",
                "doctor@hms.com",
                "012-3456789",
                "doctor123",
                "Cardiology",
                "DEPT-CARD",
                "MGR-001",
                150.00,
                "Morning"
        );
        save(demo);

        Doctor demo2 = new Doctor(
                "DOC-002",
                "Wei Ming Tan",
                "doctor2@hms.com",
                "013-9876543",
                "doctor123",
                "Neurology",
                "DEPT-NEURO",
                "MGR-001",
                180.00,
                "Afternoon"
        );
        save(demo2);
    }
}
