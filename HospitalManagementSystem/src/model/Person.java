package model;

import java.io.Serializable;

/**
 * Abstract base class demonstrating Abstraction + Encapsulation.
 * All users (Doctor, Patient, Admin, Medical Manager) inherit from Person.
 */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected String email;
    protected String phone;
    protected String password;
    protected String role; // DOCTOR, PATIENT, ADMIN, MANAGER

    public Person() {}

    public Person(String id, String name, String email, String phone, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    // ----- Getters & Setters (Encapsulation) -----
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    /**
     * Abstract method - forces subclasses to provide their own display logic (Polymorphism).
     */
    public abstract String getDisplayInfo();

    @Override
    public String toString() {
        return id + "|" + name + "|" + email + "|" + phone + "|" + password + "|" + role;
    }
}
