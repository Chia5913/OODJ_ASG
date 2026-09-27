package service;

import java.util.List;
import java.util.ArrayList;
import dao.WriteAssetsInRoom;
import dao.LoadAssetsInRoom;
import model.HospitalAsset;
import model.HospitalRoom;
import dao.LoadRoom;


public class RoomAssetsService {

    private List<HospitalRoom> roomList;
    private List<HospitalAsset> assetList;
    private List<HospitalAsset> chosenRoomAssetList;
    private LoadAssetsInRoom lAIR = new LoadAssetsInRoom();
    private WriteAssetsInRoom wAIR = new WriteAssetsInRoom();
    private LoadRoom lR = new LoadRoom();
    private int selectedRoomId;
    private String filePath;

    public RoomAssetsService() {
        this.assetList = lAIR.getObjectList();
        this.roomList = lR.getObjectList();
    }

    public int getLatestAssetIdInRoom(int roomSelectedId) {
        this.assetList = lAIR.getObjectList();
        int latestAssetId = 0;
        for (HospitalAsset a: this.assetList) {
            int roomId = a.getAssetAtRoomId();
            if (roomId == roomSelectedId) {
                if (a.getAssetId() > latestAssetId) {
                    latestAssetId = a.getAssetId();
                }
            }
        }
        return latestAssetId + 1;
    }

    public List<HospitalAsset> getAssetInRoomList(int selectedRoomId) {
        this.selectedRoomId = selectedRoomId;
        this.chosenRoomAssetList = sortAssetForRoom(selectedRoomId);
        return this.chosenRoomAssetList;
    }

    private List<HospitalAsset> sortAssetForRoom(int selectedRoomId) {
        this.selectedRoomId = selectedRoomId;
        List<HospitalAsset> tempAssetList = new ArrayList<>();
        this.assetList = lAIR.getObjectList();
        for (HospitalAsset a: this.assetList) {
            if (a.getAssetAtRoomId() == this.selectedRoomId) {
                tempAssetList.add(a);
            }
        }
        return tempAssetList;
    }

    public String appendFile(String assetName, int assetAcquisitionYear, int assetAcquisitionMonth, int assetAcquisitionDay, int assetAtRoomId) {
        int assetId = getLatestAssetIdInRoom(this.selectedRoomId);
        this.chosenRoomAssetList = sortAssetForRoom(this.selectedRoomId);
        this.roomList = lR.getObjectList();
        for (HospitalRoom a : this.roomList) {
            if (a.getDoorNumberId() == this.selectedRoomId) {
                this.filePath = "data/assets_in_room/" + a.getDoorNumberId() + "_" + a.getRoomRole().replaceAll("\\s+", "_").toLowerCase() + ".txt";
            }
        }
        String assetInRoomString = assetId + "," + assetName +"," + assetAcquisitionYear + "," + assetAcquisitionMonth + "," + assetAcquisitionDay + "," + assetAtRoomId;
        String status = wAIR.writeFile(assetInRoomString, filePath);
        if (status.equals("Asset in room file successfully updated")) {
            HospitalAsset hA = new HospitalAsset(assetId, assetName, assetAcquisitionYear, assetAcquisitionMonth, assetAcquisitionDay, assetAtRoomId);
            this.chosenRoomAssetList.add(hA);
            return status;
        } else {
            return status;
        }
    }

    public String updateFile(String action, int selectedAssetId, String assetName, int year, int month, int day) {
        this.roomList = lR.getObjectList();
        this.chosenRoomAssetList = sortAssetForRoom(this.selectedRoomId);
        for (HospitalRoom a : this.roomList) {
            if (a.getDoorNumberId() == this.selectedRoomId) {
                this.filePath = "data/assets_in_room/" + a.getDoorNumberId() + "_" + a.getRoomRole().replaceAll("\\s+", "_").toLowerCase() + ".txt";
            }
        }
        List<HospitalAsset> tempAssetList = new ArrayList<>();
        if (action.equals("update")) {
            for (HospitalAsset a : this.chosenRoomAssetList) {
                if (a.getAssetId() == selectedAssetId) {
                    HospitalAsset updatedObj = new HospitalAsset(selectedAssetId, assetName, year, month, day, this.selectedRoomId);
                    tempAssetList.add(updatedObj);
                } else {
                    tempAssetList.add(a);
                }
            }
            this.chosenRoomAssetList = tempAssetList;
            String status = wAIR.writeFile(this.chosenRoomAssetList, this.filePath);
            return status;
            
        } else if (action.equals("delete")) {
            for (HospitalAsset a : this.chosenRoomAssetList) {
                if (a.getAssetId() == selectedAssetId) {
                    continue;
                } else {
                    tempAssetList.add(a);
                }
            }
            this.chosenRoomAssetList = tempAssetList;
            String status = wAIR.writeFile(this.chosenRoomAssetList, this.filePath);
            return status;
        } else {
            return "Invalid action";
        }
    }
}
