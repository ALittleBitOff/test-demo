package org.intensive.stage2;

public class MainExample3 {
    private Integer age;
    private String name;
    private Integer accountNumber;

    public static void main(String[] args) {
        Stage2Inteface myClass = new MyClass();//доступны только методы интерфейса

        Animal animal = new Animal();
        Animal animal2 = new Cat();
        Cat cat = new Cat();

        animal2.say();
        animal.say();
        cat.say();
        cat.strach();
    }

}
