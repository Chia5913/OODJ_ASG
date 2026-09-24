package dao;

import model.Admin;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.List;

public class WriteAdmin {

    private String filePath = "data/admins.txt";
    private List<Admin> adminList = new Admin();

    public String appendAdmin(String adminString) {
        try (BufferedWriter bw = new BufferedWriter(new BufferedWriter(filePath, true))) {
            bw.write(adminString);
            bw.newLine();
        } catch (IOException e) {
            return "Admin file not found";
        }

        return "Append admin file successful"
    }

    public String writeAdmin(List<Admin> adminObject) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {

        }

    }

}