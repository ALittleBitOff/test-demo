package org.intensive.stage2;

import com.sun.tools.javac.Main;

public class MainExample2 {

    private Integer age;
    private String name;
    private Integer accountNumber;

    public MainExample2(Integer accountNumber, String name, Integer age) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
    }

    public static void main(String[] args) {

        MainExample2 mainExample2 = MainExample2.builder()
                .age(10)
                .build();
        System.out.println(mainExample2);
    }

    public static MainExample2Builder builder(){
        return new MainExample2Builder();
    }

    @Override
    public String toString() {
        return "MainExample2{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", accountNumber=" + accountNumber +
                '}';
    }

    public static class MainExample2Builder{
        private Integer age;
        private String name;
        private Integer accountNumber;

        public MainExample2Builder age(Integer age){
            this.age = age;
            return this;
        }
        public MainExample2Builder name(String name){
            this.name = name;
            return this;
        }
        public MainExample2Builder accountNumber(Integer accountNumber){
            this.accountNumber = accountNumber;
            return this;
        }

        public MainExample2 build(){
            return new MainExample2(accountNumber, name, age);
        }
    }

    public static class MyInnerStaticClass {}

    public class MyInnerClass {}
}
