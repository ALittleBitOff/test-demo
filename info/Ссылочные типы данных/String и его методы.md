[[StringBuffer и StringBuilder в Java]]
Класс **String** в Java предназначен для работы со строками в Java. Все строковые литералы, определенные в Java программе (например, "abc") — это экземпляры класса String. Давай посмотрим на его ключевые характеристики:

1. Класс реализует интерфейсы `Serializable` и `CharSequence`. Поскольку он входит в пакет `java.lang`, его не нужно импортировать.
2. Класс **String** в Java — это `final` класс, который не может иметь потомков.
3. Класс String — immutable класс, то есть его объекты не могут быть изменены после создания. Любые операции над объектом String, результатом которых должен быть объект класса String, приведут к созданию нового объекта.
4. Благодаря своей неизменности, объекты класса String являются потокобезопасными и могут быть использованы в многопоточной среде.
5. Каждый объект в Java может быть преобразован в строку через метод `toString`, унаследованный всеми Java-классами от класса `Object`.

![Класс String в Java - 1](https://cdn.javarush.com/images/article/1b7e1062-ba3a-4a8b-9757-ddb03cc42181/800.webp)

## Работа с Java String

Это один из самых часто используемых классов в **Java**.
В нем есть методы для анализа определенных символов строки, для сравнения и поиска строк, извлечения подстрок, создания копии строки с переводом всех символов в нижний и верхний регистр и прочие.
Список всех методов класса `String` можно изучить в [официальной документации](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html).
Также в **Java** реализован несложный механизм конкатенации (соединения строк), преобразования примитивов в строку и наоборот.
Давай рассмотрим некоторые примеры работы с классом `String` в **Java**.

### Создание строк

Проще всего создать экземпляр класса `String`, присвоив ему значение строкового литерала:

```java
String s = "I love movies";
```

Однако у класса String есть много [конструкторов](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#String--), которые позволяют:

- создать объект, содержащий пустую строку
- создать копию строковой переменной
- создать строку на основе массива символов
- создать строку на основе массива байтов (с учетом кодировок)
- и т.д.
### Сложение строк

Сложить две строки в Java довольно просто, воспользовавшись оператором `+`.
**Java** позволяет складывать друг с другом и переменные, и строковые литералы:

```java
public static void main(String[] args) {
    String day = "День";
    String and = "и";
    String night = "Ночь";

    String dayAndNight = day + " " + and + " " + night;
}
```

Складывая объекты класса `String` с объектами других классов, мы приводим последние к строковому виду.
Преобразование объектов других классов к строковому представлению выполняется через неявный вызов метода `toString` у объекта.

Продемонстрируем это на следующем примере:

```java
public class StringExamples {
    public static void main(String[] args) {
        Human max = new Human("Макс");
        String out = "Java объект: " + max;
        System.out.println(out);
        
    }

    static class Human {
        private String name;

        public Human(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Человек с именем " + name;
        }
    }
}
```

### Сравнение строк

Для сравнения строк можно воспользоваться методом `equals()`:

```java
public static void main(String[] args) {
    String x = "Test String";
    System.out.println("Test String".equals(x)); 
}
```

Когда при сравнении строк нам не важен регистр, нужно использовать метод `equalsIgnoreCase()`:
```java
public static void main(String[] args) {
       String x = "Test String";
       System.out.println("test string".equalsIgnoreCase(x)); 
}
```

### Перевод объекта/примитива в строку

Для перевода экземпляра любого Java-класса или любого примитивного типа данных к строковому представлению, можно использовать метод `String.valueOf()`:

```java
public class StringExamples {
    public static void main(String[] args) {
        String a = String.valueOf(1);
        String b = String.valueOf(12.0D);
        String c = String.valueOf(123.4F);
        String d = String.valueOf(123456L);
        String s = String.valueOf(true);
        String human = String.valueOf(new Human("Alex"));

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        System.out.println(s);
        System.out.println(human);
        
    }

    static class Human {
        private String name;

        public Human(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Человек с именем " + name;
        }
    }
}
```

### Перевод строки в число

Часто бывает нужно перевести строку в число.
У классов оберток примитивных типов есть методы, которые служат как раз для этой цели.
Все эти методы начинаются со слова `parse`.

Рассмотрим ниже перевод строки в целочисленное (`Integer`) и дробное (`Double`) числа:

```java
public static void main(String[] args) {
    Integer i = Integer.parseInt("12");
    Double d = Double.parseDouble("12.65D");

    System.out.println(i); 
    System.out.println(d); 
}
```

### Перевод коллекции строк к строковому представлению

Если нужно преобразовать все элементы некоторой коллекции строк к строковому представлению через произвольный разделитель, можно использовать такие методы класса `String` **Java**:

- `join(CharSequence delimiter, CharSequence... elements)`
- `join(CharSequence delimiter, Iterable<? extends CharSequence> elements)`

Где `delimiter` — разделитель элементов, а `elements` — массив строк / экземпляр коллекции строк.

Рассмотрим пример, в котором мы преобразуем список строк в строку, разделяя каждую точкой с запятой:

```java
public static void main(String[] args) {
    List<String> people = Arrays.asList(
            "Philip J. Fry",
            "Turanga Leela",
            "Bender Bending Rodriguez",
            "Hubert Farnsworth",
            "Hermes Conrad",
            "John D. Zoidberg",
            "Amy Wong"
    );

    String peopleString = String.join("; ", people);
    System.out.println(peopleString);
    
}
```

### Разбиение строки на массив строк

Эту операцию выполняет метод `split(String regex)`
В качестве разделителя выступает строковое регулярное выражение `regex`.

В примере ниже произведем операцию, обратную той, что мы выполняли в предыдущем примере:

```java
public static void main(String[] args) {
    String people = "Philip J. Fry; Turanga Leela; Bender Bending Rodriguez; Hubert Farnsworth; Hermes Conrad; John D. Zoidberg; Amy Wong";

    String[] peopleArray = people.split("; ");
    for (String human : peopleArray) {
        System.out.println(human);
    }
    
}
```

### Определение позиции элемента в строке

В языке Java String предоставляет набор методов для определения позиции символа/подстроки в строке:

1. `indexOf(int ch)`
2. `indexOf(int ch, int fromIndex)`
3. `indexOf(String str)`
4. `indexOf(String str, int fromIndex)`
5. `lastIndexOf(int ch)`
6. `lastIndexOf(int ch, int fromIndex)`
7. `lastIndexOf(String str)`
8. `lastIndexOf(String str, int fromIndex)`

Где:

1. `ch` — искомый символ (`char`)
2. `str` — искомая строка
3. `fromIndex` — позиция с которой нужно искать элемент
4. методы `indexOf` — возвращают позицию первого найденного элемента
5. методы `lastIndexOf` — возвращают позицию последнего найденного элемента

Если искомый элемент не найден, методы вернут в строке -1.
Попробуем найти порядковый номер букв `A`, `K`, `Z`, `Я` в английском алфавите, но будем иметь ввиду, что индексация символов в строке в **Java** начинается с нуля:

```java
public static void main(String[] args) {
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    System.out.println(alphabet.indexOf('A')); 
    System.out.println(alphabet.indexOf('K')); 
    System.out.println(alphabet.indexOf('Z')); 
    System.out.println(alphabet.indexOf('Я')); 
}
```

### Извлечение подстроки из строки

Для извлечения подстроки из строки класс String в Java предоставляет методы:

- `substring(int beginIndex)`
- `substring(int beginIndex, int endIndex)`

Рассмотрим, как с помощью методов определения позиции элемента и извлечения подстроки мы можем получить имя файла из его пути:

```java
public static void main(String[] args) {
    String filePath = "D:\\Movies\\Futurama.mp4";
    int lastFileSeparatorIndex = filePath.lastIndexOf('\\');
    String fileName = filePath.substring(lastFileSeparatorIndex + 1);
    System.out.println(fileName); 
}
```

### Перевод строки в верхний/нижний регистр:

Класс String предоставляет методы для перевода строки в верхний и нижний регистры:

- `toLowerCase()`
- `toUpperCase()`

Рассмотрим работу данных методов на примере:

```java
public static void main(String[] args) {
    String fry = "Philip J. Fry";

    String lowerCaseFry = fry.toLowerCase();
    String upperCaseFry = fry.toUpperCase();

    System.out.println(lowerCaseFry); 
    System.out.println(upperCaseFry); 
}
```

