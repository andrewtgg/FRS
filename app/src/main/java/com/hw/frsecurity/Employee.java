package com.hw.frsecurity;

// This will be the employee object
public class Employee {

    private int id;
    private byte[] img;
    private String name;
    private String department;

    public Employee(int newId, byte[] newImg, String newName, String newDepartment) {
        this.id = newId;
        this.img = newImg;
        this.name = newName;
        this.department = newDepartment;
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
}
