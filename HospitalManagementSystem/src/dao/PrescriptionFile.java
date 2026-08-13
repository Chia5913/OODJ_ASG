package dao;

import model.Prescription;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionFile {

    public List<Prescription> getAll() {
        List<Prescription> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.PRESCRIPTIONS_FILE)) {
            Prescription p = Prescription.fromFileString(line);
            if (p != null) list.add(p);
        }
        return list;
    }

    public List<Prescription> getByDoctorId(String doctorId) {
        return getAll().stream()
                .filter(p -> p.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public List<Prescription> getByPatientId(String patientId) {
        return getAll().stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public void save(Prescription prescription) {
        List<Prescription> all = getAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPrescriptionId().equals(prescription.getPrescriptionId())) {
                all.set(i, prescription);
                updated = true;
                break;
            }
        }
        if (!updated) {
            all.add(prescription);
        }
        writeAll(all);
    }

    private void writeAll(List<Prescription> list) {
        List<String> lines = new ArrayList<>();
        for (Prescription p : list) {
            lines.add(p.toFileString());
        }
        FileManager.writeAllLines(FileManager.PRESCRIPTIONS_FILE, FileManager.HEADER_PRESCRIPTIONS, lines);
    }
}
