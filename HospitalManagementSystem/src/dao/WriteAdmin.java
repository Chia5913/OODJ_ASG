package dao;

import model.Admin;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class WriteAdmin implements InterfaceWriteAdmin<Admin> {

    private String filePath = "data/admins.txt";
    private List<Admin> adminList = new ArrayList<>();
    private String adminFileHeader = "user_id,user_name,user_email,user_hash_password,admin_first_name,admin_last_name,admin_salary,is_active";

    @Override /*plus overloading*/
    public String writeFile(String adminString) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(adminString);
            bw.newLine();
        } catch (IOException e) {
            return "Admin file not found";
        }

        return "Append admin file successful";
    }

    @Override
    public String writeFile(List<Admin> adminObject) {
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

    @Override 
    public String writeFile(String adminString, String filePath) {
        return "This action is not supported";
    }

    @Override
    public String writeFile(List<Admin> adminObject, String filePath) {
        return "This action is not supported";
    }

}