package model;

public class HospitalRoomRole {

    private int roomRoleId;
    private String roomRoleName;

    public void HospitalRoomRole(int roomRoleId, String roomRoleName) {
        this.roomRoleId = roomRoleId;
        this.roomRoleName = roomRoleName;
    }

    public int getRoomRoleId() {
        return this.roomRoleId;
    }

    public String getRoomRoleName() {
        return this.roomRoleName;
    }

}