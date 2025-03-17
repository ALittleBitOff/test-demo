А теперь кратко рассмотрим наиболее используемые методы класса `Integer`.
Итак, “топ” возглавляют методы по преобразованию числа из строки, либо преобразованию строки из числа.
Начнем с преобразований строки в число.

Для этих целей служит метод **parseInt**, сигнатура ниже:

```java
static int parseInt(String s)
```

Данный метод преобразует `String` в `int`.

Продемонстрируем работу данного метода:

```java
int i = Integer.parseInt("10");
System.out.println(i);
```

Если преобразование невозможно — например, мы передали слово в метод `parseInt` — будет брошено исключение `NumberFormatException`.

У метода `parseInt(String s)` есть перегруженный собрат:

```java
static int parseInt(String s, int radix)
```

Данный метод преобразует параметр `s` в `int`.
В параметре `radix` указывается, в какой системе счисления изначально записано число в `s`, которое необходимо преобразовать в `int`.

Примеры ниже:

```java
System.out.println(Integer.parseInt("0011", 2)); 
System.out.println(Integer.parseInt("10", 8));   
System.out.println(Integer.parseInt("F", 16));   
```

Методы `parseInt` возвращают примитивный тип данных `int`.
У данных методов есть аналог — метод `valueOf`.
Некоторые вариации данного метода внутри себя просто вызывают parseInt.
Отличие от `parseInt` в том, что результатом работы `valueOf` будет `Integer`, а не `int`.
Рассмотрим ниже все варианты данного метода и пример его работы:

- `static Integer valueOf(int i)` — возвращает `Integer` значением которого является i;
- `static Integer valueOf(String s)` — аналогичен `parseInt(String s)`, но результатом будет `Integer`;
- `static Integer valueOf(String s, int radix)` — аналогичен `parseInt(String s, int radix)`, но результатом будет `Integer`.

Примеры:

```java
int a = 5;
Integer x = Integer.valueOf(a);
Integer y = Integer.valueOf("20");
Integer z = Integer.valueOf("20", 8);

System.out.println(x); 
System.out.println(y); 
System.out.println(z); 
```

Мы рассмотрели методы, позволяющие перевести `String` в `int`/`Integer`.
Обратная процедура достигается с помощью методов `toString`. У любого `Integer` объекта можно вызвать метод `toString` и получить его строковое представление:

```java
Integer x = 5;
System.out.println(x.toString()); 
```

Однако в связи с тем, что метод `toString` часто вызывается у объектов неявно (например, при передаче объекта на печать в консоль), данный метод редко используется разработчиками в явном виде.
Есть также и статический метод `toString`, который принимает в себя `int` параметр и переводит его в строковое представление.

Например:

```java
System.out.println(Integer.toString(5)); 
```

Однако, как и не статический метод `toString`, использование статического в явном виде можно встретить редко. Более интересен статический метод `toString`, который принимает 2 целочисленных параметра:

- `static String toString(int i, int radix)` — переведет `i` в строковое представление в системе счисления `radix`.

Пример:

```java
System.out.println(Integer.toString(5, 2));
```

В классе `Integer` есть пару методов для нахождения максимума/минимума из двух чисел:

- `static int max(int a, int b)` вернет наибольшее из значений среди переданных переменных;
- `static int min(int a, int b)` вернет наименьшее из значений среди переданных переменных.

Примеры:

```java
int x = 4;
int y = 40;

System.out.println(Integer.max(x,y));
System.out.println(Integer.min(x,y));
```

