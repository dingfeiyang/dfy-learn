package io.github.dingfeiyang.spi;

/**
 * SPI全称 Service Provider Interface ，字面意思：“服务提供者的接口”，是一种服务发现机制。
 *
 * 懒加载
 * 反射
 *
 * 参考：https://blog.csdn.net/qq_37883866/article/details/139000021
 */
public class Main {
    public static void main(String[] args) {
        SpiLoader load = SpiLoader.getLoad();
        load.land();
    }
}
