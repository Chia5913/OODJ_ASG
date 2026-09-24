package dao;

import model.Admin;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class LoadAdmin {

    private String filePath = "data/admins.txt";
    private List<Admin> adminList = new ArrayList<>();

    public String loadAdmin() {
        adminList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int userId = Integer.parseInt(data[0].trim());
                String userName = data[1].trim();
                String userEmail = data[2].trim();
                String userHashPassword = data[3].trim();
                String adminFirstName = data[4].trim();
                String adminLastName = data[5].trim();
                double adminSalary = Double.parseDouble(data[6].trim());
                boolean isActive = Boolean.parseBoolean(data[7].trim());
                Admin loadedAdminObject = new Admin(userId, userName, userEmail, userHashPassword, adminFirstName, adminLastName, adminSalary, isActive);
                adminList.add(loadedAdminObject);
            }
        }
        catch (IOException e) {
            return "File for admin is not found";
        }
        return "Admins successfully loaded"

    }

    public List<Admin> returnAdmin() {
        return this.adminList;
    }

}