package dao;

import model.AdminStaff;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for AdminStaff – text-file persistence.
 * Teammate: add query helpers as needed; keep save/getAll/findById pattern.
 */
public class AdminFile {

    public List<AdminStaff> getAll() {
        List<AdminStaff> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.ADMINS_FILE)) {
            AdminStaff obj = AdminStaff.fromFileString(line);
            if (obj != null) list.add(obj);
        }
        return list;
    }

    public AdminStaff findById(String id) {
        for (AdminStaff o : getAll()) {
            if (o.getId().equalsIgnoreCase(id)) return o;
        }
        return null;
    }

    public AdminStaff findByEmail(String email) {
        for (AdminStaff o : getAll()) {
            if (o.getEmail().equalsIgnoreCase(email)) return o;
        }
        return null;
    }

    public AdminStaff authenticate(String email, String password) {
        AdminStaff o = findByEmail(email);
        if (o != null && o.getPassword().equals(password) && o.isActive()) {
            return o;
        }
        return null;
    }

    public void save(AdminStaff obj) {
        List<AdminStaff> all = getAll();
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
        List<AdminStaff> all = getAll();
        all.removeIf(o -> o.getId().equals(id));
        writeAll(all);
    }

    private void writeAll(List<AdminStaff> list) {
        List<String> lines = new ArrayList<>();
        for (AdminStaff o : list) lines.add(o.toFileString());
        FileManager.writeAllLines(FileManager.ADMINS_FILE, FileManager.HEADER_ADMINS, lines);
    }

    /** Seed a demo account so login works out of the box. */
    public void seedDefaultIfEmpty() {
        if (!getAll().isEmpty()) return;
        save(new AdminStaff("ADM-001", "Admin User", "admin@hms.com",
                "010-1111111", "admin123", "Records"));

    }
}
