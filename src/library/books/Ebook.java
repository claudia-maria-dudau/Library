package library.books;

import library.people.Author;

import java.util.Date;

public class Ebook  extends Book{
    public Ebook(String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, String format) {
        super(title, noPages, publishDate, section, author, publishingHouse);
        this.format = format;
        this.type = "ebook";
        this.noCopies = Double.POSITIVE_INFINITY;
    }

    public Ebook(int id, String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, String format) {
        super(id, title, noPages, publishDate, section, author, publishingHouse);
        this.format = format;
        this.type = "ebook";
        this.noCopies = Double.POSITIVE_INFINITY;
    }

    @Override
    public void lendBook() {
        System.out.println("The ebook " + this.title + "was lent.");
    }

    @Override
    public void returnBook() {
        System.out.println("An ebook can't be returned.");
    }
}
