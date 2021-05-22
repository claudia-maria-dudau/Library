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

    public Author(int id, String name, Date birthdate, String email, int noBooksWritten) {
        super(id, name, birthdate, email);
        this.noBooksWritten = noBooksWritten;
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

    public int getNoBooksWritten() {
        return noBooksWritten;
    }

    public ArrayList<Book> getBooksWritten() {
        return booksWritten;
    }

    public Pbook publish(String title, int noPages, Section section, PublishingHouse publishingHouse) {
        return new Pbook(title, noPages, new Date(), section, this, publishingHouse, 10);
    }

    public Ebook publish(String title, int noPages, Section section, PublishingHouse publishingHouse, String format) {
        return new Ebook(title, noPages, new Date(),  section, this, publishingHouse, format);
    }

    public void addBook(Book book){
        this.booksWritten.add(book);
        this.noBooksWritten++;
    }

    public void removeBook(Book book){
        if (this.booksWritten.contains(book)){
            this.booksWritten.get(this.booksWritten.indexOf(book)).setNoCopies(0);
        }
        else{
            System.out.println("The book " + book.getTitle() + " doesn't exist from the author " + this.name + ".");
        }
    }

    public void listBooks() {
        if (this.booksWritten.isEmpty()){
            System.out.println("The author " + this.name + " hasn't published any book yet.");
        } else {
            System.out.println("The author " + this.name + " has published the following books:");
            for (Book book :
                    this.booksWritten) {
                System.out.println(book);
            }
        }
    }
}
