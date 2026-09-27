package dao;

import java.util.List;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class WriteConsultRate {

    private String filePath = "data/base_consultation_rate.txt";

    public String writeConsultRate(double newConsultRate) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            bw.write(String.format("%.2f", newConsultRate));
        } catch (IOException e) {
            return "Consultation rate file not found";
        }
        return "Consultation rate updated successfully";
    }
}