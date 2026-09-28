package service;

import dao.LoadConsultRate;
import dao.WriteConsultRate;

public class ConsultRateService {

    private LoadConsultRate lCR = new LoadConsultRate();
    private WriteConsultRate wCR = new WriteConsultRate();
    private double consultRate;

    public ConsultRateService() {
        this.consultRate = lCR.getConsultationRate();
    }

    public String loadConsultationRate() {
        String status = lCR.loadConsultationRate();
        this.consultRate = lCR.getConsultationRate(); 
        return status;
    }

    public double getBaseConsultationRate() {
        loadConsultationRate();
        return this.consultRate;
    }

    public String updateBaseConsultationRate(double newValue) {
        String status = wCR.writeConsultRate(newValue);
        if (status.equals("Consultation rate updated successfully")) {
            this.consultRate = newValue;    
        }
        return status;
    }
}