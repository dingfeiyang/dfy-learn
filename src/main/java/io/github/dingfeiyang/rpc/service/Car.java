package io.github.dingfeiyang.rpc.service;

public interface Car {
    void drive(String userName);
    CarInfo get(String carName);
}
