package model;

import java.time.LocalDate;

public class HospitalAsset {

    private int assetId;
    private String assetName;
    private int assetAcquisitionYear;
    private int assetAcquisitionMonth;
    private int assetAcquisitionDay;
    private int assetAtRoomId;
    private int assetMaintenanceId;
    private LocalDate assetAcquisitionDate;

    public HospitalAsset(int assetId, String assetName, int assetAcquisitionYear, int assetAcquisitionMonth, int assetAcquisitionDay, int assetAtRoomId, int assetMaintenanceId) {
        this.assetId = assetId;
        this.assetName = assetName;
        this.assetAcquisitionYear = assetAcquisitionYear;
        this.assetAcquisitionMonth = assetAcquisitionMonth;
        this.assetAcquisitionDay = assetAcquisitionDay;
        this.assetAtRoomId = assetAtRoomId;
        this.assetMaintenanceId = assetMaintenanceId;
        this.assetAcquisitionDate = LocalDate.of(this.assetAcquisitionYear, this.assetAcquisitionMonth, this.assetAcquisitionDay);

    }

    /*Overloading to give asset acquisition date a default placeholder if none is provided*/
    public HospitalAsset(int assetId, String assetName, int assetAtRoomId, int assetMaintenanceId) {
        this.assetId = assetId;
        this.assetName = assetName;
        this.assetAcquisitionYear = 2026;
        this.assetAcquisitionMonth = 9;
        this.assetAcquisitionDay = 1;
        this.assetAtRoomId = assetAtRoomId;
        this.assetMaintenanceId = assetMaintenanceId;
        this.assetAcquisitionDate = LocalDate.of(this.assetAcquisitionYear, this.assetAcquisitionMonth, this.assetAcquisitionDay);

    }

    public int getAssetId() {
        return this.assetId;
    }

    public String getAssetName() {
        return this.assetName;
    }

    public int getAssetYear() {
        return this.assetAcquisitionYear;
    }

    public int getAssetMonth() {
        return this.assetAcquisitionMonth;
    }

    public int getAssetDay() {
        return this.assetAcquisitionDay;
    }

    public LocalDate getAssetDate() {
        return this.assetAcquisitionDate;
    }

    public int getAssetAtRoomId() {
        return this.assetAtRoomId;
    }

    public int getAssetMaintenanceId() {
        return this.assetMaintenanceId;
    }

}

