package org.intensive.stage3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MainStudent {
    public static void main(String[] args) {
        List<Book> books1 = Arrays.asList(
                new Book("book 1", "author 1", 1225, 1864),
                new Book("book 2", "author 2", 672, 672),
                new Book("book 3", "author 3", 384, 1967),
                new Book("book 4", "author 4", 448, 2012),
                new Book("book 5", "author 5", 512, 2015)
        );
        List<Book> books2 = Arrays.asList(
                new Book("book 6", "author 6", 448, 2012),
                new Book("book 7", "author 7", 512, 2015),
                new Book("book 8", "author 8", 328, 1949),
                new Book("book 9", "author 9", 256, 1953),
                new Book("book 10", "author 10", 96, 1943)
        );

        Student student1 = new Student("Петр", books1);
        Student student2 = new Student("Александр", books2);

        List<Student> students = List.of(student1, student2);

        students.stream()
                .peek(System.out::println) // Выводим студентов
                .flatMap(student -> student.getBooks().stream())
                .peek(System.out::println) // Выводим книги студентов
                .sorted(Comparator.comparingInt(Book::getPages)) // Сортируем по количеству страниц
                .peek(System.out::println)
                .distinct() // Убираем дубликаты
                .peek(System.out::println)
                .filter(book -> book.getYear() > 2000) // Фильтруем книги после 2000 года
                .peek(System.out::println)
                .limit(3) // Ограничиваем до 3 книг
                .peek(System.out::println)
                .map(Book::getYear) // Получаем годы выпуска
                .peek(System.out::println)
                .findFirst()
                .ifPresentOrElse(
                        year -> System.out.println("Год выпуска книги: " + year),
                        () -> System.out.println("Книга не найдена")
                );

    }
}
