package library;

import library.books.*;
import library.people.Author;
import library.people.Reader;

import java.util.*;
import java.util.stream.Collectors;

public class Library {
    private final TreeSet<Book> books = new TreeSet<>(Comparator.comparing(Book::getTitle));
    private final TreeSet<Section> sections = new TreeSet<>((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
    private final TreeSet<Author> authors = new TreeSet<>((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
    private final TreeSet<Reader> readers = new TreeSet<>((r1, r2) -> r1.getName().compareToIgnoreCase(r2.getName()));
    private final TreeSet<PublishingHouse> publishingHouses = new TreeSet<>((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));

    @Override
    public String toString() {
        return "Library{" +
                "books=" + books +
                ", sections=" + sections +
                ", authors=" + authors +
                ", readers=" + readers +
                '}';
    }

    public static void listServices() {
        System.out.println("The present services are as follows:");
        System.out.println("1. Listing the details about the library");
        System.out.println("2. Listing all the books from the library");
        System.out.println("3. Adding a new book into the library");
        System.out.println("4. Removing a book from the library");
        System.out.println("5. Adding copies of a physical book to the library");
        System.out.println("6. Removing copies of a physical book from the library");
        System.out.println("7. Listing all the sections of the library");
        System.out.println("8. Adding a new section to the library");
        System.out.println("9. Removing a section from the library");
        System.out.println("10. Listing all the books from a section");
        System.out.println("11. Adding a book into a section");
        System.out.println("12. Removing a book from a section");
        System.out.println("13. Listing all authors");
        System.out.println("14. Adding an author into the library");
        System.out.println("15. Removing an author from the library");
        System.out.println("16. Listing all the books of an author that are in the library");
        System.out.println("17. Adding a new book from an author");
        System.out.println("18. Listing all the enrolled readers of the library");
        System.out.println("19. Enrolling a new reader into the library");
        System.out.println("20. Removing a reader from the library");
        System.out.println("21. Viewing the books lent by a reader");
        System.out.println("22. Lending a book by a reader");
        System.out.println("23. Returning a book by a reader");
        System.out.println("24. Listing all publishing houses with whom the library is associated");
        System.out.println("25. Associating a new publishing house with the library");
        System.out.println("26. Removing a publishing house");
        System.out.println("27. Listing all the books from a publishing house");
    }

    public Section getSection(String name) throws Exception{
        for (Section section : this.sections){
            if (section.getName().equalsIgnoreCase(name)){
                return section;
            }
        }

        throw new Exception("The name doesn't match with any existing section.");
    }

    public Author getAuthor(String name) throws Exception{
        for (Author author : this.authors){
            if (author.getName().equalsIgnoreCase(name)){
                return author;
            }
        }

        throw new Exception("The name doesn't match with any existing author.");
    }

    public PublishingHouse getPublishingHouse(String name) throws  Exception{
        for (PublishingHouse publishingHouse: this.publishingHouses){
            if (publishingHouse.getName().equalsIgnoreCase(name)){
                return publishingHouse;
            }
        }

        throw new Exception("The name doesn't match with any existing publishing house.");
    }

    public void details(){
        System.out.println("The library currently has:");
        System.out.println("Books: " + this.books.size());
        System.out.println("Sections: " + this.sections.size());
        System.out.println("Authors: " + this.authors.size());
        System.out.println("Readers: " + this.readers.size());
        System.out.println("Publishing houses: " + this.publishingHouses.size());
    }


    // books related actions
    public void listBooks() {
        if (this.books.isEmpty()){
            System.out.println("There are no books in the library at the moment.");
        } else {
            System.out.println("The books of the library:");
            for (Book book :
                    this.books) {
                System.out.println(book);
            }
        }
    }

    public void addBook(Book book) {
        if (this.books.contains(book)) {
            System.out.println("The book is already in the library.");
        } else {
            this.books.add(book);
            this.authors.add(book.getAuthor());
            this.sections.add(book.getSection());
            this.publishingHouses.add(book.getPublishingHouse());
            System.out.println("The book " + book.getTitle() + " was successfully added to the library.");
        }
    }

    public void addBook(String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, int noCopies) {
        Pbook book = new Pbook(title, noPages, publishDate, section, author, publishingHouse, noCopies);
        this.addBook(book);
    }

    public void addBook(String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, String format) {
        Ebook book = new Ebook(title, noPages, publishDate, section, author, publishingHouse, format);
        this.addBook(book);
    }

    public void removeBook(Book book) {
        if (this.books.contains(book)) {
            this.books.remove(book);
            book.getSection().removeBook(book);
            book.getPublishingHouse().removeBook(book);
            System.out.println("The book " + book.getTitle() + " was removed from the library.");
        } else {
            System.out.println("The book doesn't exist in the library.");
        }
    }

    public void removeBook(String title) {
        List<Book> booksToRemove = this.books.stream().filter(b -> title.equals(b.getTitle())).collect(Collectors.toList());
        if (booksToRemove.isEmpty()) {
            System.out.println("The title doesn't match with any books in the library.");
        } else {
            for (Book book : booksToRemove) {
                this.removeBook(book);
            }
        }
    }

    public void addCopies(String title, int noCopies) {
        List<Book> booksToAddCopies = this.books.stream().filter(b -> title.equals(b.getTitle()) && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        if (booksToAddCopies.isEmpty()) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            for (Book book : booksToAddCopies) {
                Pbook pbook = (Pbook) book;
                pbook.addCopy(noCopies);
            }
        }
    }

    public void addCopies(String title) {
        List<Book> booksToAddCopies = this.books.stream().filter(b -> title.equals(b.getTitle()) && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        if (booksToAddCopies.isEmpty()) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            for (Book book : booksToAddCopies) {
                Pbook pbook = (Pbook) book;
                pbook.addCopy();
            }
        }
    }

    public void lostCopy(String title) {
        List<Book> booksToLoseCopy = this.books.stream().filter(b -> title.equals(b.getTitle()) && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        if (booksToLoseCopy.isEmpty()) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            for (Book book : booksToLoseCopy) {
                Pbook pbook = (Pbook) book;
                pbook.lostCopy();
            }
        }
    }

    public void lostCopy(String title, int noCopies) {
        List<Book> booksToLoseCopy = this.books.stream().filter(b -> title.equals(b.getTitle()) && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        if (booksToLoseCopy.isEmpty()) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            for (Book book : booksToLoseCopy) {
                Pbook pbook = (Pbook) book;
                pbook.lostCopy(noCopies);
            }
        }
    }


    // sections related actions
    public void listSections() {
        if (this.sections.isEmpty()){
            System.out.println("There are no sections in the library at the moment.");
        } else {
            System.out.println("The sections of the library:");
            for (Section section :
                    this.sections) {
                System.out.println(section);
            }
        }
    }

    public void addSection(Section section) {
        if (this.sections.contains(section)) {
            System.out.println("The sections already exists.");
        } else {
            this.sections.add(section);
            this.books.addAll(section.getBooks());
            System.out.println("The section " + section.getName() + " was successfully added.");
        }
    }

    public void addSection(String name) {
        Section section = new Section(name);
        this.addSection(section);
    }

    public void addSection(String name, Book[] books) {
        Section section = new Section(name, books);
        this.addSection(section);
    }

    public void removeSection(Section section) {
        if (this.sections.contains(section)) {
            this.sections.remove(section);
            for (Book book : section.getBooks()) {
                this.removeBook(book);
            }
            System.out.println("The section " + section.getName() + " was removed.");
        } else {
            System.out.println("The section doesn't exist in the library");
        }
    }

    public void removeSection(String name) {
        List<Section> sectionsToRemove = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        if (sectionsToRemove.isEmpty()) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            for (Section section : sectionsToRemove) {
                this.removeSection(section);
            }
        }
    }

    public void listBooksFromSection(Section section) {
        if (this.sections.contains(section)) {
            section.listBooks();
        } else {
            System.out.println("The section doesn't exist in the library");
        }
    }

    public void listBooksFromSection(String name) {
        List<Section> sectionsToList = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        if (sectionsToList.isEmpty()) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            for (Section section : sectionsToList) {
                section.listBooks();
            }
        }
    }

    public void addBookToSection(Section section, String title, int noPages, Date publishDate, Author author, PublishingHouse publishingHouse, int noCopies) {
        if (this.sections.contains(section)) {
            Pbook book = new Pbook(title, noPages, publishDate, section, author, publishingHouse, noCopies);
            this.addBook(book);
        } else {
            System.out.println("The section " + section.getName() + " doesn't exist.");
        }
    }

    public void addBookToSection(String name, String title, int noPages, Date publishDate, Author author, PublishingHouse publishingHouse, int noCopies) {
        List<Section> sectionsToAddBook = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        if (sectionsToAddBook.isEmpty()) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            for (Section section : sectionsToAddBook) {
                Pbook book = new Pbook(title, noPages, publishDate, section, author, publishingHouse, noCopies);
                this.addBook(book);
            }
        }
    }

    public void addBookToSection(Section section, String title, int noPages, Date publishDate, Author author, PublishingHouse publishingHouse, String format) {
        if (this.sections.contains(section)) {
            Ebook book = new Ebook(title, noPages, publishDate, section, author, publishingHouse, format);
            this.addBook(book);
        } else {
            System.out.println("The section " + section.getName() + " doesn't exist.");
        }
    }

    public void addBookToSection(String name, String title, int noPages, Date publishDate, Author author, PublishingHouse publishingHouse, String format) {
        List<Section> sectionsToAddBook = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        if (sectionsToAddBook.isEmpty()) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            for (Section section : sectionsToAddBook) {
                Ebook book = new Ebook(title, noPages, publishDate, section, author, publishingHouse, format);
                this.addBook(book);
            }
        }
    }

    public void removeBookFromSection(Section section, String title) {
        if (this.sections.contains(section)) {
            this.removeBook(title);
        } else {
            System.out.println("The section " + section.getName() + " doesn't exist.");
        }
    }

    public void removeBookFromSection(String name, String title) {
        List<Section> sectionsToAddBook = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        if (sectionsToAddBook.isEmpty()) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            this.removeBook(title);
        }
    }

    public void removeBookFromSection(Section section, Book book) {
        if (this.sections.contains(section)) {
            this.removeBook(book);
        } else {
            System.out.println("The section " + section.getName() + " doesn't exist.");
        }
    }

    public void removeBookFromSection(String name, Book book) {
        List<Section> sectionsToAddBook = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        if (sectionsToAddBook.isEmpty()) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            this.removeBook(book);
        }
    }


    // authors related actions
    public void listAllAuthors() {
        if (this.authors.isEmpty()){
            System.out.println("There are no authors in the library at the moment.");
        } else {
            System.out.println("The authors in the library:");
            for (Author author :
                    this.authors) {
                System.out.println(author);
            }
        }
    }

    public void addAuthor(Author author){
        if (this.authors.contains(author)) {
            System.out.println("The author already exists in the library.");
        } else {
            this.authors.add(author);
            this.books.addAll(author.getBooksWritten());
            System.out.println("The author " + author.getName() + " was successfully added.");
        }
    }

    public void addAuthor(String name, java.util.Date birthDate, String email) {
        Author author = new Author(name, birthDate, email);
        this.addAuthor(author);
    }

    public void addAuthor(String name, java.util.Date birthdate, String mail, Book[] books) {
        Author author = new Author(name, birthdate, mail, books);
        this.addAuthor(author);
    }

    public void removeAuthor(Author author) {
        if (this.authors.contains(author)) {
            this.authors.remove(author);
            for (Book book : author.getBooksWritten()) {
                this.books.remove(book);
            }
            System.out.println("The author " + author.getName() + " was successfully removed from the library.");
        } else {
            System.out.println("The author " + author.getName() + " doesn't exist in the library.");
        }
    }

    public void removeAuthor(String name) {
        List<Author> authorsToRemove = this.authors.stream().filter(a -> name.equalsIgnoreCase(a.getName())).collect(Collectors.toList());
        if (authorsToRemove.isEmpty()) {
            System.out.println("The name doesn't match with any author in the library.");
        } else {
            for (Author author : authorsToRemove) {
                this.removeAuthor(author);
            }
        }
    }

    public void listAllBooksFromAuthor(Author author) {
        if (this.authors.contains(author)) {
            List<Book> booksOfAuthor = this.books.stream().filter(b -> b.getAuthor().equals(author)).collect(Collectors.toList());
            if (booksOfAuthor.isEmpty()) {
                System.out.println("The author " + author.getName() + " presently has no books in the library.");
            } else {
                System.out.println("The author " + author.getName() + " has the following books in the library:");
                for (Book book : booksOfAuthor) {
                    System.out.println(book);
                }
            }
        } else {
            System.out.println("The author " + author.getName() + " doesn't exist in the library.");
        }
    }

    public void listAllBooksFromAuthor(String name){
        List<Author> authorsToList = this.authors.stream().filter(a -> name.equalsIgnoreCase(a.getName())).collect(Collectors.toList());
        if (authorsToList.isEmpty()) {
            System.out.println("The name doesn't match with any author in the library.");
        } else {
            for (Author author : authorsToList) {
                this.listAllBooksFromAuthor(author);
            }
        }
    }

    public void addBookFromAuthor(Author author, String title, int noPages, Section section, PublishingHouse publishingHouse){
        if (this.authors.contains(author)){
            Pbook pbook = author.publish(title, noPages, section, publishingHouse);
            this.addBook(pbook);
        } else {
            System.out.println("The author " + author.getName() + " doesn't exist in the library.");
        }
    }

    public void addBookFromAuthor(String name, String title, int noPages, Section section, PublishingHouse publishingHouse){
        List<Author> authorsToAddBook = this.authors.stream().filter(a -> a.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!authorsToAddBook.isEmpty()){
            for (Author author : authorsToAddBook){
                this.addBookFromAuthor(author, title, noPages, section, publishingHouse);
            }
        } else {
            System.out.println("The name doesn't match with any author in the library.");
        }
    }

    public void addBookFromAuthor(Author author, String title, int noPages, Section section, PublishingHouse publishingHouse, String format){
        if (this.authors.contains(author)){
            Ebook ebook = author.publish(title, noPages, section, publishingHouse, format);
            this.addBook(ebook);
        } else {
            System.out.println("The author " + author.getName() + " doesn't exist in the library.");
        }
    }

    public void addBookFromAuthor(String name, String title, int noPages, Section section, PublishingHouse publishingHouse, String format){
        List<Author> authorsToAddBook = this.authors.stream().filter(a -> a.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!authorsToAddBook.isEmpty()){
            for (Author author : authorsToAddBook){
                this.addBookFromAuthor(author, title, noPages, section, publishingHouse, format);
            }
        } else {
            System.out.println("The name doesn't match with any author in the library.");
        }
    }


    // readers related actions
    public void listAllReaders(){
        if (this.readers.isEmpty()){
            System.out.println("There are no readers enrolled into the library at the moment.");
        } else {
            System.out.println("The readers enrolled into the library:");
            for (Reader reader : this.readers) {
                System.out.println(reader);
            }
        }
    }

    public void addReader(Reader reader){
        if (this.readers.contains(reader)) {
            System.out.println("The reader already exists in the library.");
        } else {
            this.readers.add(reader);
            System.out.println("The reader " + reader.getName() + " was successfully enrolled.");
        }
    }

    public void addReader(String name, java.util.Date birthDate, String email, String address){
        Reader reader = new Reader(name, birthDate, email, address);
        this.addReader(reader);
    }

    public void removeReader(Reader reader){
        if (this.readers.contains(reader)){
            this.readers.remove(reader);
            System.out.println("The reader was successfully removed from the library.");
        } else {
            System.out.println("The reader " + reader.getName() + " doesn't exist.");
        }
    }

    public void removeReader(String name){
        List<Reader> readersToRemove = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!readersToRemove.isEmpty()){
            for (Reader reader : readersToRemove){
                this.removeReader(reader);
            }
        } else {
            System.out.println("The name doesn't match with any enrolled reader.");
        }
    }

    public void listBooksLent(Reader reader){
        if (this.readers.contains(reader)){
            if (reader.getBooksLent().isEmpty()){
                System.out.println("The reader " + reader.getName() + " hasn't lent any book yet.");
            } else {
                System.out.println("The reader " + reader.getName() + " has lent the following books:");
                ArrayList<Book> booksLent = reader.getBooksLent();
                for (Book book : booksLent) {
                    System.out.println(book);
                }
            }
        } else {
            System.out.println("The reader " + reader.getName() + " doesn't exist.");
        }
    }

    public void listBooksLent(String name){
        List<Reader> readersToView = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!readersToView.isEmpty()){
            for (Reader reader : readersToView){
                this.listBooksLent(reader);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }

    public void lendBook(Reader reader, Book book){
        if (this.readers.contains(reader)){
            if (this.books.contains(book)) {
                reader.lendBook(book);
                System.out.println("The reader " + reader.getName() + " lent a book.");
            } else {
                System.out.println("The book doesn't exist in the library.");
            }
        } else {
            System.out.println("The reader " + reader.getName() + " isn't enrolled.");
        }
    }

    public void lendBook(String name, Book book){
        List<Reader> readersToLendBook = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!readersToLendBook.isEmpty()){
            for (Reader reader : readersToLendBook){
                this.lendBook(reader, book);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }

    public void lendBook(Reader reader, String title){
        List<Book> booksToLend = this.books.stream().filter(b -> b.getTitle().equals(title)).collect(Collectors.toList());
        if (!booksToLend.isEmpty()){
            for (Book book : booksToLend) {
                this.lendBook(reader, book);
            }
        } else {
            System.out.println("The title doesn't match with any book from the library.");
        }
    }

    public void lendBook(String name, String title){
        List<Reader> readersToLendBook = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!readersToLendBook.isEmpty()){
            for (Reader reader : readersToLendBook){
                this.lendBook(reader, title);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }

    public void returnBook(Reader reader, Book book){
        if (this.readers.contains(reader)){
            if (this.books.contains(book)) {
                reader.returnBook(book);
                System.out.println("The reader " + reader.getName() + " returned a book.");
            } else {
                System.out.println("The book doesn't exist in the library.");
            }
        } else {
            System.out.println("The reader " + reader.getName() + " isn't enrolled.");
        }
    }

    public void returnBook(String name, Book book){
        List<Reader> readersToReturnBook = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!readersToReturnBook.isEmpty()){
            for (Reader reader : readersToReturnBook){
                this.returnBook(reader, book);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }

    public void returnBook(Reader reader, String title){
        List<Book> booksToReturn = this.books.stream().filter(b -> b.getTitle().equals(title)).collect(Collectors.toList());
        if (!booksToReturn.isEmpty()){
            for (Book book : booksToReturn) {
                this.returnBook(reader, book);
            }
        } else {
            System.out.println("The title doesn't match with any book from the library.");
        }
    }

    public void returnBook(String title, String name){
        List<Reader> readersToReturnBook = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!readersToReturnBook.isEmpty()){
            for (Reader reader : readersToReturnBook){
                this.returnBook(reader, title);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }


    // publishing houses related actions
    public void listAllPublishingHouses(){
        if (this.publishingHouses.isEmpty()){
            System.out.println("There are no publiching houses assosiated with the library at the moment.");
        } else {
            System.out.println("The publishing houses associated with the library are:");
            for (PublishingHouse publishingHouse : this.publishingHouses) {
                System.out.println(publishingHouse);
            }
        }
    }

    public void addPublishingHouse(PublishingHouse publishingHouse){
        if (this.publishingHouses.contains(publishingHouse)) {
            System.out.println("The publishing house is already associated with the library.");
        } else {
            this.publishingHouses.add(publishingHouse);
            System.out.println("The publishing house " + publishingHouse.getName() + " was successfully associated.");
        }
    }

    public void addPublishingHouse(String name, Date establishmentDate){
        PublishingHouse publishingHouse = new PublishingHouse(name, establishmentDate);
        this.addPublishingHouse(publishingHouse);
    }

    public void removePublishingHouse(PublishingHouse publishingHouse){
        if (this.publishingHouses.contains(publishingHouse)){
            this.publishingHouses.remove(publishingHouse);
            System.out.println("The publishing house was successfully removed from the library.");
        } else {
            System.out.println("The publishing house " + publishingHouse.getName() + " doesn't exist.");
        }
    }

    public void removePublishingHouse(String name){
        List<PublishingHouse> publishingHousesToRemove = this.publishingHouses.stream().filter(ph -> ph.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!publishingHousesToRemove.isEmpty()){
            for (PublishingHouse publishingHouse : publishingHousesToRemove){
                this.removePublishingHouse(publishingHouse);
            }
        } else {
            System.out.println("The name doesn't match with any associated publishing houses.");
        }
    }

    public void listBooksFromPublishingHouse(PublishingHouse publishingHouse){
        if (this.publishingHouses.contains(publishingHouse)) {
            publishingHouse.listBooks();
        } else {
            System.out.println("The publishing house isn't associated with the library");
        }
    }

    public void listBooksFromPublishingHouse(String name) {
        List<PublishingHouse> publishingHousesToList = this.publishingHouses.stream().filter(ph -> name.equalsIgnoreCase(ph.getName())).collect(Collectors.toList());
        if (publishingHousesToList.isEmpty()) {
            System.out.println("The name doesn't match with any publishing house.");
        } else {
            for (PublishingHouse publishingHouse : publishingHousesToList) {
                publishingHouse.listBooks();
            }
        }
    }
}
