package library.books;

import library.database.DB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PublishingHouse {
    static int noPublishingHouses;
    private int id;
    private final String name;
    private final Date establishmentDate;
    private int noBooks;
//    private final ArrayList<Book> books = new ArrayList<>();
    private DB db = DB.getInstance();

    public PublishingHouse(String name, Date establishmentDate) {
        this.id = ++noPublishingHouses;
        this.name = name;
        this.establishmentDate = establishmentDate;
    }

    public PublishingHouse(int id, String name, Date establishmentDate, int noBooks) {
        this.id = id;
        this.name = name;
        this.establishmentDate = establishmentDate;
        this.noBooks = noBooks;
    }

    @Override
    public String toString() {
        return "PublishingHouse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", establishmentDate=" + establishmentDate +
                ", noBooks=" + noBooks +
//                ", books=" + books +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublishingHouse that = (PublishingHouse) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(establishmentDate, that.establishmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, establishmentDate);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getEstablishmentDate() {
        return establishmentDate;
    }

    public int getNoBooks() {
        return noBooks;
    }

    public static void setNoPublishingHouses(int noPublishingHouses) {
        PublishingHouse.noPublishingHouses = noPublishingHouses;
    }

    public void addBook(Book book) {
//        if (!this.books.contains(book)) {
//            this.books.add(book);
            this.noBooks++;
            db.updatePublishingHouse(this);
//        }
//        else{
//            System.out.println("The book already exists from the publishing house " + this.name + ".");
//        }
    }

    public void removeBook(Book book) {
//        if (this.books.contains(book)){
//            this.books.remove(book);
            this.noBooks--;
            db.updatePublishingHouse(this);
//        }
//        else{
//            System.out.println("The book " + book.getTitle() + " doesn't exist from the publishing house " + this.name + ".");
//        }
    }

    public void listBooks(){
        List<Book> books = db.getBooksFromPublishingHouse(this.id);
        if (books.isEmpty()){
            System.out.println("The publishing house " + this.name + " hasn't published any book yet.");
        } else {
            System.out.println("The publishing house " + this.name + " has published the following books:\n");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }
}
