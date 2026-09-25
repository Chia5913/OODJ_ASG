package dao;

import model.HospitalRoom;
import java.util.List;
import java.io.FileWriiter;
import java.io.BufferedWriter;
import java.io.IOException;

public class WriteRoom {

    private String filePath = "data/rooms.txt";
    private List<HospitalRoom> roomObjectList;

    public String writeRoom(String roomString){
        try (BufferedWriter bw = new BufferedWriter (new FileWriter(filePath, true))) {
            bw.write(roomString);
            bw.newLine();
        } catch (IOException e) {
            return "File rooms.txt not found";
        }
        return "Rooms file updated successfully";
    }

    public String writeRoom(List<HospitalRoom> roomObjectList) {
        LoadRoom roomLoader = new LoadRoom();
        String loadRoomStatus = roomLoader.loadRoom();
        String roomHeader = roomLoader.getRoomHeader();
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
}