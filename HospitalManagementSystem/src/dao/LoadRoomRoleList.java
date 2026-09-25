package dao;

import model.HospitalRoomRole;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class LoadRoomRoleList {

    private String filePath = "data/room_role_list.txt";
    private List<HospitalRoomRole> roomRoleObjectList = new ArrayList<>();
    
    public String loadRoomRole() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while ((line=br.readLine()) != null) {
                String[] data = br.split(",");
                int roomRoleId = Integer.parseInt(data[0].trim());
                String roomRoleName = data[1].trim();
                HospitalRoomRole roomRole = new HospitalRoomRole(roomRoleId, roomRoleName);
                roomRoleObjectList.add(roomRole);
            }
        } catch (IOException e) {
            return "Room role file not found";
        }
        return "Room role list loaded successfully";
    }

    public List<HospitalRoomRole> getRoomRoleObjectList() {
        return this.roomRoleObjectList;
    }
}