package dao;

import model.HospitalAssetMaintenance;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class LoadMaintenance {

    private String filePath = "data/asset_maintenances.txt";
    private List<HospitalAssetMaintenance> maintenanceList = new ArrayList<>();
    private String line;

    public String loadMaintenace() {
        maintenanceList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int assetMaintenanceId = Integer.parseInt(data[0].trim());
                String assetMaintenanceName = data[1].trim();
                HospitalAssetMaintenance ham = new HospitalAssetMaintenance(assetMaintenanceId, assetMaintenanceName);
                maintenanceList.add(ham);
            }
        } catch (IOException e) {
            return "Asset maintenance file not found";
        }
        return "Asset maintenance file loaded";
    }

    public List<HospitalAssetMaintenance> getMaintenanceObjectList() {
        return this.maintenanceList;
    }

    public int getLatestMaintenanceId() {
        int latestId = 0;
        for (HospitalAssetMaintenance a : maintenanceList) {
            if (a.getAssetMaintenanceId() > latestId) {
                latestId = a.getAssetMaintenanceId();
            }
        }
        return latestId + 1;
    }
}