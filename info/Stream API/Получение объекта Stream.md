Пока что хватит теории. Пришло время посмотреть, как создать или получить объект java.util.stream.Stream.  
  - Пустой стрим: `Stream.empty() // Stream<String>  `
  - Стрим из `List`: `list.stream() // Stream<String>  `
  - Стрим из `Map`: `map.entrySet().stream() // Stream<Map.Entry<String, String>>`
  - Стрим из массива: `Arrays.stream(array) // Stream<String>  `
  - Стрим из указанных элементов: `Stream.of("a", "b", "c") // Stream<String>  `
  
А вот и пример:  

```java
public static void main(String[] args) {
    
    List<String> list = Arrays.stream(args)
        .filter(s -> s.length() <= 2)
        .collect(Collectors.toList());
}
```
  
В данном примере источником служит метод `Arrays.stream`, который из массива `args` делает стрим. Промежуточный оператор `filter` отбирает только те строки, длина которых не превышает два. Терминальный оператор `collect` собирает полученные элементы в новый список.  
  
И ещё один пример:  

```java
IntStream.of(120, 410, 85, 32, 314, 12)
    
    .filter(x -> x < 300)
    .map(x -> x + 11)
    .limit(3)
    .forEach(System.out::print)
```
  
Здесь уже три промежуточных оператора:  
  - `filter` — отбирает элементы, значение которых меньше 300,  
  - `map` — прибавляет 11 к каждому числу,  
  - `limit` — ограничивает количество элементов до 3.  
  
Терминальный оператор `forEach` применяет функцию `print` к каждому приходящему элементу.  

На ранних версиях Java этот пример выглядел бы так:  

```java
int[] arr = {120, 410, 85, 32, 314, 12};
    
int count = 0;
    
for (int x : arr) {
    
    if (x >= 300) continue;
    x += 11;
    count++;
    if (count > 3) break;
    System.out.print(x);
}
```

С увеличением числа операторов код в ранних версиях усложнялся бы на порядок, не говоря уже о том, что разбить вычисления на несколько потоков при таком подходе было бы крайне непросто.
