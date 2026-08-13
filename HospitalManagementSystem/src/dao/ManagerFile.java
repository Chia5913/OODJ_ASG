package dao;

import model.MedicalManager;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for MedicalManager – text-file persistence.
 * Teammate: add query helpers as needed; keep save/getAll/findById pattern.
 */
public class ManagerFile {

    public List<MedicalManager> getAll() {
        List<MedicalManager> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.MANAGERS_FILE)) {
            MedicalManager obj = MedicalManager.fromFileString(line);
            if (obj != null) list.add(obj);
        }
        return list;
    }

    public MedicalManager findById(String id) {
        for (MedicalManager o : getAll()) {
            if (o.getId().equalsIgnoreCase(id)) return o;
        }
        return null;
    }

    public MedicalManager findByEmail(String email) {
        for (MedicalManager o : getAll()) {
            if (o.getEmail().equalsIgnoreCase(email)) return o;
        }
        return null;
    }

    public MedicalManager authenticate(String email, String password) {
        MedicalManager o = findByEmail(email);
        if (o != null && o.getPassword().equals(password) && o.isActive()) {
            return o;
        }
        return null;
    }

    public void save(MedicalManager obj) {
        List<MedicalManager> all = getAll();
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
        List<MedicalManager> all = getAll();
        all.removeIf(o -> o.getId().equals(id));
        writeAll(all);
    }

    private void writeAll(List<MedicalManager> list) {
        List<String> lines = new ArrayList<>();
        for (MedicalManager o : list) lines.add(o.toFileString());
        FileManager.writeAllLines(FileManager.MANAGERS_FILE, FileManager.HEADER_MANAGERS, lines);
    }

    /** Seed a demo account so login works out of the box. */
    public void seedDefaultIfEmpty() {
        if (!getAll().isEmpty()) return;
        save(new MedicalManager("MGR-001", "Dr. Manager Lee", "manager@hms.com",
                "010-2222222", "manager123", "DEPT-CARD"));

    }
}
