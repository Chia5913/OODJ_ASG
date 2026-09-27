package service;

import java.util.List;
import java.util.ArrayList;
import dao.WriteRoomRole;
import dao.LoadRoomRoleList;
import model.HospitalRoomRole;

public class RoomRoleService {

    private WriteRoomRole wRR = new WriteRoomRole();
    private LoadRoomRoleList lRR = new LoadRoomRoleList();
    private HospitalRoomRole hospitalRoomRoleObject;
    private List<HospitalRoomRole> hospitalRoomRoleObjectList;

    public RoomRoleService() {
        this.hospitalRoomRoleObjectList = lRR.getObjectList();
    }

    public int getRoomRoleLatestId() {
        int latestRoomRoleId = 0;
        for (HospitalRoomRole a: this.hospitalRoomRoleObjectList) {
            if (a.getRoomRoleId() > latestRoomRoleId) {
                latestRoomRoleId = a.getRoomRoleId();
            }
        }
        return latestRoomRoleId + 1;
    }

    public List<HospitalRoomRole> getRoomRoleList() {
        this.hospitalRoomRoleObjectList = lRR.getObjectList();
        return  this.hospitalRoomRoleObjectList;
    }

    public String appendFile(int roomRoleId, String roomRoleName) {
        String roomRoleString = roomRoleId + "," + roomRoleName;
        String status = wRR.writeFile(roomRoleString);
        if (status.equals("Room role list file successfully updated")) {
            HospitalRoomRole updatedRoomRole = new HospitalRoomRole(roomRoleId, roomRoleName);
            this.hospitalRoomRoleObjectList.add(updatedRoomRole);
        } else {
            return status;
        }
    }

    public String updateFile(String action, int selectedRoomRoleId, String roomRoleName) {
        this.hospitalRoomRoleObjectList = lRR.getObjectList();
        List<HospitalRoomRole> tempList = new ArrayList<>();
        if (action.equals("update")) {
            for (HospitalRoomRole a : this.hospitalRoomRoleObjectList) {
            if (a.getRoomRoleId() == selectedRoomId) {
                HospitalRoomRole updatedRoomRole = new HospitalRoomRole(selectedRoomRoleId, roomRoleName);
                tempList.add(updatedRoomRole);
            } else {
                tempList.add(a);
            }
            this.hospitalRoomRoleObjectList = tempList;
            String status = wRR.writeFile(this.hospitalRoomRoleObjectList);
            return status;
        }
        } else if (action.equals("delete")) {
            for (HospitalRoomRole a : this.hospitalRoomRoleObjectList) {
                if (a.getRoomRoleId() == selectedRoomRoleId) {
                    continue;
                } else {
                    tempList.add(a);
                }
            }
            this.hospitalRoomRoleObjectList = tempList;
            String status = wRR.writeFile(this.hospitalRoomRoleObjectList);
            return status;            
        } else {
            return "Invalid action";
        }
    }
}