package io.github.dingfeiyang.javassist.example;

import javassist.*;

import java.lang.reflect.InvocationTargetException;

public class JavassistExample {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ClassPool pool = ClassPool.getDefault();
        CtClass targetClass = pool.get("io.github.dingfeiyang.javassist.example.TargetClass");
        CtMethod targetMethod = targetClass.getDeclaredMethod("execute");
        targetMethod.addLocalVariable("startTime", CtClass.longType);
        // 方法调用前记录开始时间
        targetMethod.insertBefore("startTime = System.currentTimeMillis();");
        //方法调用后计算耗时并输出
        targetMethod.insertAfter("System.out.println(\"Execution time: \" + (System.currentTimeMillis() - startTime) + \" ms\");");

        //转换并加在修改后的类
        Class<?> modifiedClass = targetClass.toClass();
        targetClass.detach();

        // 创建目标类实例并调用方法
        Object instance = modifiedClass.newInstance();
        modifiedClass.getMethod("execute").invoke(instance);
    }
}
