package dao;

import model.Admin;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class WriteAdmin {

    private String filePath = "data/admins.txt";
    private List<Admin> adminList = new ArrayList<>();
    private String adminFileHeader = "user_id,user_name,user_email,user_hash_password,admin_first_name,admin_last_name,admin_salary,is_active";

    public String writeAdmin(String adminString) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(adminString);
            bw.newLine();
        } catch (IOException e) {
            return "Admin file not found";
        }

        return "Append admin file successful"
    }

    public String writeAdmin(List<Admin> adminObject) {
        this.adminList = adminObject;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            bw.write(adminFileHeader);
            bw.newLine();
            for (Admin a: adminList)  {
                bw.write(a.getUserId() + "," + a.getUserName() + "," + a.getUserEmail() + "," + a.getUserHashPassword() + "," + a.getAdminFirstName() + "," + a.getAdminLastName() + "," + a.getAdminSalary() + "," + a.getIsActive());
                bw.newLine();
            }

        } catch (IOException e) {
            return "Admin file not found";
        }
        return "Update admin file successful";
    }

}