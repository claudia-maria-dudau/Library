package library.books;

import library.database.DB;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Section {
    static int noSections;
    private int id;
    private final String name;
    private int noBooks;
    // private final TreeSet<Book> books = new TreeSet<>(Comparator.comparing(Book::getTitle));
    private DB db = DB.getInstance();

    public Section(String name){
        this.id = ++noSections;
        this.name = name;
        this.noBooks = 0;
    }

    public Section(String name, Book[] books){
        this.id = ++noSections;
        this.name = name;
        for (Book book:
                books) {
            this.addBook(book);
        }
    }

    public Section(int id, String name, int noBooks){
        this.id = id;
        this.name = name;
        this.noBooks = noBooks;
    }


    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", noBooks=" + noBooks +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNoBooks() {
        return noBooks;
    }

//    public TreeSet<Book> getBooks() {
//        return books;
//    }

    public static void setNoSections(int noSections) {
        Section.noSections = noSections;
    }

    public void addBook(Book book) {
//        if (!this.books.contains(book)) {
            this.noBooks++;
//            this.books.add(book);
            db.updateSection(this);
//        }
//        else{
//            System.out.println("The book already exists in the section " + this.name + ".");
//        }
    }

    public void removeBook(Book book) {
//        if (this.books.contains(book)){
            this.noBooks--;
//            this.books.remove(book);
            db.updateSection(this);
//        }
//        else{
//            System.out.println("The book " + book.getTitle() + " doesn't exist in the section " + this.name + ".");
//        }
    }

    public void listBooks() {
        List<Book> books = db.getBooksFromSection(this.id);
        if (books.isEmpty()){
            System.out.println("There are no books in the section " + this.name + " at the moment.");
        } else {
            System.out.println("The books from the publishing house " + this.name + " are the following:\n");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }
}
