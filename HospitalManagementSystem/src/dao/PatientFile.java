package dao;

import model.Patient;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Patient – text-file persistence.
 * Teammate: add query helpers as needed; keep save/getAll/findById pattern.
 */
public class PatientFile {

    public List<Patient> getAll() {
        List<Patient> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.PATIENTS_FILE)) {
            Patient obj = Patient.fromFileString(line);
            if (obj != null) list.add(obj);
        }
        return list;
    }

    public Patient findById(String id) {
        for (Patient o : getAll()) {
            if (o.getId().equalsIgnoreCase(id)) return o;
        }
        return null;
    }

    public Patient findByEmail(String email) {
        for (Patient o : getAll()) {
            if (o.getEmail().equalsIgnoreCase(email)) return o;
        }
        return null;
    }

    public Patient authenticate(String email, String password) {
        Patient o = findByEmail(email);
        if (o != null && o.getPassword().equals(password)) {
            return o;
        }
        return null;
    }

    public void save(Patient obj) {
        List<Patient> all = getAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(obj.getId())) {
                all.set(i, obj);
                updated = true;
                break;
            }
        }
        if (!updated) all.add(obj);
        writeAll(all);
    }

    public void delete(String id) {
        List<Patient> all = getAll();
        all.removeIf(o -> o.getId().equals(id));
        writeAll(all);
    }

    private void writeAll(List<Patient> list) {
        List<String> lines = new ArrayList<>();
        for (Patient o : list) lines.add(o.toFileString());
        FileManager.writeAllLines(FileManager.PATIENTS_FILE, FileManager.HEADER_PATIENTS, lines);
    }

    /** Seed a demo account so login works out of the box. */
    public void seedDefaultIfEmpty() {
        if (!getAll().isEmpty()) return;
        save(new Patient("PAT-001", "Ahmad Faiz", "ahmad@email.com",
                "011-1111111", "patient123", "O+", "None", "Allianz", "012-9999999"));
        save(new Patient("PAT-002", "Siti Nurhaliza", "siti@email.com",
                "012-2222222", "patient123", "A+", "Penicillin", "Great Eastern", "013-8888888"));

    }
}
