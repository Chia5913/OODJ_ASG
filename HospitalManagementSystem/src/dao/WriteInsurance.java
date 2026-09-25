package dao;

import java.util.List;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class WriteInsurance {

    private String filePath = "data/insurance_list.txt" ;
    private List<Insurance> insuranceObjectList;
    private String insuranceString;
    private String insuranceHeader = "insurance_id,insurance_name,insurance_is_active";

    public String writeInsurance(String insuranceString) {
        this.insuranceString = insuranceString;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(this.insuranceString);
            bw.newLine();
        } catch (IOException e) {
            return "Insurance list file not found";
        }
        return "Insurance list updated";
    }

    public String writeInsurance(List<Insurance> insuranceObjectList) {
        this.insuranceObjectList = insuranceObjectList;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            bw.write(insuranceHeader);
            bw.newLine();
            for (Insurance a : this.insuranceObjectList) {
                bw.write(a.getInsuranceId() + "," + a.getInsuranceName() + "," + a.getInsuranceIsActive());
                bw.newLine();
            }

        } catch (IOException e) {
            return "Insurance file not found";
        }
        return "Insurance list updated";
    }
}