package model;

public class HospitalAssetMaintenance {

    private int assetMaintenanceId;
    private String assetMaintenanceName;
    
    public HospitalAssetMaintenance(int assetMaintenanceId, String assetMaintenanceName) {
        this.assetMaintenanceId = assetMaintenanceId;
        this.assetMaintenanceName = assetMaintenanceName;
    }

    public int getAssetMaintenanceId() {
        return this.assetMaintenanceId;
    }

    public Strng getAssetMaintenanceName() {
        return this.assetMaintenanceName;
    }
}