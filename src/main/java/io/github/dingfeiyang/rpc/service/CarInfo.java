package io.github.dingfeiyang.rpc.service;

import java.io.Serializable;

public class CarInfo implements Serializable {
    private String userName;
    private String carType;
    private String carName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    @Override
    public String toString() {
        return "CarInfo{" +
                "userName='" + userName + '\'' +
                ", carType='" + carType + '\'' +
                ", carName='" + carName + '\'' +
                '}';
    }
}
