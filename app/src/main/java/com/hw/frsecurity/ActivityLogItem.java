package com.hw.frsecurity;

public class ActivityLogItem {

    private int id;
    private byte[] img;
    // Status is a boolean variable (1 or 0)
    private int status;

    private String dateSeen;
    private String timeSeen;

    // prediction probability
    private double probability;

    public ActivityLogItem(int newId, byte[] newImg, String newDateSeen, String newTimeSeen, int newStatus, double newProbability) {
        this.id = newId;
        this.img = newImg;
        this.status = newStatus;
        this.dateSeen = newDateSeen;
        this.timeSeen = newTimeSeen;
        this.probability = newProbability;
    }

    public String getTimeSeen() {
        return timeSeen;
    }

    public void setTimeSeen(String timeSeen) {
        this.timeSeen = timeSeen;
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

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
