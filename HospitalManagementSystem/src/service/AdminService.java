package service;

import java.util.List;
import dao.LoadAdmin;
import dao.WriteAdmin;


public class AdminService {

    private List<Admin> adminList;
    private LoadAdmin loadAdmin;

    public AdminService() {
        this.loadAdmin = new LoadAdmin();
        this.adminList = loadAdmin.getAdminList();
    }

    public int getLatestAdminId() {
        int latestAdminId = 0;
        for (Admin i: this.adminList) {
            if (i.getUserId() > latestAdminId) {
                latestAdminId = i.getUserId();
            }
        }
        return latestAdminId + 1;
    }

    public List<Admin> getAdminList() {
        return this.adminList;
    }
}