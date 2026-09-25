package dao;

import java.util.List;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import model.HospitalAssetMaintenance;

public class WriteMaintenance {

    private String filePath = "data/asset_maintenances.txt";
    private String maintenanceHeader = "asset_maintenance_id,asset_maintenance_name";
    private HospitalAssetMaintenance maintenanceObjectList;

    public String writeMaintenance(String maintenanceString) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(maintenanceString);
            bw.newLine();

        } catch (IOException e) {
            return "Asset maintenance file not found";
        }
        return "Asset maintenance updated";
    }

    public String writeMaintenance(List<HospitalAssetMaintenance> maintenanceObjectList) {
        this.maintenanceObjectList = maintenanceObjectList;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            bw.write(maintenanceHeader);
            bw.newLine();
            for (HospitalAssetMaintenance a : this.maintenanceObjectList) {
                bw.write(a.getAssetMaintenanceId() + "," + a.getAssetMaintenanceName())
                bw.newLine();
            }
        } catch (IOException e) {
            return "Asset maintenance file not found";
        }
        return "Asset Maintenance file not found";
    }
}