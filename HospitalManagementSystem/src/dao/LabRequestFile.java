package dao;

import model.LabRequest;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LabRequestFile {

    public List<LabRequest> getAll() {
        List<LabRequest> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.LAB_REQUESTS_FILE)) {
            LabRequest lr = LabRequest.fromFileString(line);
            if (lr != null) list.add(lr);
        }
        return list;
    }

    public List<LabRequest> getByDoctorId(String doctorId) {
        return getAll().stream()
                .filter(lr -> lr.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public List<LabRequest> getByPatientId(String patientId) {
        return getAll().stream()
                .filter(lr -> lr.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public void save(LabRequest request) {
        List<LabRequest> all = getAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getRequestId().equals(request.getRequestId())) {
                all.set(i, request);
                updated = true;
                break;
            }
        }
        if (!updated) {
            all.add(request);
        }
        writeAll(all);
    }

    private void writeAll(List<LabRequest> list) {
        List<String> lines = new ArrayList<>();
        for (LabRequest lr : list) {
            lines.add(lr.toFileString());
        }
        FileManager.writeAllLines(FileManager.LAB_REQUESTS_FILE, FileManager.HEADER_LAB_REQUESTS, lines);
    }
}
