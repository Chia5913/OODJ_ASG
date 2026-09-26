/* Purely used by admin child class, teammates are using Person as parent class 
There is no setter method is because of only allowing manipulation of data via the text file, preserving data integration*/

package model;


public abstract class User1 {

    protected int userId;
    protected String userName;
    protected String userEmail;
    protected String userHashPassword;
    protected boolean isActive;

    public User1(int userId, String userName, String userEmail, String userHashPassword, boolean isActive) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userHashPassword = userHashPassword;
        this.isActive =isActive;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserHashPassword() {
        return this.userHashPassword;
    }
    
    public boolean getIsActive() {
        return this.isActive;
    }
}