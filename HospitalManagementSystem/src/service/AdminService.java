package service;

import java.util.List;
import java.util.ArrayList;
import dao.LoadAdmin;
import dao.WriteAdmin;
import dao.DoctorFile;
import dao.ManagerFile;
import dao.PatientFile;
import java.security.MessageDigest;
import model.Admin;
import model.Doctor;
import model.Patient;
import model.MedicalManager;

public class AdminService {

    private List<Admin> adminList;
    private List<Doctor> doctorList;
    private List<Patient> patientList;
    private List<MedicalManager> medicalManagerList;

    public AdminService() {
        LoadAdmin lA = new LoadAdmin();
        this.adminList = lA.getObjectList();
    }

    public int getLatestAdminId() {
        int latestAdminId = 0;
        for (Admin i: this.adminList) {
            if (i.getUserId() > latestAdminId) {
                latestAdminId = i.getUserId();
            }
        }
        return latestAdminId + 1;
    }

    public List<Admin> getAdminList() {
        LoadAdmin lA = new LoadAdmin();
        this.adminList = lA.getObjectList();        
        return this.adminList;
    }

    public String passwordHasher(String plaintextPassword) {
        if ((plaintextPassword.trim().length()) > 16 ) {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(plaintextPassword.trim().getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte a : hashedBytes) {
                stringBuilder.append(String.format("%02x", a));
            }
            return stringBuilder.toString();            
        } else {
            return plaintextPassword;
        }
    }

    public String appendAdmin(int userId, String userName, String userEmail, String userPassword, boolean isActive, String adminFirstName, String adminLastName, double adminSalary) {
        WriteAdmin aW = new WriteAdmin();
        String userHashPassword = passwordHasher(userPassword);
        String adminString = userId + "," + userName + "," + userEmail + "," + userHashPassword + "," + adminFirstName + "," + adminLastName + "," + adminSalary + "," + isActive;
        String status = aW.writeFile(adminString);
        Admin adminObject = new Admin(userId, userName, userEmail, userHashPassword, isActive, adminFirstName, adminLastName, adminSalary);
        adminList.add(adminObject);
        return status;
    }

    public String writeAdmin(String action, int selectedAdminId, String userName, String userEmail, String userPassword, boolean isActive, String adminFirstName, String adminLastName, double adminSalary) {
        List<Admin> tempAdminList = new ArrayList<>();
        String userHashPassword = passwordHasher(userPassword);
        if (action.equals("update")) {
            tempAdminList.clear();
            for (Admin a: adminList) {
                if (a.getUserId() == selectedAdminId) {
                    Admin updatedObject = new Admin(selectedAdminId, userName, userEmail, userHashPassword, isActive, adminFirstName, adminLastName, adminSalary);
                    tempAdminList.add(updatedObject);
                } else {
                    tempAdminList.add(a);
                }
            }
            this.adminList = tempAdminList;
            WriteAdmin wA = new WriteAdmin();
            String status =  wA.writeFile(adminList);
            return status;
        
        } else if (action.equals("delete")) {
            tempAdminList.clear();
            for (Admin a : adminList) {
                if (a.getUserId() == selectedAdminId) {
                    continue;
                } else {
                    tempAdminList.add(a);
                }
            }
            this.adminList = tempAdminList;
            WriteAdmin wA = new WriteAdmin();
            String status = wA.writeFile(adminList);
            return status;

        } else {
            return "Error updating the admin list";
        }
    }

    /*Below are all written to use teammate's coding style.*/

    public List<MedicalManager> getMedicalManager() {
        ManagerFile mF = new ManagerFile();
        this.medicalManagerList = mF.getAll();
        return this.medicalManagerList;
    }

    public String saveMedicalManager(String id, String name, String email, String phone, String password, String managedDepartmentId) {
        ManagerFile mF = new ManagerFile();
        MedicalManager obj = new MedicalManager(id, name, email, phone, password, managedDepartmentId);
        mF.save(obj);
        return "Medical manager file updated";
    }

    public String deleteMedicalManager(String id) {
        ManagerFile mF = new ManagerFile();
        mF.delete(id);
        return "Medical manager file updated";
    }

    public List<Doctor> getDoctors() {
        DoctorFile dF = new DoctorFile();
        this.doctorList = dF.getAll();
        return this.doctorList;
    }

    public String saveDoctor(String id, String name, String email, String phone, String password, String specialty, String departmentId, String managerId, double consultationFee, String shift) {
        DoctorFile dF = new DoctorFile();
        Doctor doctor = new Doctor(id, name, email, phone, password, specialty, departmentId, managerId, consultationFee, shift);
        dF.save(doctor);
        return "Doctor file updated";
    }

    public String deleteDoctor(String id) {
        DoctorFile dF = new DoctorFile();
        dF.delete(id);
        return "Doctor file updated";
    }

    public List<Patient> getPatient() {
        PatientFile pF = new PatientFile();
        this.patientList = pF.getAll();
        return this.patientList;

    }

    public String addPatient(String id, String name, String email, String phone, String password, String bloodType, String allergies, String insuranceProvider, String emergencyContact) {
        PatientFile pF = new PatientFile();
        Patient patient = new Patient(id, name, email, phone, password, bloodType, allergies, insuranceProvider, emergencyContact);
        pF.add(patient);
        return "Patient file had been updated";
    }

    public String removePatient(String id) {
        PatientFile pF = new PatientFile();
        pF.delete(id);
        return "Patient file had been updated";
    }




}