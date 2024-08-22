package ru.ctraltdefeat.DemoWolframSingle.calculation;

import ru.ctraltdefeat.DemoWolframSingle.domain.CalculationRequest;
import ru.ctraltdefeat.DemoWolframSingle.domain.CalculationResponse;

public class Bridge {

    static {
        try{
            System.loadLibrary("calculation");
            System.out.println("Loaded libraries");
        }catch(UnsatisfiedLinkError e){
            //nothing to do
            System.out.println("Couldn't load libraries");
            System.out.println(e.getMessage());
        }
    }

    public static native void SayHello();

    public static native int calculate(CalculationRequest calculationRequest, CalculationResponse outCalculationResponse);
}
