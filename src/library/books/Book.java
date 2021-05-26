package library.books;

import library.database.DB;
import library.people.Author;

import java.util.Date;
import java.util.Objects;

public abstract class Book {
    static int noBooks;
    protected int id;
    protected String title;
    protected String type;
    protected int noPages;
    protected int noCopies;
    protected Date publishDate;
    protected String format;
    protected Section section;
    protected Author author;
    protected PublishingHouse publishingHouse;
    protected DB db = DB.getInstance();

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

        this.getAuthor().addBook(this);
        this.getSection().addBook(this);
        this.getPublishingHouse().addBook(this);
    }

    public Book(int id, String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse){
        this.id = id;
        this.title = title;
        this.noPages = noPages;
        this.publishDate = publishDate;
        this.section = section;
        this.author = author;
        this.publishingHouse = publishingHouse;
    }

    public void setNoCopies(int noCopies) {
        this.noCopies = noCopies;
        db.updateBook(this);
    }

    public static void setNoBooks(int noBooks) {
        Book.noBooks = noBooks;
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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public int getNoPages() {
        return noPages;
    }

    public int getNoCopies() {
        return noCopies;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getFormat() {
        return format;
    }

    public Section getSection() {
        return section;
    }

    public Author getAuthor() {
        return author;
    }

    public PublishingHouse getPublishingHouse() {
        return publishingHouse;
    }

    public boolean isAvailable(){
        return this.noCopies > 0;
    }
}
