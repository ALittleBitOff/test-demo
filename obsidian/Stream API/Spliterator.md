Пришло время немного углубиться в работу **`Stream API`** изнутри. Элементы стримов нужно не только итерировать, но ещё и разделять на части и отдавать другим потокам. За итерацию и разбиение отвечает Spliterator. Он даже звучит как Iterator, только с приставкой `Split` — разделять.  
  
Методы интерфейса:  
   - `trySplit` — как следует из названия, пытается разделить элементы на две части. Если это сделать не получается, либо элементов недостаточно для разделения, то вернёт null. В остальных случаях возвращает ещё один Spliterator с частью данных.  
   - `tryAdvance(Consumer action)` — если имеются элементы, для которых можно применить действие, то оно применяется и возвращает `true`, в противном случае возвращается `false`, но действие не выполняется.  
   - `estimateSize()` — возвращает примерное количество элементов, оставшихся для обработки, либо `Long.MAX_VALUE`, если стрим бесконечный или посчитать количество невозможно.  
   - `characteristics()` — возвращает характеристики сплитератера.  
### Характеристики

В методе `sorted` и `distinct` было упомянуто, что если стрим помечен как отсортированный или содержащий уникальные элементы, то соответствующие операции проводиться не будут. Вот характеристики сплитератера и влияют на это.  
  
   - **DISTINCT** — все элементы уникальны. Сплитератеры всех реализаций `Set` содержат эту характеристику.  
   - **SORTED** — все элементы отсортированы.  
   - **ORDERED** — порядок имеет значение. Сплитератеры большинства коллекций содержат эту характеристику, а `HashSet`, к примеру, нет.  
   - **SIZED** — количество элементов точно известно.  
   - **SUBSIZED** — количество элементов каждой разбитой части точно известно.  
   - **NONNULL** — в элементах не встречается `null`. Некоторые коллекции из `java.util.concurrent`, в которые нельзя положить `null`, содержат эту характеристику.
   - **IMMUTABLE** — источник является иммутабельным и в него нельзя больше добавить элементов, либо удалить их.  
   - **CONCURRENT** — источник лоялен к любым изменениям.  
  
Разумеется, характеристики могут быть изменены при выполнении цепочки операторов. Например, после sorted добавляется характеристика `SORTED`, после filter теряется `SIZED` и т.д. 
### Жизненный цикл сплитератора

Чтобы понять когда и как сплитератор вызывает тот или иной метод, давайте создадим обёртку, которая [логирует все вызовы](https://gist.github.com/aNNiMON/71488fe8bfc6781d641fd4d0ed1f1aa7). Чтобы из сплитератора создать стрим, используется класс `StreamSupport`.  

```java
long count = StreamSupport.stream(
 
    Arrays.asList(0, 1, 2, 3).spliterator(), true)
    .count();
```

![list-spliterator.png](https://annimon.com/ablogs/file806/list-spliterator.png "list-spliterator.png")  

На рисунке показан один из возможных вариантов работы сплитератора. `characteristics` везде возвращает `ORDERED` | `SIZED` | `SUBSIZED`, так как в `List` порядок имеет значение, количество элементов и всех разбитых кусков также известно. `trySplit` делит последовательность пополам, но не обязательно каждая часть будет отправлена новому потоку. В параллельном стриме новый поток может и не создаться, т.к. всё успевает обработаться в главном потоке. В данном же случае, новый поток успевал обработать части до того, как это делал главный поток.  


```java
Spliterator<Integer> s = IntStream.range(0, 4)
 
    .boxed()
    .collect(Collectors.toSet())
    .spliterator();
long count = StreamSupport.stream(s, true).count();
```

Здесь у сплитератора характеристикой будет `SIZED` | `DISTINCT`, а вот у каждой части характеристика `SIZED` теряется, остаётся только DISTINCT, потому что нельзя поделить множество так, чтобы размер каждой части был известен.  
В случае с `Set` было три вызова `trySplit`, первый якобы делил элементы поровну, после двух других каждая из частей возвращала `estimateSize: 1`, однако во всех, кроме одной попытка вызвать `tryAdvance` не увенчалась успехом — возвращался `false`. А вот на одном из частей, который для `estimateSize` также возвращал 1, было 4 успешных вызова `tryAdvance`. Это и подтверждает тот факт, что `estimateSize` не обязательно должен возвращать действительное число элементов.  
  
```java
Arrays.spliterator(new int[] {0, 1, 2, 3});
   
Stream.of(0, 1, 2, 3).spliterator();
```

Ситуация аналогична работе `List`, только характеристики возвращали `ORDERED` | `SIZED` | `SUBSIZED` | `IMMUTABLE`.  
  

```java
Stream.of(0, 1, 2, 3).distinct().spliterator();  
```

Здесь `trySplit` возвращал `null`, а значит поделить последовательно не представлялось возможным. Иерархия вызовов:  


```java
[main] characteristics: ORDERED | DISTINCT
 
[main] estimateSize: 4
 
[main] trySplit: null
 
[main] characteristics: ORDERED | DISTINCT
 
[main] tryAdvance: true
 
[main] tryAdvance: true
 
[main] tryAdvance: true
 
[main] tryAdvance: true
 
[main] tryAdvance: false
 
count: 4
```

```java
Stream.of(0, 1, 2, 3)
 
    .distinct()
    .map(x -> x + 1)
    .spliterator();
```

Всё, как и выше, только теперь после применения оператора map, флаг `DISTINCT` исчез.  
### Реализация сплитератора

Для правильной реализации сплитератора нужно продумать, как сделать разбиение и обозначить характеристики стрима. Давайте напишем сплитератор, генерирующий последовательность чисел Фибоначчи.  
Для упрощения задачи нам будет известно максимальное количество элементов для генерирования. А значит мы можем разделять последовательность пополам, а потом быстро просчитывать нужные числа по новому индексу.  
Осталось определиться с характеристиками. Мы уже решили, что размер последовательности нам будет известен, а значит будет известен и размер каждой разбитой части. Порядок будет важен, так что без флага `ORDERED` не обойтись. Последовательность Фибоначчи также отсортирована — каждый последующий элемент всегда не меньше предыдущего.  
А вот с флагом `DISTINCT`, кажется, промах. 0 1 1 2 3, две единицы повторяются, а значит не видать нам этого флага?  
На самом деле ничто нам не мешает просчитывать флаги автоматически. Если часть последовательности не будет затрагивать начальные индексы, то этот флаг можно выставить.  
```java
int distinct = (index >= 2) ? DISTINCT : 0;
 
return ORDERED | distinct | SIZED | SUBSIZED | IMMUTABLE | NONNULL;  
```

Полная реализация класса:  

```java
import java.math.BigInteger;
 
import java.util.Spliterator;
 
import java.util.function.Consumer;
 
public class FibonacciSpliterator implements Spliterator<BigInteger> {
 
    private final int fence;
    private int index;
    private BigInteger a, b;
    public FibonacciSpliterator(int fence) {
        this(0, fence);
    }
    protected FibonacciSpliterator(int start, int fence) {
        this.index = start;
        this.fence = fence;
        recalculateNumbers(start);
    }
    private void recalculateNumbers(int start) {
        a = fastFibonacciDoubling(start);
        b = fastFibonacciDoubling(start + 1);
    }
     @Override
     public boolean tryAdvance(Consumer<? super BigInteger> action) {
         if (index >= fence) {
             return false;
         }
         action.accept(a);
         BigInteger c = a.add(b);
         a = b;
         b = c;
         index++;
          return true;
      }
      @Override
      public FibonacciSpliterator trySplit() {
          int lo = index;
          int mid = (lo + fence) >>> 1;
          if (lo >= mid) {
              return null;
          }
          index = mid;
           recalculateNumbers(mid);
           return new FibonacciSpliterator(lo, mid);
       }
       @Override
       public long estimateSize() {
           return fence - index;
       }
       @Override
       public int characteristics() {
           int distinct = (index >= 2) ? DISTINCT : 0;
         return ORDERED | distinct | SIZED | SUBSIZED | IMMUTABLE | NONNULL;
 	 }
 	 /*
 	  * https://www.nayuki.io/page/fast-fibonacci-algorithms
 	  */
 	 public static BigInteger fastFibonacciDoubling(int n) {
 		 BigInteger a = BigInteger.ZERO;
 		 BigInteger b = BigInteger.ONE;
 		 for (int bit = Integer.highestOneBit(n); bit != 0; bit >>>= 1) {
 			 BigInteger d = a.multiply(b.shiftLeft(1).subtract(a));
 			BigInteger e = a.multiply(a).add(b.multiply(b));
 			a = d;
 			b = e;
 			if ((n & bit) != 0) {
 				BigInteger c = a.add(b);
 				a = b;
 				b = c;
 			}
 		}
 		return a;
 	}
}
```

Вот как разбиваются теперь элементы параллельного стрима:  

```java
StreamSupport.stream(new FibonacciSpliterator(7), true)
 
    .count();
```

![fibonaccispliterator.png](https://annimon.com/ablogs/file797/fibonaccispliterator.png "fibonaccispliterator.png")  
  

```java
StreamSupport.stream(new FibonacciSpliterator(500), true)
 
    .count();
```

![fibonaccispliterator500.png](https://annimon.com/ablogs/file798/fibonaccispliterator500.png "fibonaccispliterator500.png")