package dao;

import model.HospitalRoom;
import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class LoadRoom implements InterfaceLoadAdmin<HospitalRoom> {

    private List<HospitalRoom> roomsList = new ArrayList<>();
    private String filePath = "data/rooms.txt";

    @Override
    public String readLoadFile() {
        roomsList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int roomDoorNumberId = Integer.parseInt(data[0].trim());
                String roomDesignatedName = data[1].trim();
                String roomDesignatedRole =  data[2].trim();
                boolean roomStatus =  Boolean.parseBoolean(data[3].trim());
                HospitalRoom hospitalRoomObject = new HospitalRoom(roomDoorNumberId, roomDesignatedName, roomDesignatedRole, roomStatus);
                roomsList.add(hospitalRoomObject);
            }
        } catch (IOException e) {
            return "Rooms file not found";
        }
        return "Rooms loaded";
    }

    @Override
    public List<HospitalRoom> getObjectList() {
        String status = readLoadFile();
        return this.roomsList; 
    }


    public int getLatestRoomId(int selectedFloorNumber) {
        int latestRoomId = 0;
        int floorNumber;
        for (HospitalRoom a : roomsList) {
            int roomId = a.getDoorNumberId();
            floorNumber = roomId / 100;
            if (selectedFloorNumber == floorNumber) {
                if (roomId > latestRoomId) {
                    latestRoomId = roomId;
                }
            }
        }
        return latestRoomId + 1;
    }
}