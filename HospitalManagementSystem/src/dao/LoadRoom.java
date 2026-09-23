package dao;

import model.HospitalRoom;
import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;

public class LoadRoom {

    private List<HospitalRoom> roomsList = new ArrayList<>();
    private String filePath = "data/rooms.txt";

    public String loadRoom() {
        roomsList.clear();
        String line;
        try () {
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int roomDoorNumberId = Integer.parseInt(data[0].trim());
                String roomDesignatedName = data[1].trim();
                String roomDeisgnatedRole =  data[2].trim();
                boolean roomStatus =  Boolean.parseBoolean(data[3].trim());
                HospitalRoom hospitalRoomObject = new HospitalRoom(roomDoorNumberId, roomDeisgnatedName, roomDesignatedRole, roomStatus);
                roomsList.add(hospitalRoomObject);
            }
        } catch (IOException e) {
            return "Rooms file not found";
        }
        return "Rooms loaded";
    }

    public List<HospitalRoom> getRoomsList() {
        return this.roomsList; 
    }
}