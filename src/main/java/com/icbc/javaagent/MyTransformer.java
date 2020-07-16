package com.icbc.javaagent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("正在加载类" + className);
        if (!"com/icbc/test/Person".equals(className))
            return classfileBuffer;

        CtClass cl = null;
        try {
            ClassPool classPool = ClassPool.getDefault();
            cl = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            CtMethod ctMethod = cl.getDeclaredMethod("test");
            String ctMethodName = ctMethod.getName();
            System.out.println("获取需要监控的方法名:" + ctMethodName);
            ctMethod.addLocalVariable("start", CtClass.longType);
            ctMethod.insertBefore("start = System.currentTimeMillis();");
            ctMethod.insertAfter("System.out.println(\"" + ctMethodName + " cost: \" + (System" +
                    ".currentTimeMillis() - start)+\"ms\");");
            byte[] transformed = cl.toBytecode();
            return transformed;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classfileBuffer;
    }
}
