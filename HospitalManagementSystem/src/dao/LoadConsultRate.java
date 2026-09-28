package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadConsultRate {

    private String filePath = "data/base_consultation_rate.txt";
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
        String status = loadConsultationRate();
        return this.consultationRate;
    }
}