package org.intensive.stage5;

public class MySingleton{
    public static void main(String[] args) {
        // Получаем экземпляр Singleton
        Singleton singleton = Singleton.getInstance();

        // Используем методы Singleton
        singleton.doSomething();

        // Проверяем, что это один и тот же объект
        Singleton anotherSingleton = Singleton.getInstance();
        anotherSingleton.doSomething();
        System.out.println("Проверка объекта " + (singleton == anotherSingleton));
    }
}

class Singleton {
    private static Singleton INSTANCE;

    private Singleton() {}

    public static Singleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton();
        }
        return INSTANCE;
    }

    public void doSomething() {
        System.out.println(INSTANCE.hashCode());
    }
}
