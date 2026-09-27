package service;

import java.util.List;
import java.util.ArrayList;
import dao.LoadMaintenance;
import dao.WriteMaintenance;
import model.HospitalAssetMaintenance;

public class AssetsMaintenanceService {

    private List<HospitalAssetMaintenance> maintenanceList;
    private LoadMaintenance lM = new LoadMaintenance();
    private WriteMaintenance wM = new WriteMaintenance();
    private HospitalAssetMaintenance maintenanceObject;

    public AssetsMaintenanceService() {
        this.maintenanceList = lM.getObjectList();
    }

    public int getLatestMaintenanceId() {
        this.maintenanceList = lM.getObjectList();
        int latestId = 0;
        for (HospitalAssetMaintenance a : this.maintenanceList) {
            if (a.getAssetMaintenanceId() > latestId) {
                latestId = a.getAssetMaintenanceId();
            }
        }
        return latestId + 1;
    }

    public List<HospitalAssetMaintenance> getMaintenanceList() {
        this.maintenanceList = lM.getObjectList();
        return this.maintenanceList;
    }

    public String appendMaintenanceList(String assetMaintenanceName) { 
        int assetMaintenanceId = getLatestMaintenanceId();
        String maintenanceString = assetMaintenanceId + "," + assetMaintenanceName;
        String status = wM.writeFile(maintenanceString);
        if (status.equals("Asset Maintenance file updated")) {
            this.maintenanceObject = new HospitalAssetMaintenance(assetMaintenanceId, assetMaintenanceName);
            this.maintenanceList.add(this.maintenanceObject);
        }
        return status;
    }

    public String updateMaintenanceList(String action, int selectedAssetMaintenanceId, String assetMaintenanceName) {
        this.maintenanceList = lM.getObjectList();
        List<HospitalAssetMaintenance> tempAssetList = new ArrayList<>();
        if (action.equals("update")) {
            for (HospitalAssetMaintenance a : this.maintenanceList) {
                if (selectedAssetMaintenanceId == a.getAssetMaintenanceId()) {
                    this.maintenanceObject = new HospitalAssetMaintenance(selectedAssetMaintenanceId, assetMaintenanceName);
                    tempAssetList.add(maintenanceObject);
                } else {
                    tempAssetList.add(a);
                }
            }
            this.maintenanceList = tempAssetList;
            wM.writeFile(this.maintenanceList);
            return "Asset maintenance list updated";
        } else if (action.equals("delete")) {
            for (HospitalAssetMaintenance a : this.maintenanceList) {
                if (a.getAssetMaintenanceId() == selectedAssetMaintenanceId) {
                    continue;
                } else {
                    tempAssetList.add(a);
                }
            }
            this.maintenanceList = tempAssetList;
            wM.writeFile(this.maintenanceList);
            return "Asset maintenance list updated";
        } else {
            return "Action not supported";
        }
    }
}