package service;

import dao.LoadRoom;
import dao.WriteRoom;
import java.util.ArrayList;
import java.util.List;
import model.HospitalRoom;

public class RoomService {

    private WriteRoom wR = new WriteRoom();
    private LoadRoom lR = new LoadRoom();
    private List<HospitalRoom> hospitalRoomObjectList;
    private HospitalRoom hospitalRoomObject;
    private int selectedFloorNumber;
    private List<HospitalRoom> sortedFloorRoomList;

    public RoomService() {
        this.hospitalRoomObjectList = lR.getObjectList();
    }

    public int getLatestRoomId(int selectedFloorNumber) {
        this.hospitalRoomObjectList = lR.getObjectList();
        this.selectedFloorNumber = selectedFloorNumber;
        int latestRoomId = 0;
        int floorNumber;
        for (HospitalRoom a : this.hospitalRoomObjectList) {
            int roomId = a.getDoorNumberId();
            floorNumber = roomId / 100;
            if (selectedFloorNumber == floorNumber) {
                if (roomId > latestRoomId) {
                    latestRoomId = roomId;
                }
            }
        }
        if (latestRoomId == 0) {
            latestRoomId = selectedFloorNumber * 100;
        }
        return latestRoomId + 1;
    }

    public List<HospitalRoom> getHospitalRoomList(int selectedFloorNumber) {
        this.selectedFloorNumber = selectedFloorNumber;
        this.sortedFloorRoomList = sortHospitalRoomListByFloorNumber(this.selectedFloorNumber);
        return this.sortedFloorRoomList;
    }

    private List<HospitalRoom> sortHospitalRoomListByFloorNumber(int selectedFloorNumber) {
        int floorNumber;
        this.hospitalRoomObjectList = lR.getObjectList();
        List<HospitalRoom> tempList = new ArrayList<>();
        for (HospitalRoom a: this.hospitalRoomObjectList) {
            floorNumber = a.getDoorNumberId() / 100;
            if (floorNumber == selectedFloorNumber) {
                tempList.add(a);
            }
        }
        return tempList;
    }

    public String appendFile(String roomDesignatedName, String roomDesignatedRole, boolean roomStatus) {
        int roomDoorNumberId = getLatestRoomId(this.selectedFloorNumber);
        String roomString = roomDoorNumberId + "," + roomDesignatedName  + "," + roomDesignatedRole + "," + roomStatus;
        String status = wR.writeFile(roomString);
        if (status.equals("Rooms file updated successfully")) {
            HospitalRoom updatedRoom = new HospitalRoom(roomDoorNumberId, roomDesignatedName, roomDesignatedRole, roomStatus);
            sortedFloorRoomList.add(updatedRoom);
            return status;
        } else {
            return status;
        }
    }   

    public String updateFile(String action, int selectedRoomId, String roomDesignatedName, String roomDesignatedRole, boolean roomStatus) {
        this.hospitalRoomObjectList = lR.getObjectList();
        List<HospitalRoom> tempList = new ArrayList<>();
        if (action.equals("update")) {
            for (HospitalRoom a : this.hospitalRoomObjectList) {
                if (a.getDoorNumberId() == selectedRoomId) {
                    HospitalRoom updatedRoom = new HospitalRoom(selectedRoomId, roomDesignatedName, roomDesignatedRole, roomStatus);
                    tempList.add(updatedRoom);
                } else {
                    tempList.add(a);
                }
            }
            this.hospitalRoomObjectList = tempList;
            String status = wR.writeFile(this.hospitalRoomObjectList);
            return status;
        } else if (action.equals("delete")) {
            for (HospitalRoom a: this.hospitalRoomObjectList) {
                if (a.getDoorNumberId() == selectedRoomId) {
                    continue;
                } else {
                    tempList.add(a);
                }
            }
            this.hospitalRoomObjectList = tempList;
            String status = wR.writeFile(hospitalRoomObjectList);
            return status;
        } else {
            return "Invalid Action";
        }
    }
}