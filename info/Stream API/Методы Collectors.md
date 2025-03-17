#### `toList()`

Самый распространённый метод. Собирает элементы в List.  
  
---
#### `toSet()`

Собирает элементы в множество.  

---
#### `toCollection(Supplier collectionFactory)`

Собирает элементы в заданную коллекцию. Если нужно конкретно указать, какой `List`, `Set` или другую коллекцию мы хотим использовать, то этот метод поможет.  


```java
Deque<Integer> deque = Stream.of(1, 2, 3, 4, 5)
 
    .collect(Collectors.toCollection(ArrayDeque::new));
Set<Integer> set = Stream.of(1, 2, 3, 4, 5)
 
    .collect(Collectors.toCollection(LinkedHashSet::new));
```  

---
#### `toMap(Function keyMapper, Function valueMapper)`

Собирает элементы в `Map`. Каждый элемент преобразовывается в ключ и в значение, основываясь на результате функций `keyMapper` и `valueMapper` соответственно. Если нужно вернуть тот же элемент, что и пришел, то можно передать `Function.identity()`.  


```java
Map<Integer, Integer> map1 = Stream.of(1, 2, 3, 4, 5)
 
    .collect(Collectors.toMap(
        Function.identity(),
        Function.identity()
    ));
// {1=1, 2=2, 3=3, 4=4, 5=5}
 
Map<Integer, String> map2 = Stream.of(1, 2, 3)
 
    .collect(Collectors.toMap(
        Function.identity(),
         i -> String.format("%d * 2 = %d", i, i * 2)
    ));
// {1="1 * 2 = 2", 2="2 * 2 = 4", 3="3 * 2 = 6"}

Map<Character, String> map3 = Stream.of(50, 54, 55)

    .collect(Collectors.toMap(
        i -> (char) i.intValue(),
        i -> String.format("<%d>", i)
    ));
// {'2'="<50>", '6'="<54>", '7'="<55>"}
```
#### `toMap(Function keyMapper, Function valueMapper, BinaryOperator mergeFunction)`

Аналогичен первой версии метода, только в случае, когда встречается два одинаковых ключа, позволяет объединить значения.  


```java
Map<Integer, String> map4 = Stream.of(50, 55, 69, 20, 19, 52)
 
    .collect(Collectors.toMap(
        i -> i % 5,
        i -> String.format("<%d>", i),
        (a, b) -> String.join(", ", a, b)
    ));
// {0="<50>, <55>, <20>", 2="<52>", 4="<64>, <19>"} 
```

В данном случае, для чисел 50, 55 и 20, ключ одинаков и равен 0, поэтому значения накапливаются. Для 64 и 19 аналогично.  
#### `toMap(Function keyMapper, Function valueMapper, BinaryOperator mergeFunction, Supplier mapFactory)`

Всё то же, только позволяет указывать, какой именно класс Map использовать.  


```java
Map<Integer, String> map5 = Stream.of(50, 55, 69, 20, 19, 52)
 
    .collect(Collectors.toMap(
        i -> i % 5,
        i -> String.format("<%d>", i),
        (a, b) -> String.join(", ", a, b),
        LinkedHashMap::new
    ));
// {0=<50>, <55>, <20>, 4=<69>, <19>, 2=<52>}
```

Отличие этого примера от предыдущего в том, что теперь сохраняется порядок, благодаря `LinkedHashList`.  
#### `toConcurrentMap(Function keyMapper, Function valueMapper)`

`toConcurrentMap(Function keyMapper, Function valueMapper, BinaryOperator mergeFunction)`

`toConcurrentMap(Function keyMapper, Function valueMapper, BinaryOperator mergeFunction, Supplier mapFactory)`

Всё то же самое, что и `toMap`, только работаем с `ConcurrentMap`.  
  
---
#### `collectingAndThen(Collector downstream, Function finisher)`

Собирает элементы с помощью указанного коллектора, а потом применяет к полученному результату функцию.  


```java
List<Integer> list = Stream.of(1, 2, 3, 4, 5)
 
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        Collections::unmodifiableList));
System.out.println(list.getClass());
 
// class java.util.Collections$UnmodifiableRandomAccessList
 
List<String> list2 = Stream.of("a", "b", "c", "d")
 
    .collect(Collectors.collectingAndThen(
            Collectors.toMap(Function.identity(), s -> s + s),
            map -> map.entrySet().stream()))
    .map(e -> e.toString())
    .collect(Collectors.collectingAndThen(
             Collectors.toList(),
             Collections::unmodifiableList));
list2.forEach(System.out::println);

// a=aa

// b=bb

// c=cc

// d=dd
```

---
#### `joining()`

`joining(CharSequence delimiter)`

`joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix)`

Собирает элементы, реализующие интерфейс `CharSequence`, в единую строку. Дополнительно можно указать разделитель, а также префикс и суффикс для всей последовательности.  


```java
String s1 = Stream.of("a", "b", "c", "d")
 
    .collect(Collectors.joining());
System.out.println(s1);
 
// abcd
 
String s2 = Stream.of("a", "b", "c", "d")
 
    .collect(Collectors.joining("-"));
System.out.println(s2);
 
// a-b-c-d
 
String s3 = Stream.of("a", "b", "c", "d")
 
    .collect(Collectors.joining(" -> ", "[ ", " ]"));
System.out.println(s3);

// [ a -> b -> c -> d ]
```

---
#### `summingInt(ToIntFunction mapper)`

#### `summingLong(ToLongFunction mapper)`

#### `summingDouble(ToDoubleFunction mapper)`

Коллектор, который преобразовывает объекты в `int`/`long`/`double` и подсчитывает сумму.  

#### `averagingInt(ToIntFunction mapper)`

#### `averagingLong(ToLongFunction mapper)`

#### averagingDouble(ToDoubleFunction mapper)

Аналогично, но со средним значением.  
  

#### `summarizingInt(ToIntFunction mapper)`

#### `summarizingLong(ToLongFunction mapper)`

#### `summarizingDouble(ToDoubleFunction mapper)`

Аналогично, но с полной статистикой.  


```java
Integer sum = Stream.of("1", "2", "3", "4")
 
    .collect(Collectors.summingInt(Integer::parseInt));
System.out.println(sum);
 
// 10
 
Double average = Stream.of("1", "2", "3", "4")
 
    .collect(Collectors.averagingInt(Integer::parseInt));
System.out.println(average);
 
// 2.5
 
DoubleSummaryStatistics stats = Stream.of("1.1", "2.34", "3.14", "4.04")
 
    .collect(Collectors.summarizingDouble(Double::parseDouble));
System.out.println(stats);

// DoubleSummaryStatistics{count=4, sum=10.620000, min=1.100000, average=2.655000, max=4.040000}
```

Все эти методы и несколько последующих, зачастую используются в качестве составных коллекторов для группировки или `collectingAndThen`. В том виде, в котором они показаны на примерах используются редко. Я лишь показываю пример, что они возвращают, чтобы было понятнее.  

---
#### `counting()`

Подсчитывает количество элементов.  

```java
Long count = Stream.of("1", "2", "3", "4")
 
    .collect(Collectors.counting());
System.out.println(count);
 
// 4
```

---
#### `filtering(Predicate predicate, Collector downstream)`

#### `mapping(Function mapper, Collector downstream)`

#### `flatMapping(Function downstream)`

#### `reducing(BinaryOperator op)  `
`reducing(T identity, BinaryOperator op)  `
`reducing(U identity, Function mapper, BinaryOperator op)`

Специальная группа коллекторов, которая применяет операции `filter`, `map`, `flatMap` и `reduce`. `filtering` и `flatMapping` появились в **Java 9**.  

```java
List<Integer> ints = Stream.of(1, 2, 3, 4, 5, 6)
 
    .collect(Collectors.filtering(
        x -> x % 2 == 0,
        Collectors.toList()));
// 2, 4, 6
 
String s1 = Stream.of(1, 2, 3, 4, 5, 6)
 
    .collect(Collectors.filtering(
        x -> x % 2 == 0,
        Collectors.mapping(
            x -> Integer.toString(x),
            Collectors.joining("-")
        )
    ));
// 2-4-6

String s2 = Stream.of(2, 0, 1, 3, 2)

    .collect(Collectors.flatMapping(
        x -> IntStream.range(0, x).mapToObj(Integer::toString),
        Collectors.joining(", ")
    ));
 // 0, 1, 0, 0, 1, 2, 0, 1

int value = Stream.of(1, 2, 3, 4, 5, 6)

    .collect(Collectors.reducing(
        0, (a, b) -> a + b
    ));
// 21

String s3 = Stream.of(1, 2, 3, 4, 5, 6)

    .collect(Collectors.reducing(
        "", x -> Integer.toString(x), (a, b) -> a + b
    ));
 // 123456
```

---
#### `minBy(Comparator comparator)`

#### `maxBy(Comparator comparator)`

Поиск минимального/максимального элемента, основываясь на заданном компараторе.  


```java
Optional<String> min = Stream.of("ab", "c", "defgh", "ijk", "l")
 
    .collect(Collectors.minBy(Comparator.comparing(String::length)));
min.ifPresent(System.out::println);
 
// c
 
Optional<String> max = Stream.of("ab", "c", "defgh", "ijk", "l")
 
    .collect(Collectors.maxBy(Comparator.comparing(String::length)));
max.ifPresent(System.out::println);
 
// defgh
```

---
#### `groupingBy(Function classifier)  `
`groupingBy(Function classifier, Collector downstream)  `
`groupingBy(Function classifier, Supplier mapFactory, Collector downstream)`

Группирует элементы по критерию, сохраняя результат в `Map`. Вместе с представленными выше агрегирующими коллекторами, позволяет гибко собирать данные. Подробнее о комбинировании в разделе [Примеры](https://annimon.com/article/2778#examples).  
#### `groupingByConcurrent(Function classifier)  `
`groupingByConcurrent(Function classifier, Collector downstream)  `
`groupingByConcurrent(Function classifier, Supplier mapFactory, Collector downstream)`

Аналогичный набор методов, только сохраняет в ConcurrentMap.  


```java
Map<Integer, List<String>> map1 = Stream.of(
 
    "ab", "c", "def", "gh", "ijk", "l", "mnop")
    .collect(Collectors.groupingBy(String::length));
map1.entrySet().forEach(System.out::println);
 
// 1=[c, l]
 
// 2=[ab, gh]
 
// 3=[def, ijk]
 
// 4=[mnop]
 
Map<Integer, String> map2 = Stream.of(
 
    "ab", "c", "def", "gh", "ijk", "l", "mnop")
    .collect(Collectors.groupingBy(
        String::length,
        Collectors.mapping(
            String::toUpperCase,
            Collectors.joining())
    ));
map2.entrySet().forEach(System.out::println);

// 1=CL

// 2=ABGH

 // 3=DEFIJK

// 4=MNOP

Map<Integer, List<String>> map3 = Stream.of(

    "ab", "c", "def", "gh", "ijk", "l", "mnop")
    .collect(Collectors.groupingBy(
        String::length,
        LinkedHashMap::new,
        Collectors.mapping(
            String::toUpperCase,
            Collectors.toList())
     ));
 map3.entrySet().forEach(System.out::println);

// 2=[AB, GH]

// 1=[C, L]

// 3=[DEF, IJK]

// 4=[MNOP]
```

---
#### `partitioningBy(Predicate predicate)  `
`partitioningBy(Predicate predicate, Collector downstream)`

Ещё один интересный метод. Разбивает последовательность элементов по какому-либо критерию. В одну часть попадают все элементы, которые удовлетворяют переданному условию, во вторую — все, которые не удовлетворяют.  


```java
Map<Boolean, List<String>> map1 = Stream.of(
 
    "ab", "c", "def", "gh", "ijk", "l", "mnop")
    .collect(Collectors.partitioningBy(s -> s.length() <= 2));
map1.entrySet().forEach(System.out::println);
 
// false=[def, ijk, mnop]
 
// true=[ab, c, gh, l]
 
Map<Boolean, String> map2 = Stream.of(
 
    "ab", "c", "def", "gh", "ijk", "l", "mnop")
    .collect(Collectors.partitioningBy(
        s -> s.length() <= 2,
        Collectors.mapping(
            String::toUpperCase,
            Collectors.joining())
    ));
map2.entrySet().forEach(System.out::println);

// false=DEFIJKMNOP

// true=ABCGHL
```  

---
#### `teeing(Collector downstream1, Collector downstream2, BiFunction merger)
`
Если вы знакомы с командой tee в `Unix`, вы, возможно, уже догадались, что делает этот коллектор. Он собирает элементы в два разных коллектора `downstream1` и `downstream2`, а затем объединяет результат в функции `merger`.  

```java
String result = Stream.of(
 
    "ab", "c", "def", "gh", "ijk", "l", "mnop")
    .collect(Collectors.teeing(
        Collectors.counting(),
        Collectors.filtering(s -> s.length() <= 2, Collectors.toList()),
        (elementsCount, filteredList) ->
            "From the %d elements, only %d were filtered: %s"
                .formatted(elementsCount, filteredList.size(), filteredList)
        ));
System.out.println(result);

// From the 7 elements, only 4 were filtered: [ab, c, gh, l]

record Range(int min, int max) {}

Range range = new Random(42).ints(7)

    .boxed()
    .collect(Collectors.teeing(
        Collectors.minBy(Integer::compare),
        Collectors.maxBy(Integer::compare),
        (min, max) -> new Range(
            min.orElse(Integer.MIN_VALUE),
             max.orElse(Integer.MAX_VALUE)
         )));
 System.out.println(range);

// Range[min=-1360544799, max=1325939940]
```
