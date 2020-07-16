package com.icbc.javaagent;

import java.lang.instrument.Instrumentation;

public class Demo {
    public static void premain(String agentArgs, Instrumentation inst){
        System.out.println("premain!");
        inst.addTransformer(new MyTransformer(),true);
    }
}
