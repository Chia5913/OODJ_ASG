package model;

/**
 * Physical hospital asset: consultation room, ward, lab, X-ray room, etc.
 * Primarily managed by Admin Staff.
 * Teammate can extend (capacity, floor, equipment...).
 */
public class HospitalAsset {
    private String assetId;
    private String name;
    private String type;       // ROOM, WARD, LAB, IMAGING
    private String location;   // e.g. Floor 2 / Block A
    private String status;     // AVAILABLE, OCCUPIED, MAINTENANCE

    public HospitalAsset() {}

    public HospitalAsset(String assetId, String name, String type, String location, String status) {
        this.assetId = assetId;
        this.name = name;
        this.type = type;
        this.location = location;
        this.status = status;
    }

    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /** Format: assetId|name|type|location|status */
    public String toFileString() {
        return assetId + "|" + name + "|" + type + "|" + location + "|" + status;
    }

    public static HospitalAsset fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 5) return null;
        HospitalAsset a = new HospitalAsset();
        a.setAssetId(p[0]);
        a.setName(p[1]);
        a.setType(p[2]);
        a.setLocation(p[3]);
        a.setStatus(p[4]);
        return a;
    }
}
