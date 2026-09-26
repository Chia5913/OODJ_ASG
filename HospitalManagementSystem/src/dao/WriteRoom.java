package dao;

import model.HospitalRoom;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class WriteRoom implements InterfaceWriteAdmin<HospitalRoom> {

    private String filePath = "data/rooms.txt";
    private List<HospitalRoom> roomObjectList;
    private String roomHeader = "room_door_number_id,room_designated_name,room_role,room_status";

    @Override 
    public String writeFile(String roomString){
        try (BufferedWriter bw = new BufferedWriter (new FileWriter(filePath, true))) {
            bw.write(roomString);
            bw.newLine();
        } catch (IOException e) {
            return "File rooms.txt not found";
        }
        return "Rooms file updated successfully";
    }

    @Override
    public String writeFile(List<HospitalRoom> roomObjectList) {
        this.roomObjectList = roomObjectList;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            bw.write(roomHeader);
            bw.newLine();
            for (HospitalRoom a : roomObjectList) {
                bw.write(a.getDoorNumberId() + "," + a.getRoomName() + "," + a.getRoomRole() + "," + a.getRoomStatus());
                bw.newLine();
            }
        } catch (IOException e) {
            return "Room file not found";
        }
        return "Room file updated successfully.";
    }

    @Override
    public String writeFile(String x, String filePath) {
        return "This action is not supported";
    }

    @Override
    public String writeFile(List<HospitalRoom> x, String filePath) {
        return "This action is not supported";
    }
}