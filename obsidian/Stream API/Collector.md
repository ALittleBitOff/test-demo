Интерфейс `java.util.stream.Collector` служит для сбора элементов стрима в некоторый мутабельный контейнер. Он состоит из таких методов: 

-`Supplier<A> supplier()` — функция, которая создаёт экземпляры контейнеров.

-`BiConsumer<A,T> accumulator()` — функция, которая кладёт новый элемент в контейнер.

-`BinaryOperator<A> combiner()` — функция, которая объединяет два контейнера в один. В параллельных стримах каждая часть может собираться в отдельный экземпляр контейнера и в итоге необходимо их объединять в один результирующий.

-`Function<A,R> finisher()` — функция, которая преобразовывает весь контейнер в конечный результат. Например, можно обернуть `List` в `Collections.unmodifiableList.Set<Characteristics> characteristics()` — возвращает характеристики коллектора, чтобы внутренняя реализация знала, с чем имеет дело. Например, можно указать, что коллектор поддерживает многопоточность.  
  
Характеристики: 

   - **CONCURRENT** — коллектор поддерживает многопоточность, а значит отдельные части стрима могут быть успешно положены в контейнер из другого потока.  
   - **UNORDERED** — коллектор не зависит от порядка поступаемых элементов.  
   - **IDENTITY_FINISH** — функция `finish()` имеет стандартную реализацию (Function.identity()), а значит её можно не вызывать.  
### 8.1. Реализация собственного коллектора
  
Прежде чем писать свой коллектор, нужно убедиться, что задачу нельзя решить при помощи комбинации стандартных коллекторов.  
К примеру, если нужно собрать лишь уникальные элементы в список, то можно собрать элементы сначала в `LinkedHashSet`, чтобы сохранился порядок, а потом все элементы добавить в `ArrayList`. Комбинация `collectingAndThen` с `toCollection` и функцией, передающей полученный `Set` в конструктор `ArrayList`, делает то, что задумано:  


```java
Stream.of(1, 2, 3, 1, 9, 2, 5, 3, 4, 8, 2)
 
    .collect(Collectors.collectingAndThen(
        Collectors.toCollection(LinkedHashSet::new),
        ArrayList::new));
// 1 2 3 9 5 4 8
```
  
А вот если задача состоит в том, чтобы собрать уникальные элементы в одну часть, а повторяющиеся в другую, например в `Map<Boolean, List>`, то при помощи `partitioningBy` получится не очень красиво:  

```java
final Set<Integer> elements = new HashSet<>();
 
Stream.of(1, 2, 3, 1, 9, 2, 5, 3, 4, 8, 2)
 
    .collect(Collectors.partitioningBy(elements::add))
    .forEach((isUnique, list) -> System.out.format("%s: %s%n", isUnique ? "unique" : "repetitive", list));
```

Здесь приходится создавать `Set` и в предикате коллектора его использовать, что нежелательно. Можно превратить лямбду в анонимную функцию, но это ещё хуже:  


```java
new Predicate<Integer>() {
 
    final Set<Integer> elements = new HashSet<>();
    @Override
    public boolean test(Integer t) {
        return elements.add(t);
    }
}
```
  
Для создания своего коллектора есть два пути: 

   1. Создать класс, реализующий интерфейс `Collector`.  
   2. Воспользоваться фабрикой `Collector.of`. 
 
Если нужно сделать что-то универсальное, чтобы работало для любых типов, то есть использовать дженерики, то во втором варианте можно просто сделать статическую функцию, а внутри использовать `Collector.of`.  
  
Вот полученный коллектор.  


```java
public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningByUniqueness() {
 
    return Collector.<T, Map.Entry<List<T>, Set<T>>, Map<Boolean, List<T>>>of(
        () -> new AbstractMap.SimpleImmutableEntry<>(
                    new ArrayList<T>(), new LinkedHashSet<>()),
        (c, e) -> {
            if (!c.getValue().add(e)) {
                c.getKey().add(e);
            }
        },
        (c1, c2) -> {
            c1.getKey().addAll(c2.getKey());
            for (T e : c2.getValue()) {
                if (!c1.getValue().add(e)) {
                    c1.getKey().add(e);
                }
            }
            return c1;
        },
        c -> {
             Map<Boolean, List<T>> result = new HashMap<>(2);
             result.put(Boolean.FALSE, c.getKey());
             result.put(Boolean.TRUE, new ArrayList<>(c.getValue()));
             return result;
        });
}  
```

Давайте теперь разбираться.  
  
Интерфейс `Collector` объявлен так:  
interface `Collector<T, A, R>`

   `T` - тип входных элементов.  
   `A` - тип контейнера, в который будут поступать элементы.  
   `R` - тип результата.  
  
Сигнатура метода, возвращающего коллектор такова:  
`public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningByUniqueness()`
Он принимает элементы типа `T`, возвращает `Map<Boolean, List<T>>`, как и `partitioningBy`. Знак вопроса (джокер) в среднем параметре говорит о том, что внутренний тип реализации для публичного **API** не важен. Многие методы класса `Collectors` содержат джокер в качестве типа контейнера.  
  
`return Collector.<T, Map.Entry<List<T>, Set<T>>, Map<Boolean, List<T>>>of ` 
Вот здесь уже пришлось указать тип контейнера. Так как в **Java** нет класса `Pair` или `Tuple`, то два разных типа можно положить в `Map.Entry`.  
  
```java
// supplier
 
() -> new AbstractMap.SimpleImmutableEntry<>(
 
        new ArrayList<>(), new LinkedHashSet<>())
```

Контейнером будет `AbstractMap.SimpleImmutableEntry`. В ключе будет список повторяющихся элементов, в значении — множество с уникальными элементами.  
  
```java
// accumulator
 
(c, e) -> {
 
    if (!c.getValue().add(e)) {
        c.getKey().add(e);
    }
}
```

Здесь всё просто. Если элемент нельзя добавить во множество (по причине того, что там уже есть такой элемент), то добавляем его в список повторяющихся элементов.  


```java
// combiner
 
(c1, c2) -> {
 
    c1.getKey().addAll(c2.getKey());
    for (T e : c2.getValue()) {
        if (!c1.getValue().add(e)) {
            c1.getKey().add(e);
        }
    }
    return c1;
}
```

Нужно объединить два `Map.Entry`. Списки повторяющихся элементов можно объединить вместе, а вот с уникальными элементами так просто не выйдет — нужно пройтись поэлементно и повторить всё то, что делалось в функции-аккумуляторе. Кстати, лямбду-аккумулятор можно присвоить переменной и тогда цикл можно превратить в `c2.getValue().forEach(e -> accumulator.accept(c1, e));  `
  

```java
// finisher
 
c -> {
 
    Map<Boolean, List<T>> result = new HashMap<>(2);
    result.put(Boolean.FALSE, c.getKey());
    result.put(Boolean.TRUE, new ArrayList<>(c.getValue()));
    return result;
} 
```

Наконец, возвращаем необходимый результат. В `map.get(Boolean.TRUE)` будут уникальные, а в `map.get(Boolean.FALSE)` — повторяющиеся элементы.  

```java
Map<Boolean, List<Integer>> map;
 
map = Stream.of(1, 2, 3, 1, 9, 2, 5, 3, 4, 8, 2)
 
    .collect(partitioningByUniqueness());
// {false=[1, 2, 3, 2], true=[1, 2, 3, 9, 5, 4, 8]}
```

Хорошей практикой является создание коллекторов, которые принимают ещё один коллектор и зависят от него. Например, можно будет складывать элементы не только в `List`, но и в любую другую коллекцию `(Collectors.toCollection)`, либо в строку `(Collectors.joining)`.  

```java
public static <T, D, A> Collector<T, ?, Map<Boolean, D>> partitioningByUniqueness(
 
        Collector<? super T, A, D> downstream) {
    class Holder<A, B> {
        final A unique, repetitive;
        final B set;
        Holder(A unique, A repetitive, B set) {
            this.unique = unique;
            this.repetitive = repetitive;
            this.set = set;
        }
    }
    BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
    BinaryOperator<A> downstreamCombiner = downstream.combiner();
    BiConsumer<Holder<A, Set<T>>, T> accumulator = (t, element) -> {
        A container = t.set.add(element) ? t.unique : t.repetitive;
        downstreamAccumulator.accept(container, element);
    };
    return Collector.<T, Holder<A, Set<T>>, Map<Boolean, D>>of(
            () -> new Holder<>(
                 downstream.supplier().get(),
                 downstream.supplier().get(),
                 new HashSet<>() ),
             accumulator,
             (t1, t2) -> {
                 downstreamCombiner.apply(t1.repetitive, t2.repetitive);
                 t2.set.forEach(e -> accumulator.accept(t1, e));
                 return t1;
             },
             t -> {
                  Map<Boolean, D> result = new HashMap<>(2);
                  result.put(Boolean.FALSE, downstream.finisher().apply(t.repetitive));
                  result.put(Boolean.TRUE, downstream.finisher().apply(t.unique));
                  t.set.clear();
                  return result;
              });
  }
```

Алгоритм остался тем же, только теперь уже нельзя во второй контейнер сразу же складывать уникальные элементы, приходится создавать новый `set`. Для удобства также добавлен класс `Holder`, который хранит два контейнера для уникальных и повторяющихся элементов, а также само множество.  
  
Все операции теперь нужно проводить через переданный коллектор, именуемый downstream. Именно он сможет поставить контейнер нужного типа `(downstream.supplier().get())`, добавить элемент в этот контейнер `(downstream.accumulator().accept(container, element))`, объединить контейнеры и создать окончательный результат.  

```java
Stream.of(1, 2, 3, 1, 9, 2, 5, 3, 4, 8, 2)
 
    .map(String::valueOf)
    .collect(partitioningByUniqueness(Collectors.joining("-")))
    .forEach((isUnique, str) -> System.out.format("%s: %s%n", isUnique ? "unique" : "repetitive", str));
// repetitive: 1-2-3-2
 
// unique: 1-2-3-9-5-4-8
```
  
Кстати, первую реализацию метода без аргументов можно теперь заменить на:  

```java
public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningByUniqueness() {
 
    return partitioningByUniqueness(Collectors.toList());
}
```
