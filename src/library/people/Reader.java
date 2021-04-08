package library.people;

import library.books.Book;

import java.util.ArrayList;
import java.util.Date;

public class Reader extends Person {
    private final String address;
    private int noBooksLent;
    private final ArrayList<Book> booksLent = new ArrayList<>();

    public Reader(String name, Date birthDate, String mail, String address) {
        super(name, birthDate, mail);
        this.address = address;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "address='" + address + '\'' +
                ", noBooksLent=" + noBooksLent +
                ", booksLent=" + booksLent +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", mail='" + mail + '\'' +
                '}';
    }

    public ArrayList<Book> getBooksLent() {
        return booksLent;
    }

    public void lendBook(Book book) {
        if (book.isAvailable() && this.noBooksLent < 5) {
            System.out.println("Reader " + this.name + "lent a book.");
            book.lendBook();
            this.booksLent.add(book);

            if (book.getNoCopies() != Double.POSITIVE_INFINITY){
                this.noBooksLent++;
            }
        } else if (!book.isAvailable()) {
            book.lendBook();
        } else {
            System.out.println("Reader " + this.name + "can't lend any more books until he returns at least one book back.");
        }
    }

    public void returnBook(Book book){
        book.returnBook();
        this.noBooksLent--;
        this.booksLent.remove(book);
    }
}
