package library.books;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class PublishingHouse {
    private final String name;
    private final Date establishmentDate;
    private final ArrayList<Book> books = new ArrayList<>();

    public PublishingHouse(String name, Date establishmentDate) {
        this.name = name;
        this.establishmentDate = establishmentDate;
    }

    @Override
    public String toString() {
        return "PublishingHouse{" +
                "name='" + name + '\'' +
                ", establishmentDate=" + establishmentDate +
                ", books=" + books +
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

    public String getName() {
        return name;
    }

    public void addBook(Book book) {
        if (!this.books.contains(book)) {
            this.books.add(book);
        }
        else{
            System.out.println("The book already exists from the publishing house " + this.name + ".");
        }
    }

    public void removeBook(Book book) {
        if (this.books.contains(book)){
            this.books.remove(book);
        }
        else{
            System.out.println("The book " + book.getTitle() + " doesn't exist from the publishing house " + this.name + ".");
        }
    }

    public void listBooks(){
        if (this.books.isEmpty()){
            System.out.println("The publishing house " + this.name + " hasn't published any book yet.");
        } else {
            System.out.println("The publishing house " + this.name + " has published the following books:\n");
            for (Book book :
                    this.books) {
                System.out.println(book);
            }
        }
    }
}
