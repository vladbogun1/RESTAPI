package main.java.ua.nure.bogun.apiproject.entity;

import java.util.Date;

public class Device extends Entity{
    private String phone;
    private String temperature;
    private String humidity;
    private String lastDate;
    private int userId;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + getId() +
                ", phone='" + phone + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", lastDate='" + lastDate + '\'' +
                ", userId=" + userId +
                '}';
    }
}
