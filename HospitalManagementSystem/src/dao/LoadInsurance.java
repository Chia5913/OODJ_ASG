package dao;

import model.Insurance;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class LoadInsurance implements InterfaceLoadAdmin<Insurance>{

    private String filePath = "data/insurance_list.txt";
    private List<Insurance> insuranceList = new ArrayList<>();

    @Override
    public String readLoadFile() {
        insuranceList.clear();  
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            line = br.readLine();
            while((line=br.readLine()) != null) {
                String[] data = line.split(",");
                int insuranceId = Integer.parseInt(data[0].trim());
                String insuranceName = data[1].trim();
                boolean insuranceIsActive = Boolean.parseBoolean(data[2].trim());
                Insurance insuranceListObject = new Insurance(insuranceId, insuraneName, insuranceIsActive);
                insuranceList.add(insuranceListObject);
            }
        } catch (IOException e) {
            return "Insurance file not found";
        }
        return "Insurance list loaded successfully"; 
    }

    @Override
    public List<Insurance> getObjectList() {
        String status = readLoadFile();
        return this.insuranceList;
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