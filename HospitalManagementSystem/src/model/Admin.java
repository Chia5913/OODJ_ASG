package model;

public class Admin extends User1 {

    private String adminFirstName;
    private String adminLastName;
    private double adminSalary;

    public Admin(int userId, String userName, String userEmail, String userHashPassword, boolean isActive, String adminFirstName, String adminLastName, double adminSalary) {
        super(userId, userName, userEmail, userHashPassword, isActive);
        this.adminFirstName = adminFirstName;
        this.adminLastName = adminLastName;
        this.adminSalary = adminSalary;
    }

    public String getAdminFirstName() {
        return this.adminFirstName;
    }

    public String getAdminLastName() {
        return this.adminLastName;
    }

    public double getAdminSalary() {
        return this.adminSalary;
    }
}
