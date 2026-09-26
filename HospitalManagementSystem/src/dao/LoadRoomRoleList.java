package dao;

import model.HospitalRoomRole;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadRoomRoleList implements InterfaceLoadAdmin<HospitalRoomRole>{

    private String filePath = "data/room_role_list.txt";
    private List<HospitalRoomRole> roomRoleObjectList = new ArrayList<>();
    
    @Override
    public String readLoadFile() {
        String line;
        this.roomRoleObjectList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while ((line=br.readLine()) != null) {
                String[] data = line.split(",");
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

    @Override
    public List<HospitalRoomRole> getObjectList() {
        String status = readLoadFile();
        return this.roomRoleObjectList;
    }

    public int getRoomRoleLatestId() {
        int latestRoomRoleId = 0;
        for (HospitalRoomRole a: roomRoleObjectList) {
            if (a.getRoomRoleId() > latestRoomRoleId) {
                latestRoomRoleId = a.getRoomRoleId();
            }
        }
        return latestRoomRoleId + 1;
    }
}