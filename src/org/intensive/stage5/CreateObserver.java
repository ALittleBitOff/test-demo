package org.intensive.stage5;

public class CreateObserver implements Observer {
    private String name;

    public CreateObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(this.name + ": " + message);
    }
}
