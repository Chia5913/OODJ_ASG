package dao;

import model.Insurance;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class LoadInsurance {

    private String filePath = "data/insurance_list.txt";
    private List<Insurance> insuranceList = new ArrayList<>();
    private String[] insuranceHeader;

    public String loadInsurance() {
        insuranceList.clear();  
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            line = br.readLine();
            this.insuranceHeader = line.split(",");
            while((line=br.readLine()) != null) {
                String[] data = line.split(",");
                int insuraneId = Integer.parseInt(data[0].trim());
                String insuranceName = data[1].trim();
                boolean insuranceIsActive = data[2].trim();
                Insurance insuranceListObject = new Insurance(insuranceId, insuraneName, insuranceIsActive);
                insuranceList.add(insuranceListObject);
            }
        } catch (IOException e) {
            return "Insurance file not found";
        }
        return "Insurance list loaded successfully"; 
    }

    public List<Insurance> getInsuranceList() {
        return this.insuranceList;
    }

    public String[] getInsuranceHeader() {
        return this.insuranceHeader;
    }

    public int getLatestInsuranceId() {
        int latestInsuranceId = 0;
        for (Insurance i : this.insuranceList) {
            if (i.getInsuranceId() > latestInsuranceId) {
                latestInsuranceId = i.getInsuranceId();
            }
        }
        return latestInsuranceId + 1;
    }
    
}