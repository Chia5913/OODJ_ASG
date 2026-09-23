package model;

public class HospitalRoom {

    private String roomDesignatedName;
    private String roomDesignatedRole;
    private int roomDoorNumberId;
    private boolean roomStatus;

    public void HospitalRoom(int roomDoorNumberId, String roomDesignatedName, String roomDesignatedRole, boolean roomStatus ) {
        this.roomDesignatedName = roomDesignatedName;
        this.roomDesignatedRole = roomDesignatedRole;
        this.roomDoorNumberId = roomDoorNumberId;
        this.roomStatus = roomStatus;
    }

    public String getRoomName() {
        return this.roomDesignatedName;
    }

    public String getRoomRole() {
        return this.roomDeisgnatedRole;
    }

    public int getDoorNumberId() {
        return this.roomDoorNumberId;
    }

    public boolean getRoomStatus() {
        return this.roomStatus;
    }

}
