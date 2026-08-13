package dao;

import model.Department;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Department – text-file persistence.
 * Teammate: add filters as needed.
 */
public class DepartmentFile {

    public List<Department> getAll() {
        List<Department> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.DEPARTMENTS_FILE)) {
            Department obj = Department.fromFileString(line);
            if (obj != null) list.add(obj);
        }
        return list;
    }

    public Department findById(String id) {
        for (Department o : getAll()) {
            if (o.getDepartmentId().equals(id)) return o;
        }
        return null;
    }

    public void save(Department obj) {
        List<Department> all = getAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getDepartmentId().equals(obj.getDepartmentId())) {
                all.set(i, obj);
                updated = true;
                break;
            }
        }
        if (!updated) all.add(obj);
        writeAll(all);
    }

    public void delete(String id) {
        List<Department> all = getAll();
        all.removeIf(o -> o.getDepartmentId().equals(id));
        writeAll(all);
    }

    private void writeAll(List<Department> list) {
        List<String> lines = new ArrayList<>();
        for (Department o : list) lines.add(o.toFileString());
        FileManager.writeAllLines(FileManager.DEPARTMENTS_FILE, FileManager.HEADER_DEPARTMENTS, lines);
    }
}
