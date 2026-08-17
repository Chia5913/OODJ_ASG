package dao;

import model.Department;
import model.ShiftRoster;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for ShiftRoster.
 * Handles text-file storage and retrieval.
 */
public class ShiftRosterFile {

    //Read all shift rosters.
    public List<ShiftRoster> getAll() {
        List<ShiftRoster> list = new ArrayList<>();

        for (String line : FileManager.readAllLines(FileManager.SHIFT_ROSTERS_FILE)) {
            ShiftRoster roster = ShiftRoster.fromFileString(line);

            if (roster != null) {
                list.add(roster);
            }
        }

        return list;
    }

    //Find one shift roster by Roster ID.
    public ShiftRoster findById(String rosterId) {

        for (ShiftRoster roster : getAll()) {

            if (roster.getRosterId().equalsIgnoreCase(rosterId)) {
                return roster;
            }
        }

        return null;
    }

    //Find all rosters belonging to one doctor.
    public List<ShiftRoster> findByDoctorId(String doctorId) {

        List<ShiftRoster> matchingRosters =
                new ArrayList<>();

        for (ShiftRoster roster :
                getAll()) {

            if (roster.getDoctorId()
                    .equalsIgnoreCase(
                            doctorId)) {

                matchingRosters.add(
                        roster
                );
            }
        }

        return matchingRosters;
    }

    //Find all rosters belonging to one department.
    public List<ShiftRoster> findByDepartmentId(String departmentId) {

        List<ShiftRoster> matchingRosters = new ArrayList<>();

        for (ShiftRoster roster : getAll()) {

            if (roster.getDepartmentId().equalsIgnoreCase(departmentId)) {
                matchingRosters.add(roster);
            }
        }

        return matchingRosters;
    }

    //Find all shift rosters that belong to departments managed by one Medical Manager.
    public List<ShiftRoster> findByManagerId(String managerId) {

        List<ShiftRoster> matchingRosters = new ArrayList<>();

        DepartmentFile departmentFile = new DepartmentFile();

        for (ShiftRoster roster : getAll()) {

            Department department = departmentFile.findById(roster.getDepartmentId());

            if (department != null && department.getManagerId().equalsIgnoreCase(managerId)) {
                matchingRosters.add(roster);
            }
        }

        return matchingRosters;
    }

    //Save a shift roster.
    //If Roster ID already exists: update the existing record.
    //If Roster ID does not exist: add a new record.
    public void save(ShiftRoster obj) {

        List<ShiftRoster> all = getAll();
        boolean updated = false;

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getRosterId().equalsIgnoreCase(obj.getRosterId())) {
                all.set(i, obj);
                updated = true;
                break;
            }
        }

        if (!updated) {
            all.add(obj);
        }

        writeAll(all);
    }

    //Delete one roster by Roster ID.
    public void delete(String rosterId) {

        List<ShiftRoster> all = getAll();
        all.removeIf(roster -> roster.getRosterId().equalsIgnoreCase(rosterId));

        writeAll(all);
    }

    //Rewrite all roster records into shift_rosters.txt.
    private void writeAll(List<ShiftRoster> list) {

        List<String> lines = new ArrayList<>();

        for (ShiftRoster roster : list) {
            lines.add(roster.toFileString());
        }

        FileManager.writeAllLines( FileManager.SHIFT_ROSTERS_FILE, FileManager.HEADER_SHIFT_ROSTERS, lines);
    }
}