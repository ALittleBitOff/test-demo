[[Методы equals и hashCode]]
Давай посмотрим, что же это за методы:

| Метод                                                                                                                                                                                        | Описание                                                                                   |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ |
| `public String toString()`                                                                                                                                                                   | Возвращает строковое представление объекта.                                                |
| `public native int hashCode() public boolean equals(Object obj)`                                                                                                                             | Пара методов, которые используются для сравнения объектов.                                 |
| `public final native Class getClass()`                                                                                                                                                       | Возвращает специальный объект, который описывает текущий класс.                            |
| `public final native void notify() public final native void notifyAll() public final native void wait(long timeout) public final void wait(long timeout, intnanos) public final void wait()` | Методы для контроля доступа к объекту из различных нитей. Управление синхронизацией нитей. |
| `protected void finalize()`                                                                                                                                                                  | Метод позволяет «освободить» родные не-Java ресурсы: закрыть файлы, потоки и т.д.          |
| `protected native Object clone()`                                                                                                                                                            | Метод позволяет клонировать объект: создает дубликат объекта.                              |
**— Начнем с метода `toString()`;**

Этот метод позволяет получить текстовое описание любого объекта. Реализация его в классе Object очень простая:

```java
return getClass().getName() + "@" + Integer.toHexString(hashCode());
```

`getClass()` и `hashCode()` – это тоже методы класса Object.  
Вот стандартный результат вызова такого метода

```java
java.lang.Object@12F456
```

— Из такого описания можно узнать класс объекта, у которого вызвали данный метод.
А также можно различать объекты – разным объектам соответствуют разные цифры, идущие после символа @.

Но ценность данного метода в другом. Данный метод можно переопределить в любом классе и возвращать более нужное или более детальное описание объекта.

Но и это еще не все.
Благодаря тому, что для каждого объекта можно получить его текстовое представление, в **Java** можно было реализовать поддержку «сложения» строк с объектами.  
Вот смотри:

| Код                                                                               | Что происходит на самом деле                                                                                                      |
| --------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| `int age = 18;`<br>`System.out.println("Age is " + age);`                         | `String s = String.valueOf(18);`<br><br>`String result = "Age is " + s;`<br><br>`System.out.println(result);`                     |
| `Student st = new Student("Vasya");`<br>`System.out.println("Student is " + st);` | `Student st = new Student("Vasya");`<br><br>`String result = "Student is " + st.toString();`<br><br>`System.out.println(result);` |
| `Car car = new Porsche();`<br>`System.out.println("My car is " + car);`           | `Car car = new Porsche();`<br><br>`String result = "My car is " + car.toString();`<br><br>`System.out.println(result);`           |
