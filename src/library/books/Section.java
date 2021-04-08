package library.books;

import java.util.Comparator;
import java.util.TreeSet;

public class Section {
    private final String name;
    private int noBooks;
    private final TreeSet<Book> books = new TreeSet<>(Comparator.comparing(Book::getTitle));

    public Section(String name){
        this.name = name;
        this.noBooks = 0;
    }

    public Section(String name, Book[] books){
        this.name = name;
        for (Book book:
                books) {
            this.addBook(book);
        }
    }

    @Override
    public String toString() {
        return "Section{" +
                "name='" + name + '\'' +
                ", noBooks=" + noBooks +
                '}';
    }

    public String getName() {
        return name;
    }

    public TreeSet<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        if (!this.books.contains(book)) {
            this.noBooks++;
            this.books.add(book);
        }
        else{
            System.out.println("The book already exists in the section " + this.name + ".");
        }
    }

    public void removeBook(Book book) {
        if (this.books.contains(book)){
            this.noBooks--;
            this.books.remove(book);
        }
        else{
            System.out.println("The book " + book.getTitle() + " doesn't exist in the section " + this.name + ".");
        }

    }

    public void listBooks() {
        if (this.books.isEmpty()){
            System.out.println("There are no books in the section " + this.name + " at the moment.");
        } else {
            System.out.println("The books from the publishing house " + this.name + " are the following:\n");
            for (Book book :
                    this.books) {
                System.out.println(book);
            }
        }
    }
}
