package model;

public class Insurance {

    private int insuranceId;
    private String insuranceName;
    private boolean insuranceIsActive;

    public Insurance(int insurnaceId, String insuranceName, boolean insuranceIsActive) {
        this.insuranceId = insuranceId;
        this.insuranceName = insuranceName;
        this.insuranceIsActive = insuranceIsActive;

    }

    public int getInsuranceId() {
        return this.insuranceId;
    }

    public String getInsuranceName() {
        return this.insuranceName;
    }

    public boolean getInsuranceIsActive() {
        return this.insuranceIsActive;
    }
}