package library.people;

import library.books.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Author extends Person {
    private int noBooksWritten;
    private final ArrayList<Book> booksWritten = new ArrayList<>();

    public Author(String name, Date birthdate, String email) {
        super(name, birthdate, email);
    }

    public Author(String name, Date birthdate, String mail, Book[] books) {
        this(name, birthdate, mail);
        Collections.addAll(this.booksWritten, books);
        this.noBooksWritten = books.length;
    }

    @Override
    public String toString() {
        return "Author{" +
                "noBooksWritten=" + noBooksWritten +
                ", booksWritten=" + booksWritten +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", mail='" + mail + '\'' +
                '}';
    }

    public ArrayList<Book> getBooksWritten() {
        return booksWritten;
    }

    public Pbook publish(String title, int noPages, Section section, PublishingHouse publishingHouse) {
        Pbook pbook = new Pbook(title, noPages, (java.sql.Date) new Date(), section, this, publishingHouse, 10);
        this.booksWritten.add(pbook);
        this.noBooksWritten++;
        return pbook;
    }

    public Ebook publish(String title, int noPages, Section section, PublishingHouse publishingHouse, String format) {
        Ebook ebook = new Ebook(title, noPages, (java.sql.Date) new Date(),  section, this, publishingHouse, format);
        this.booksWritten.add(ebook);
        this.noBooksWritten++;
        return ebook;
    }

    public void listBooks() {
        System.out.println("The author " + this.name + " has published the following books:");
        for (Book book :
                this.booksWritten) {
            System.out.println(book);
        }
    }
}
