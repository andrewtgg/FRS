package com.hw.frsecurity;

public class ActivityLogItem {

    private int id;
    private byte[] img;
    private String dateSeen;
    // Status is a boolean variable (1 or 0)
    private int status;

    public ActivityLogItem(int newId, byte[] newImg, String newDateSeen, int newStatus) {
        this.id = newId;
        this.img = newImg;
        this.dateSeen = newDateSeen;
        this.status = newStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getDateSeen() {
        return dateSeen;
    }

    public void setDateSeen(String dateSeen) {
        this.dateSeen = dateSeen;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
