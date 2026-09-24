package dao;

import model.HospitalAsset;
import dao.LoadRoom;
import model.HospitalRoom;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class LoadAssetsInRoom {

    private List<HospitalRoom> hospitalRoomsList = new ArrayList<>(); 
    private List<HospitalAsset> assetInRoomList = new ArrayList<>();
    private LoadRoom roomLoader = new LoadRoom();
    private String filePath;
    private String fileName;
    private int roomId;
    private String roomDesignatedRole;

    public String loadAsset() {
        assetInRoomList.clear();
        roomLoader.loadRoom();
        this.hospitalRoomsList = roomLoader.getRoomsList();
        for (HospitalRoom i : this.hospitalRoomsList) {
            this.roomDesignatedRole = i.getRoomRole();
            this.roomId = i.getDoorNumberId();
            this.roomDesignatedRole = i.getRoomRole();
            this.filePath = "data/assets_in_room" + this.roomId + "_" + this.roomDesignatedRole.replaceAll("\\s+", "_").toLowerCase() + ".txt";
            String line;
            try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
                line = br.readLine();
                while ((line = br.readLine() != null)) {
                    String[] data = line.split(",");
                    int assetId = Integer.parseInt(data[0].trim());
                    String assetName = data[1].trim();
                    int assetAcquisitionYear = Integer.parseInt(data[2].trim());
                    int assetAcquisitionMonth = Integer.parseInt(data[3].trim());
                    int assetAcquisitionDay = Integer.parseInt(data[4].trim());
                    int assetAtRoomId = Integer.parseInt(data[5].trim());
                    int assetMaintenanceId = Integer.parseInt(data[6].trim());
                    if ((assetAcquisitionYear == 2026) && (assetAcquisitionMonth == 9) && (assetAcquisitionDay == 1)) {
                        HospitalAsset hospitalAssetObject = new HospitalAsset(assetId, assetName, assetAtRoomId, assetMaintenanceId);
                        assetInRoomList.add(hospitalAssetObject);
                    } else {
                        HospitalAsset hospitalAssetObject = new HospitalAsset(assetId, assetName, assetAcquisitionYear, assetAcquisitionMonth, assetAcquisitionDay, assetAtRoomId, assetMaintenanceId);
                        assetInRoomList.add(hospitalAsetObject);
                    }
                }
            } catch (IOException e) {
                return "Asset in room file not found: " + this.filePath;
            }
        }
        return "Asssets in room loaded successfully";

    }

    public List<HospitalAsset> getAssetInRoomList() {
        return this.assetInRoomList;
    }
}