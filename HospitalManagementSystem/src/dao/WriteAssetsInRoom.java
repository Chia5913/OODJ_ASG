package dao;

import model.HospitalAsset;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.List;

public class WriteAssetsInRoom {

    private String filePath;
    private String assetInRoomHeader = "asset_id,asset_name,asset_acquisition_year,asset_acquisition_month,asset_acquisition_day,asset_at_room_id,asset_maintenance_id";
    
    public String writeAssetInRoom(String assetInRoomString , String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(assetInRoomString);
            bw.newLine();
        } catch (IOException e) {
            return "Asset in room file not found";
        }
        return "Asset in room file found";
    }

    public String writeAssetInRoom(List<HospitalAsset> assetInRoomObjectList, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            bw.write(assetInRoomHeader);
            bw.newLine();
            for (HospitalAsset a: assetInRoomObjectList) {
                bw.write(a.getAssetId() + "," + a.getAssetName() + "," + a.getAssetYear() + "," + a.getAssetMonth() + "," + a.getAssetDay() + "," + a.getAssetAtRoomId() + "," + a.getAssetMaintenanceId());
                bw.newLine();
            }
        } catch (IOException e) {
            return "Asset in room file not found";
        }
        return "Asset in room file successfully updated";
    }
}