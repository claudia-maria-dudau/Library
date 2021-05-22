package library.books;

import library.people.Author;

import java.util.Date;

public class Pbook extends Book {
    public Pbook(String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, int noCopies) {
        super(title, noPages, publishDate, section, author, publishingHouse);
        this.type = "pbook";
        this.noCopies = noCopies;
        this.format = "physical";
    }

    public Pbook(int id, String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, int noCopies) {
        super(id, title, noPages, publishDate, section, author, publishingHouse);
        this.type = "pbook";
        this.noCopies = noCopies;
        this.format = "physical";
    }

    @Override
    public void lendBook() {
        if (this.isAvailable()) {
            System.out.println("The book " + this.title + " was lent.");
            this.noCopies--;
        } else {
            System.out.println("The book is not available at the moment. Please come back later.");
        }
    }

    @Override
    public void returnBook() {
        System.out.println("The book " + this.title + " was returned.");
        this.noCopies++;
    }

    public void addCopy() {
        this.noCopies++;
    }

    public void addCopy(int nr) {
        this.noCopies += nr;
    }

    public void lostCopy() {
        this.noCopies--;
    }

    public void lostCopy(int nr){
        this.noCopies -=  nr;
    }

}
