package org.intensive.stage2;

public abstract class Stage2Example {
    private Integer number;
    private String title;

    public Stage2Example(){}

    public Stage2Example(Integer number, String title){
        this.number = number;
        this.title = title;
    }

    public void doSomeInnerLogic(){
        System.out.println("Inner Logic");
    }
    public abstract void move();
}
