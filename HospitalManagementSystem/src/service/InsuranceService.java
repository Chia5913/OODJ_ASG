package service;

import java.util.List;
import java.util.ArrayList;
import model.Insurance;
import dao.LoadInsurance;
import dao.WriteInsurance;

public class InsuranceService {

    private LoadInsurance lI = new LoadInsurance();
    private WriteInsurance wI = new WriteInsurance();
    private List<Insurance> insuranceObjectList;
    private Insurance insuranceObject;

    public InsuranceService() {
        this.insuranceObjectList = lI.getObjectList();
    }

    public int getLatestInsuranceId() {
        this.insuranceObjectList = lI.getObjectList();
        int latestInsuranceId = 0;
        for (Insurance i : this.insuranceObjectList) {
            if (i.getInsuranceId() > latestInsuranceId) {
                latestInsuranceId = i.getInsuranceId();
            }
        }
        return latestInsuranceId + 1;
    }

    public List<Insurance> getInsuranceList() {
        this.insuranceObjectList = lI.getObjectList();
        return this.insuranceObjectList;
    }

    public String appendFile(String insuranceName, boolean insuranceIsActive) {
        int insuranceId = getLatestInsuranceId();
        String insuranceString = insuranceId + "," + insuranceName + "," + insuranceIsActive;
        String status = wI.writeFile(insuranceString);
        if (status.equals("Insurance list updated")) {
            this.insuranceObject = new Insurance(insuranceId, insuranceName, insuranceIsActive);
            this.insuranceObjectList.add(insuranceObject);
            return status;
        } else {
            return status;
        }
        
    }

    public String updateFile(String action, int selectedInsuranceId, String insuranceName, boolean insuranceIsActive) {
        List<Insurance> tempObjectList = new ArrayList<>();
        if (action.equals("update")) {
            for (Insurance a: this.insuranceObjectList) {
                if (a.getInsuranceId() == selectedInsuranceId) {
                    Insurance updatedInsuranceObject = new Insurance(selectedInsuranceId, insuranceName, insuranceIsActive);
                    tempObjectList.add(updatedInsuranceObject);
                } else {
                    tempObjectList.add(a);
                }
            }
            this.insuranceObjectList = tempObjectList;
            String status = wI.writeFile(this.insuranceObjectList);
            return status;

        } else if (action.equals("delete")) {
            for (Insurance a : this.insuranceObjectList) {
                if (a.getInsuranceId() == selectedInsuranceId) {
                    continue;
                } else {
                    tempObjectList.add(a);
                }
            }
            this.insuranceObjectList = tempObjectList;
            String status = wI.writeFile(this.insuranceObjectList);
            return status;
        } else {
            return "Invalid action";
        }

    }

}