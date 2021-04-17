package library.books;

import library.people.Author;

import java.util.Date;
import java.util.Objects;

public abstract class Book {
    static int noBooks;
    protected int id;
    protected String title;
    protected String type;
    protected int noPages;
    protected double noCopies;
    protected Date publishDate;
    protected Section section;
    protected Author author;
    protected PublishingHouse publishingHouse;

    public abstract void lendBook();
    public abstract void returnBook();

    public Book(String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse){
        this.id = ++noBooks;
        this.title = title;
        this.noPages = noPages;
        this.publishDate = publishDate;
        this.section = section;
        this.author = author;
        this.publishingHouse = publishingHouse;

        this.section.addBook(this);
        this.publishingHouse.addBook(this);
        this.author.addBook(this);
    }

    public void setNoCopies(double noCopies) {
        this.noCopies = noCopies;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", noPages=" + noPages +
                ", noCopies=" + noCopies +
                ", publishDate=" + publishDate +
                ", section=" + section.getName() +
                ", author=" + author.getName() +
                ", publishingHouse='" + publishingHouse.getName() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return getTitle().equalsIgnoreCase(book.getTitle()) &&
                type.equals(book.type) &&
                author.equals(book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), type, author);
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public Section getSection() {
        return section;
    }

    public PublishingHouse getPublishingHouse() {
        return publishingHouse;
    }

    public double getNoCopies() {
        return noCopies;
    }

    public boolean isAvailable(){
        return this.noCopies > 0;
    }
}
