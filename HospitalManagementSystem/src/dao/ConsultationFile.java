package dao;

import model.Consultation;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultationFile {

    public List<Consultation> getAll() {
        List<Consultation> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.CONSULTATIONS_FILE)) {
            Consultation c = Consultation.fromFileString(line);
            if (c != null) list.add(c);
        }
        return list;
    }

    public List<Consultation> getByDoctorId(String doctorId) {
        return getAll().stream()
                .filter(c -> c.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public List<Consultation> getByPatientId(String patientId) {
        return getAll().stream()
                .filter(c -> c.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public Consultation findById(String id) {
        for (Consultation c : getAll()) {
            if (c.getConsultationId().equals(id)) return c;
        }
        return null;
    }

    public void save(Consultation consultation) {
        List<Consultation> all = getAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getConsultationId().equals(consultation.getConsultationId())) {
                all.set(i, consultation);
                updated = true;
                break;
            }
        }
        if (!updated) {
            all.add(consultation);
        }
        writeAll(all);
    }

    private void writeAll(List<Consultation> list) {
        List<String> lines = new ArrayList<>();
        for (Consultation c : list) {
            lines.add(c.toFileString());
        }
        FileManager.writeAllLines(FileManager.CONSULTATIONS_FILE, FileManager.HEADER_CONSULTATIONS, lines);
    }
}
