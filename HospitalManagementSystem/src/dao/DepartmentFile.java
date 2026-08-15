package dao;

import model.Department;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

//DAO for Department – text-file persistence
public class DepartmentFile {

    //Read all departments.
    public List<Department> getAll() {

        List<Department> list = new ArrayList<>();

        for (String line : FileManager.readAllLines(FileManager.DEPARTMENTS_FILE)) {
            Department obj = Department.fromFileString(line);
            if (obj != null) {
                list.add(obj);
            }
        }

        return list;
    }

    //Find one department using Department ID
    public Department findById(String id) {

        for (Department department : getAll()) {
            if (department.getDepartmentId().equalsIgnoreCase(id)) {
                return department;
            }
        }

        return null;
    }

    //NEW: Find all departments managed by one Medical Manager
    public List<Department> findByManagerId(String managerId) {

        List<Department> matchingDepartments = new ArrayList<>();

        for (Department department : getAll()) {
            if (department.getManagerId().equalsIgnoreCase(managerId)) {
                matchingDepartments.add(department);
            }
        }

        return matchingDepartments;
    }

    //Add a new department or update an existing department.
    public void save(Department obj) {

        List<Department> all = getAll();

        boolean updated = false;

        for (int i = 0; i < all.size(); i++) {

            if (all.get(i).getDepartmentId().equalsIgnoreCase(obj.getDepartmentId())) {
                all.set(i, obj);
                updated = true;
                break;
            }
        }

        //If Department ID does not exist, add it as a new department
        if (!updated) {

            all.add(obj);
        }

        writeAll(all);
    }

    //Delete a department using ID
    public void delete(String id) {

        List<Department> all = getAll();
        all.removeIf(department -> department.getDepartmentId().equalsIgnoreCase(id));
        writeAll(all);
    }

    //Rewrite all department records into departments.txt
    private void writeAll(List<Department> list) {

        List<String> lines = new ArrayList<>();

        for (Department department : list) {
            lines.add(department.toFileString());
        }

        FileManager.writeAllLines(FileManager.DEPARTMENTS_FILE, FileManager.HEADER_DEPARTMENTS, lines);
    }
}