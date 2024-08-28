package io.github.dingfeiyang.rpc.service;

public class CarImpl implements Car{

    @Override
    public void drive(String userName) {
        System.out.println("driving");
    }

    @Override
    public CarInfo get(String carName) {
        CarInfo carInfo = new CarInfo();
        carInfo.setUserName("dfy");
        carInfo.setCarName("Bez");
        carInfo.setCarType("C260L");

        return carInfo;
    }
}
