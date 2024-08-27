package io.github.dingfeiyang.javassist.basic;

import javassist.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Javassist的核心特点是将源代码片段作为字符串嵌入到现有类中，然后在运行时进行编译和加载，这使得代码修改变得非常灵活和便捷
 *
 * 参考：https://www.51cto.com/article/750294.html
 */
public class Test_Javassist {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        /**
         * test
         */
        ClassPool classPool = ClassPool.getDefault();
//        CtClass ctClass = classPool.makeClass("io.github.dingfeiyang.javassist.basic.Dfy.java");
//        System.out.println(ctClass);
        CtClass ctClass = classPool.get("io.github.dingfeiyang.javassist.basic.Dfy");
        ctClass.setSuperclass(classPool.get("io.github.dingfeiyang.javassist.basic.Super_Dfy"));
        CtMethod ctMethod = CtNewMethod.make("public int add(int a, int b) { return a + b ;}", ctClass);
        ctClass.addMethod(ctMethod);
        CtMethod addMethod = ctClass.getDeclaredMethod("add");
        System.out.println(addMethod);
        /**
         * 代理
         */
//        ClassPool classPool = ClassPool.getDefault();
//        CtClass ctClass = classPool.makeClass("io.github.dingfeiyang.javassist.basic.MyProxy");
//        // 为代理类添加接口
//        ctClass.addInterface(classPool.get("io.github.dingfeiyang.javassist.basic.MyInterface"));
//        // 添加委托对象字段
//        CtField delegateField = new CtField(classPool.get("io.github.dingfeiyang.javassist.basic.MyInterface"), "delegate", ctClass);
//        delegateField.setModifiers(Modifier.PRIVATE);
//        ctClass.addField(delegateField);
//        System.out.println(ctClass);
//
//        // 为代理类的每个方法添加代理逻辑
//        for (CtMethod method : classPool.get("io.github.dingfeiyang.javassist.basic.MyInterface").getDeclaredMethods()) {
//            CtMethod proxyMethod = CtNewMethod.delegator(method, ctClass);
//            ctClass.addMethod(proxyMethod);
//        }
//        System.out.println(ctClass);
        /**
         * AOP
         */
//        ClassPool pool = ClassPool.getDefault();
//        CtClass ctClass = pool.get("io.github.dingfeiyang.javassist.basic.Dfy");
//        CtMethod ctMethod = ctClass.getDeclaredMethod("test");
//        // 在方法调用前插入逻辑
//        ctMethod.insertBefore("System.out.println(\"Before method call\");");
//        // 在方法调用后插入逻辑
//        ctMethod.insertAfter("System.out.println(\"After method all\");");
        /**
         * 代码注入
         */
//        ClassPool pool = ClassPool.getDefault();
//        CtClass targetClass = pool.get("io.github.dingfeiyang.javassist.basic.Dfy");
//        CtMethod targetMethod = targetClass.getDeclaredMethod("test");
//        // 在方法调用前注入代码
//        targetMethod.instrument(new ExprEditor(){
//            @Override
//            public void edit(Cast c) throws CannotCompileException {
//                c.replace("System.out.println(\"Before method call: \" + $1); $_ = $proceed($$);");
//            }
//        });

    }
}
