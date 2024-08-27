package io.github.dingfeiyang.javassist.example;

public class TargetClass {
    public void execute() {
        try {
            Thread.sleep(1000);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
