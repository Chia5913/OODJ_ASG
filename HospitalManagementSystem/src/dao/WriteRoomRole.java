package dao;

import model.HospitalRoomRole;
import java.util.List;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class WriteRoomRole implements InterfaceWriteAdmin<HospitalRoomRole>{

    private String filePath = "data/room_role_list.txt";
    private String roomRoleHeader = "room_role_id,room_role_name";

    @Override
    public String writeFile(String roomRoleString) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(roomRoleString);
            bw.newLine();
        } catch (IOException e) {
            return "Room role list file not found";
        }
        return "Room role list file successfully updated";
    }

    @Override
    public String writeFile(List<HospitalRoomRole> roomRoleObjectList) {
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

    @Override
    public String writeFile(String x, String filePath) {
        return "This action is not supported";
    }

    @Override
    public String writeFile(List<HospitalRoomRole> x, String filePath) {
        return "This action is not supported";
    }
}