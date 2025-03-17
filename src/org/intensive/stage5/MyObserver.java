package org.intensive.stage5;

public class MyObserver {
    public static void main(String[] args) {
        Subject subject = new Subject();

        // список наблюдений
        Observer observer1 = new CreateObserver("observer1");
        Observer observer2 = new CreateObserver("observer2");

        subject.addObserver(observer1);
        subject.addObserver(observer2);
        //состоение 1
        subject.setState("stage 1");
        // -2 обсервер
        subject.removeObserver(observer2);
        // меняем на состоение 3
        subject.setState("stage 3");

    }
}
