
Дословно **«Iterator»** можно перевести как **«переборщик»**. То есть это некая сущность, способная перебрать все элементы в коллекции. При этом она позволяет это сделать без вникания во внутреннюю структуру и устройство коллекций.

Представим на секунду, что iterator в Java отсутствует.
В таком случае всем и каждому придётся нырнуть в самые глубины коллекций и по-настоящему разобраться, чем отличается, `ArrayList` от `LinkedList` и `HashSet` от `TreeSet`.
## Методы, которые должен имплементировать Iterator

`boolean hasNext()` — если в итерируемом объекте (пока что это Collection) остались еще значение — метод вернет `true`, если значения кончились `false`.
`E next()` — возвращает следующий элемент коллекции (объекта).
Если элементов больше нет (не было проверки `hasNext()`, а мы вызвали `next()`, достигнув конца коллекции), метод бросит `NoSuchElementException`.
`void remove()` — удалит элемент, который был в последний раз получен методом `next()`. Метод может бросить:

- `UnsupportedOperationException`, если данный итератор не поддерживает метод `remove()` (в случае с read-only коллекциями, например)
- `IllegalStateException`, если метод `next()` еще не был вызван, или если `remove()` уже был вызван после последнего вызова `next()`.

Итак, iterator для List — самая распространенная имплементация. 
Итератор идет от начала коллекции к ее концу: 

- смотрит есть ли в наличии следующий элемент и возвращает его, если таковой находится.

На основе этого несложного алгоритма построен цикл `for-each`.
Его расширение — `ListIterator`. Рассмотрим дополнительные методы **java** `list iterator`:

- `void add(E e)` — вставляет элемент `E` в `List`;
- `boolean hasPrevious()` — вернет `true`, если при обратном переборе `List` имеются элементы;
- `int nextIndex()`— вернет индекс следующего элемента;
- `E previous()` — вернет предыдущий элемент листа;
- `int previousIndex()` — вернет индекс предыдущего элемента;
- `void set(E e)` — заменит элемент, возвращенный последним вызовом `next()` или `previous()` на элемент `e`.

Рассмотрим небольшой пример.

Создадим `List`, содержащий в себе строки приветствия обучающихся:

```java
List<String> list = new ArrayList<>();
list.add("Привет");
list.add("Обучающимся");
list.add("На");
list.add("JavaRush");
```

Теперь получим для него итератор и выведем в консоль все содержащиеся строки:

```java
Iterator iterator = list.iterator();
while (iterator.hasNext()) {
    System.out.println(iterator.next());
}
```

Сейчас будет «узкое место»:

Java Collections как ты, вероятно, знаешь (а если не знаешь, разберись), расширяют интерфейс `Iterable`, но это не означает, что только `List`, `Set` и `Queue` поддерживают итератор. Для `java Map iterator` также поддерживается, но его необходимо вызывать для `Map.entrySet()`:

```java
Map<String, Integer> map = new HashMap<>();
Iterator mapIterator = map.entrySet().iterator();
```

Тогда метод `next()` будет возвращать объект `Entry`, содержащий в себе пару «ключ»-«значение». Дальше все аналогично с `List`:

```java
while (mapIterator.hasNext()) {
    Map.Entry<String, Integer> entry = mapIterator.next();
    System.out.println("Key: " + entry.getKey());
    System.out.println("Value: " + entry.getValue());
}
```

Ты думаешь:

«Стоп. Мы говорим про интерфейс, а в заголовке статьи написано «Паттерн».
То есть, паттерн iterator – это интерфейс Iterator? Или интерфейс — это паттерн?»

Если это слово встречается впервые, даю справку:

- паттерн — это шаблон проектирования, некое поведение, которого должен придерживаться класс или множество взаимосвязанных классов.

**Итератор** в java может быть реализован для любого объекта, внутренняя структура которого подразумевает перебор, при этом можно изменить сигнатуру обсуждаемых методов.
Главное при реализации паттерна – логика, которой должен придерживаться класс.
**Интерфейс итератор** – частная реализация одноименного паттерна, применяемая как к готовым структурам (`List, Set, Queue, Map`), так и к прочим, на усмотрение программиста.
Расширяя интерфейс Iterator, ты реализуешь паттерн, но для реализации паттерна не обязательно расширять интерфейс.

Простая аналогия:
	все рыбы плавают, но не всё, что плавает – рыбы.
	
В качестве примера я решил взять… слово.
А конкретнее — существительное.
Оно состоит из частей:

- приставки
- корня
- суффикса
- окончания.

Для частей слова создадим интерфейс `WordPart` и классы, расширяющие его:

- `Prefix`
- `Root`
- `Suffix`
- `Ending`

```java
interface WordPart {
    String getWordPart();
}

static class Root implements WordPart {

    private String part;

    public Root(String part) {
        this.part = part;
    }

    @Override
    public String getWordPart() {
        return part;
    }
}

static class Prefix implements WordPart {

    private String part;

    public Prefix(String part) {
        this.part = part;
    }

    @Override
    public String getWordPart() {
        return part;
    }
}

static class Suffix implements WordPart {

    private String part;

    public Suffix(String part) {
        this.part = part;
    }

    @Override
    public String getWordPart() {
        return part;
    }
}

static class Ending implements WordPart {

    private String part;

    public Ending(String part) {
        this.part = part;
    }

    @Override
    public String getWordPart() {
        return part;
    }
}
```

Тогда класс `Word` (слово) будет содержать в себе части, а кроме них добавим целое число, отражающее количество частей в слове:

```java
public class Word {

    private Root root;
    private Prefix prefix;
    private Suffix suffix;
    private Ending ending;
    private int partCount;

    public Word(Root root, Prefix prefix, Suffix suffix, Ending ending) {
        this.root = root;
        this.prefix = prefix;
        this.suffix = suffix;
        this.ending = ending;
        this.partCount = 4;
    }

    public Word(Root root, Prefix prefix, Suffix suffix) {
        this.root = root;
        this.prefix = prefix;
        this.suffix = suffix;
        this.partCount = 3;

    }

    public Word(Root root, Prefix prefix) {
        this.root = root;
        this.prefix = prefix;
        this.partCount = 2;
    }

    public Word(Root root) {
        this.root = root;
        this.partCount = 1;
    }

    public Root getRoot() {
        return root;
    }

    public Prefix getPrefix() {
        return prefix;
    }

    public Suffix getSuffix() {
        return suffix;
    }

    public Ending getEnding() {
        return ending;
    }

    public int getPartCount() {
        return partCount;
    }

    public boolean hasRoot() {
        return this.root != null;
    }

    public boolean hasPrefix() {
        return this.prefix != null;
    }

    public boolean hasSuffix() {
        return this.suffix != null;
    }

    public boolean hasEnding() {
        return this.ending != null;
    }
```

Окей, у нас есть четыре перегруженных конструктора (для простоты, предположим, что суффикс у нас может быть только один).
Существительное не может состоять из одной приставки, поэтому для конструктора с одним параметром будем устанавливать корень.
Теперь напишем реализацию паттерна итератор:
	WordIterator, переопределяющий 2 метода:
	- `hasNext()`
	- `next()`

```java
public class WordIterator implements Iterator<Word.WordPart> {

    private Word word;
    private int wordPartsCount;

    public WordIterator(Word word) {
        this.word = word;
        this.wordPartsCount = word.getPartCount();
    }

    @Override
    public boolean hasNext() {
        if (wordPartsCount == 4) {
            return word.hasPrefix() || word.hasRoot() || word.hasSuffix() || word.hasEnding();
        } else if (wordPartsCount == 3) {
            return word.hasPrefix() || word.hasRoot() || word.hasSuffix();
        } else if (wordPartsCount == 2) {
            return word.hasPrefix() || word.hasRoot();
        } else if (wordPartsCount == 1) {
            return word.hasRoot();
        }
        return false;
    }

    @Override
    public Word.WordPart next() throws NoSuchElementException {
        if (wordPartsCount <= 0) {
            throw new NoSuchElementException("No more elements in this word!");
        }

        try {
            if (wordPartsCount == 4) {
                return word.getEnding();
            }
            if (wordPartsCount == 3) {
                return word.getSuffix();
            }
            if (wordPartsCount == 2) {
                return word.getPrefix();
            }
            return word.getRoot();
        } finally {
            wordPartsCount--;
        }
    }
}
```

Осталось назначить итератор классу `Word`:

```java
public class Word implements Iterable<Word.WordPart> {
	…
	@Override
	public Iterator<WordPart>iterator() {
    		return new WordIterator(this);
	}
	…
}
```

Теперь проведем морфемный разбор слова «перебежка»:

```java
public class Main {
    public static void main(String[] args) {
        Word.Root root = new Word.Root("беж");
        Word.Prefix prefix = new Word.Prefix("пере");
        Word.Suffix suffix = new Word.Suffix("к");
        Word.Ending ending = new Word.Ending("a");

        Word word = new Word(root, prefix, suffix, ending);

        Iterator wordIterator = word.iterator();
        while (wordIterator.hasNext()) {
            Word.WordPart part = (Word.WordPart) wordIterator.next();
            System.out.println(part.getClass() + ": " + part.getWordPart());
        }
    }
}
```

Обрати внимание, в своей реализации паттерна iterator я выбрал следующий порядок вывода:

1. окончание
2. суффикс
3. приставка
4. корень
33
При проектировании собственного итератора, ты сам можешь задать алгоритм итерации, как захочешь. Успехов в обучении!