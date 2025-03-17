package org.intensive.stage7;

public class MyTea {
    public static void main(String[] args) {
        Tea tea = new PriceTea();
        System.out.println(tea.getDescription() + " " + tea.getPrice());

        tea = new SugarDecorator(tea);
        System.out.println(tea.getDescription() + " " + tea.getPrice());
    }
}
