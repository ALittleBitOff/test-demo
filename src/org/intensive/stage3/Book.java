package org.intensive.stage3;

public class Book {

    private String title;
    private String author;
    private Integer pages;
    private Integer year;

    public Book(String title, String author, Integer pages, Integer year) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getPages() {
        return pages;
    }

    public Integer getYear() {
        return year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pages=" + pages +
                ", year=" + year +
                '}';
    }
}

