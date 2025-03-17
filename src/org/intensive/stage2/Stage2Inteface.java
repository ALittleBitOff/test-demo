package org.intensive.stage2;

public interface Stage2Inteface {
    String str = "String";
    public static final String st2 = "string";// по умолчанию статик файнал паблик
    void voice();
    private void innerLogic(){
        System.out.println("Default private");
    }

    default void doSomeInnerLogicInteface(){
        innerLogic();
    };
}