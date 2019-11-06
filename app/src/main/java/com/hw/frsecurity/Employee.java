package com.hw.frsecurity;

// This will be the employee object
public class Employee {

    private int id;
    private byte[] img;
    private String name;
    private String department;
    private String lastSeen;

    public Employee(int newId, byte[] newImg, String newName, String newDepartment, String newLastSeen) {
        this.id = newId;
        this.img = newImg;
        this.name = newName;
        this.department = newDepartment;
        this.lastSeen = newLastSeen;
    }

    // GETTERS
    public int getId() {
        return this.id;
    }
    public byte[] getImg() {
        return this.img;
    }
    public String getName() {
        return this.name;
    }
    public String getDepartment() {
        return this.department;
    }
    public String getLastSeen() { return this.lastSeen; }

    // SETTERS (UPDATERS)
    public void setId(int newId) { this.id = newId; }
    public void setImg(byte[] img) { this.img = img; }
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setLastSeen(String lastSeen) { this.lastSeen = lastSeen; }

}
