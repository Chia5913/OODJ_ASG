package dao;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class LoadConsultRate {

    private String filePath = "data/base_consultation_rate";
    private double consultationRate;

    public String loadConsultationRate() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line; 
            this.consultationRate = Double.parseDouble(br.readLine().trim());
        } catch (IOException e) {
            return "Consultation file not found";
        }
        return "Consultation rate loaded successfully";
    }

    public double getConsultationRate() {
        return this.consultationRate;
    }
}