package dao;

import model.HospitalRoomRole;
import java.util.List;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class WriteRoomRole {

    private String filePath = "data/room_role_list.txt";
    private String roomRoleHeader = "room_role_id,room_role_name";

    public String writeRoomRole(String roomRoleString) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(roomRoleString);
            bw.newLine();
        } catch (IOException e) {
            return "Room role list file not found";
        }
        return "Room role list file successfully updated";
    }

    public String writeRoomRole(List<HospitalRoomRole> roomRoleObjectList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            bw.write(roomRoleHeader);
            bw.newLine();
            for (HospitalRoomRole a : roomRoleObjectList) {
                bw.write(a.getRoomRoleId() + "," + a.getRoomRoleName());
                bw.newLine();
            }
        } catch (IOException e) {
            return "Room role list file not found";
        }
        return "Room role list updated successfully";
    }
}