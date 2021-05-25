package library.people;

import library.database.DB;
import library.books.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reader extends Person {
    private final String address;
    private int noBooksLent;
    private final ArrayList<Book> booksLent = new ArrayList<>();
    private DB db = DB.getInstance();

    public Reader(String name, Date birthDate, String mail, String address) {
        super(name, birthDate, mail);
        this.address = address;
    }

    public Reader(int id, String name, Date birthDate, String mail, String address, int noBooksLent) {
        super(id, name, birthDate, mail);
        this.address = address;
        this.noBooksLent = noBooksLent;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "address='" + address + '\'' +
                ", noBooksLent=" + noBooksLent +
                ", booksLent=" + booksLent +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", mail='" + mail + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public int getNoBooksLent() {
        return noBooksLent;
    }

    public ArrayList<Book> getBooksLent() {
        return booksLent;
    }

    public void lendBook(Book book) {
        if (book.isAvailable() && this.noBooksLent < 5) {
            db.createLent(book.getId(), this.id, (java.sql.Date) new Date());
            System.out.println("Reader " + this.name + "lent a book.");
            book.lendBook();
            this.booksLent.add(book);

            if (book.getNoCopies() != Double.POSITIVE_INFINITY){
                this.noBooksLent++;
                db.updateReader(this);
            }
        } else if (!book.isAvailable()) {
            System.out.println("The book " + book.getTitle() + " is not available at the moment.");
        } else {
            System.out.println("Reader " + this.name + "can't lend any more books until he returns at least one book back.");
        }
    }

    public void returnBook(Book book){
        book.returnBook();
        this.noBooksLent--;
        this.booksLent.remove(book);
        db.updateReader(this);
        db.updateLent(book.getId(), this.id, true);
    }

    public void listBooksLent() {
        List<Book> booksLent = db.getBooksLentByReader(this.id);
        if (booksLent.isEmpty()){
            System.out.println("The reader " + this.name + " hasn't lent any book yet.");
        } else {
            System.out.println("The reader " + this.name + " has lent the following books:");
            for (Book book : booksLent) {
                System.out.println(book);
            }
        }
    }
}
