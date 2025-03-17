package org.intensive.stage2;

public class MainExample1 {

    private Integer age;
    private String name;
    private Integer accountNumber;

    public MainExample1(Integer accountNumber, String name, Integer age) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
    }

    public static void main(String[] args) {
        //
//        MyClass myclass = new MyClass();
//        myclass.doSomeInnerLogicInteface();
//        myclass.doSomeInnerLogic();


//        вложенные класса
//        MainExample2 main = new MainExample2();
//        main.new MyInnerClass();
//        MainExample2.MyInnerStaticClass myInnerStaticClass = new MainExample2.MyInnerStaticClass();
//        MainExample2.MyInnerClass myInnerClass = new MainExample2().new MyInnerClass();

        MainExample1 mainExample2 = MainExample1.builder()
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

        public MainExample1 build(){
            return new MainExample1(accountNumber, name, age);
        }
    }

    public static class MyInnerStaticClass {}

    public class MyInnerClass {}
}
