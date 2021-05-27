package library;

import library.books.*;
import library.database.DB;
import library.people.Author;
import library.people.Person;
import library.people.Reader;

import java.util.*;
import java.util.stream.Collectors;

public class Library {
    private static Library instance;
    private final TreeSet<Book> books = new TreeSet<>((b1, b2) -> (b1.getTitle().compareToIgnoreCase(b2.getTitle()) +
            b1.getAuthor().getName().compareToIgnoreCase(b2.getAuthor().getName()) +
            b1.getPublishDate().toString().compareToIgnoreCase(b2.getPublishDate().toString()) +
            b1.getPublishingHouse().getName().compareToIgnoreCase(b2.getPublishingHouse().getName()) +
            b1.getFormat().compareToIgnoreCase(b2.getFormat())));
    private final TreeSet<Section> sections = new TreeSet<>((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
    private final TreeSet<Author> authors = new TreeSet<>((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
    private final TreeSet<Reader> readers = new TreeSet<>((r1, r2) -> r1.getName().compareToIgnoreCase(r2.getName()));
    private final TreeSet<PublishingHouse> publishingHouses = new TreeSet<>((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
    private final DB db = DB.getInstance();

    private Library() {
        this.books.addAll(db.getBooks());
        this.sections.addAll(db.getSections());
        this.authors.addAll(db.getAuthors());
        this.readers.addAll(db.getReaders());
        this.publishingHouses.addAll(db.getPublishingHouses());
    }

    public static Library getInstance() {
        if (instance == null) {
            synchronized (Library.class) {
                if (instance == null) {
                    instance = new Library();
                }
            }
        }

        return instance;
    }

    public TreeSet<Book> getBooks() {
        return books;
    }

    public TreeSet<Section> getSections() {
        return sections;
    }

    public TreeSet<Author> getAuthors() {
        return authors;
    }

    public TreeSet<Reader> getReaders() {
        return readers;
    }

    public TreeSet<PublishingHouse> getPublishingHouses() {
        return publishingHouses;
    }

    @Override
    synchronized public String toString() {
        return "Library{" +
                "books=" + books +
                ", sections=" + sections +
                ", authors=" + authors +
                ", readers=" + readers +
                '}';
    }

    synchronized public static void listServices() {
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

    synchronized public void details() {
        System.out.println("The library currently has:");
        System.out.println("Books: " + this.books.size());
        System.out.println("Sections: " + this.sections.size());
        System.out.println("Authors: " + this.authors.size());
        System.out.println("Readers: " + this.readers.size());
        System.out.println("Publishing houses: " + this.publishingHouses.size());
    }


    // books related actions
    synchronized public void listBooks() {
        List<Book> books = db.getBooks();
        if (this.books.isEmpty()) {
            System.out.println("There are no books in the library at the moment.");
        } else {
            System.out.println("The books of the library:");
            for (Book book : this.books) {
                System.out.println(book);
            }
        }
    }

    synchronized public void addBook(Book book) {
        Book bookDB = db.getBook(book.getId());
        if (this.books.contains(book) || bookDB != null) {
            System.out.println("The book is already in the library.");
        } else {
            db.createBook(book);
            this.books.add(book);
            System.out.println("The book " + book.getTitle() + " was successfully added to the library.");
        }
    }

    synchronized public void addBook(String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, int noCopies) {
        Pbook book = new Pbook(title, noPages, publishDate, section, author, publishingHouse, noCopies);
        this.addBook(book);
    }

    synchronized public void addBook(String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, String format) {
        Ebook book = new Ebook(title, noPages, publishDate, section, author, publishingHouse, format);
        this.addBook(book);
    }

    synchronized public void removeBook(Book book) {
        Book bookDB = db.getBook(book.getId());
        if (this.books.contains(book) || bookDB != null) {
            db.deleteBook(book.getId());
            this.books.remove(book);
            book.getSection().removeBook(book);
            book.getPublishingHouse().removeBook(book);
            book.getAuthor().removeBook(book);
            System.out.println("The book " + book.getTitle() + " was removed from the library.");
        } else {
            System.out.println("The book doesn't exist in the library.");
        }
    }

    synchronized public void removeBook(String title) {
        List<Book> booksToRemove = this.books.stream().filter(b -> title.equals(b.getTitle())).collect(Collectors.toList());
        List<Book> bookDB = db.getBook(title);
        if (booksToRemove.isEmpty() && (bookDB == null || bookDB.isEmpty())) {
            System.out.println("The title doesn't match with any books in the library.");
        } else {
            for (Book book : booksToRemove) {
                this.removeBook(book);
            }
        }
    }

    synchronized public void addCopies(Book book, int noCopies) {
        List<Book> booksToAddCopies = this.books.stream().filter(b -> book.getId() == b.getId() && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        Book bookDB = db.getBook(book.getId());
        if (booksToAddCopies.isEmpty() && bookDB == null) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            Pbook pbook = (Pbook) book;
            pbook.addCopy(noCopies);
        }
    }

    synchronized public void addCopies(String title, int noCopies) {
        List<Book> booksToAddCopies = this.books.stream().filter(b -> title.equals(b.getTitle()) && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        List<Book> bookDB = db.getBook(title);
        if (booksToAddCopies.isEmpty() && (bookDB == null || bookDB.isEmpty())) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            for (Book book : booksToAddCopies) {
                Pbook pbook = (Pbook) book;
                pbook.addCopy(noCopies);
            }
        }
    }

    synchronized public void addCopies(String title) {
        List<Book> booksToAddCopies = this.books.stream().filter(b -> title.equals(b.getTitle()) && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        List<Book> bookDB = db.getBook(title);
        if (booksToAddCopies.isEmpty() && (bookDB == null || bookDB.isEmpty())) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            for (Book book : booksToAddCopies) {
                Pbook pbook = (Pbook) book;
                pbook.addCopy();
            }
        }
    }

    synchronized public void lostCopy(Book book, int noCopies) {
        List<Book> booksToAddCopies = this.books.stream().filter(b -> book.getId() == b.getId() && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        Book bookDB = db.getBook(book.getId());
        if (booksToAddCopies.isEmpty() && bookDB == null) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            Pbook pbook = (Pbook) book;
            pbook.lostCopy(noCopies);
        }
    }

    synchronized public void lostCopy(String title) {
        List<Book> booksToLoseCopy = this.books.stream().filter(b -> title.equals(b.getTitle()) && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        List<Book> bookDB = db.getBook(title);
        if (booksToLoseCopy.isEmpty() && (bookDB == null || bookDB.isEmpty())) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            for (Book book : booksToLoseCopy) {
                Pbook pbook = (Pbook) book;
                pbook.lostCopy();
            }
        }
    }

    synchronized public void lostCopy(String title, int noCopies) {
        List<Book> booksToLoseCopy = this.books.stream().filter(b -> title.equals(b.getTitle()) && b.getClass().equals(Pbook.class)).collect(Collectors.toList());
        List<Book> bookDB = db.getBook(title);
        if (booksToLoseCopy.isEmpty() && (bookDB == null || bookDB.isEmpty())) {
            System.out.println("The title doesn't match with any physical books in the library.");
        } else {
            for (Book book : booksToLoseCopy) {
                Pbook pbook = (Pbook) book;
                pbook.lostCopy(noCopies);
            }
        }
    }

    synchronized public void updateBook(Book book) {
        Book bookDB = db.getBook(book.getId());
        if (!this.books.contains(book) && bookDB == null) {
            System.out.println("The book doesn't exist in the library.");
        } else {
            db.updateBook(book);
            List<Book> booksToUpdate = this.books.stream().filter(b -> b.getId() == book.getId()).collect(Collectors.toList());
            for (Book book1 : booksToUpdate) {
                this.removeBook(book1);
            }
            this.addBook(book);
            System.out.println("The book " + book.getTitle() + " was successfully modified.");
        }
    }

    synchronized public void updateBook(int id, String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, int noCopies) {
        Pbook book = new Pbook(id, title, noPages, publishDate, section, author, publishingHouse, noCopies);
        this.updateBook(book);
    }

    synchronized public void updateBook(int id, String title, int noPages, Date publishDate, Section section, Author author, PublishingHouse publishingHouse, String format) {
        Ebook book = new Ebook(id, title, noPages, publishDate, section, author, publishingHouse, format);
        this.updateBook(book);
    }


    // sections related actions
    synchronized public void listSections() {
        List<Section> sections = db.getSections();
        if (this.sections.isEmpty()) {
            System.out.println("There are no sections in the library at the moment.");
        } else {
            System.out.println("The sections of the library:");
            for (Section section : this.sections) {
                System.out.println(section);
            }
        }
    }

    synchronized public void addSection(Section section) {
        Section sectionDB = db.getSection(section.getId());
        if (this.sections.contains(section) || sectionDB != null) {
            System.out.println("The section already exists.");
        } else {
            db.createSection(section);
            this.sections.add(section);
//            this.books.addAll(section.getBooks());
            System.out.println("The section " + section.getName() + " was successfully added.");
        }
    }

    synchronized public void addSection(String name) {
        Section section = new Section(name);
        this.addSection(section);
    }

    synchronized public void addSection(String name, Book[] books) {
        Section section = new Section(name, books);
        this.addSection(section);
    }

    synchronized public void removeSection(Section section) {
        Section sectionDB = db.getSection(section.getId());
        if (this.sections.contains(section) || sectionDB != null) {
            this.sections.remove(section);
            for (Book book : db.getBooksFromSection(section.getId())) {
                this.removeBook(book);
            }
            db.deleteSection(section.getId());
            System.out.println("The section " + section.getName() + " was removed.");
        } else {
            System.out.println("The section doesn't exist in the library");
        }
    }

    synchronized public void removeSection(String name) {
        List<Section> sectionsToRemove = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        Section sectionDB = db.getSection(name);
        if (sectionsToRemove.isEmpty() && sectionDB == null) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            for (Section section : sectionsToRemove) {
                this.removeSection(section);
            }
        }
    }

    synchronized public void listBooksFromSection(Section section) {
        Section sectionDB = db.getSection(section.getId());
        if (this.sections.contains(section) || sectionDB != null) {
            section.listBooks();
        } else {
            System.out.println("The section doesn't exist in the library");
        }
    }

    synchronized public void listBooksFromSection(String name) {
        List<Section> sectionsToList = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        Section sectionDB = db.getSection(name);
        if (sectionsToList.isEmpty() && sectionDB == null) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            for (Section section : sectionsToList) {
                section.listBooks();
            }
        }
    }

    synchronized public void addBookToSection(Section section, String title, int noPages, Date publishDate, Author author, PublishingHouse publishingHouse, int noCopies) {
        Section sectionDB = db.getSection(section.getId());
        if (this.sections.contains(section) && sectionDB != null) {
            Pbook book = new Pbook(title, noPages, publishDate, section, author, publishingHouse, noCopies);
            this.addBook(book);
        } else {
            System.out.println("The section " + section.getName() + " doesn't exist.");
        }
    }

    synchronized public void addBookToSection(String name, String title, int noPages, Date publishDate, Author author, PublishingHouse publishingHouse, int noCopies) {
        List<Section> sectionsToAddBook = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        Section sectionDB = db.getSection(name);
        if (sectionsToAddBook.isEmpty() && sectionDB == null) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            for (Section section : sectionsToAddBook) {
                Pbook book = new Pbook(title, noPages, publishDate, section, author, publishingHouse, noCopies);
                this.addBook(book);
            }
        }
    }

    synchronized public void addBookToSection(Section section, String title, int noPages, Date publishDate, Author author, PublishingHouse publishingHouse, String format) {
        Section sectionDB = db.getSection(section.getName());
        if (this.sections.contains(section) || sectionDB != null) {
            Ebook book = new Ebook(title, noPages, publishDate, section, author, publishingHouse, format);
            this.addBook(book);
        } else {
            System.out.println("The section " + section.getName() + " doesn't exist.");
        }
    }

    synchronized public void addBookToSection(String name, String title, int noPages, Date publishDate, Author author, PublishingHouse publishingHouse, String format) {
        List<Section> sectionsToAddBook = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        Section sectionDB = db.getSection(name);
        if (sectionsToAddBook.isEmpty() && sectionDB == null) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            for (Section section : sectionsToAddBook) {
                Ebook book = new Ebook(title, noPages, publishDate, section, author, publishingHouse, format);
                this.addBook(book);
            }
        }
    }

    synchronized public void removeBookFromSection(Section section, String title) {
        Section sectionDB = db.getSection(section.getId());
        if (this.sections.contains(section) || sectionDB != null) {
            this.removeBook(title);
        } else {
            System.out.println("The section " + section.getName() + " doesn't exist.");
        }
    }

    synchronized public void removeBookFromSection(String name, String title) {
        List<Section> sectionsToAddBook = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        Section sectionDB = db.getSection(name);
        if (sectionsToAddBook.isEmpty() && sectionDB == null) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            this.removeBook(title);
        }
    }

    synchronized public void removeBookFromSection(Section section, Book book) {
        Section sectionDB = db.getSection(section.getId());
        if (this.sections.contains(section) || sectionDB != null) {
            this.removeBook(book);
        } else {
            System.out.println("The section " + section.getName() + " doesn't exist.");
        }
    }

    synchronized public void removeBookFromSection(String name, Book book) {
        List<Section> sectionsToAddBook = this.sections.stream().filter(s -> name.equalsIgnoreCase(s.getName())).collect(Collectors.toList());
        Section sectionDB = db.getSection(name);
        if (sectionsToAddBook.isEmpty() && sectionDB == null) {
            System.out.println("The name doesn't match with any sections.");
        } else {
            this.removeBook(book);
        }
    }

    synchronized public void updateSection(Section section) {
        Section sectionDB = db.getSection(section.getId());
        if (!this.sections.contains(section) && sectionDB == null) {
            System.out.println("The section doesn't exist in the library.");
        } else {
            db.updateSection(section);
            List<Section> sectionsToUpdate = this.sections.stream().filter(b -> b.getId() == section.getId()).collect(Collectors.toList());
            for (Section section1 : sectionsToUpdate) {
                this.sections.remove(section1);
            }
            this.sections.add(section);
            System.out.println("The section " + section.getName() + " was successfully modified.");
        }
    }

    synchronized public void updateSection(int id, String name, int noBooks) {
        Section section = new Section(id, name, noBooks);
        this.updateSection(section);
    }


    // authors related actions
    synchronized public void listAllAuthors() {
        List<Author> authors = db.getAuthors();
        if (this.authors.isEmpty()) {
            System.out.println("There are no authors in the library at the moment.");
        } else {
            System.out.println("The authors in the library:");
            for (Author author : this.authors) {
                System.out.println(author);
            }
        }
    }

    synchronized public void addAuthor(Author author) {
        Author authorDB = db.getAuthor(author.getId());
        if (this.authors.contains(author) || authorDB != null) {
            System.out.println("The author already exists in the library.");
        } else {
            db.createAuthor(author);
            this.authors.add(author);
//            this.books.addAll(author.getBooksWritten());
            System.out.println("The author " + author.getName() + " was successfully added.");
        }
    }

    synchronized public void addAuthor(String name, java.util.Date birthDate, String email) {
        Author author = new Author(name, birthDate, email);
        this.addAuthor(author);
    }

    synchronized public void addAuthor(String name, java.util.Date birthdate, String mail, Book[] books) {
        Author author = new Author(name, birthdate, mail, books);
        this.addAuthor(author);
    }

    synchronized public void removeAuthor(Author author) {
        Author authorDB = db.getAuthor(author.getId());
        if (this.authors.contains(author) || authorDB != null) {
            this.authors.remove(author);
            for (Book book : db.getBooksFromAuthor(author.getId())) {
                this.books.remove(book);
            }
            db.deleteAuthor(author.getId());
            System.out.println("The author " + author.getName() + " was successfully removed from the library.");
        } else {
            System.out.println("The author " + author.getName() + " doesn't exist in the library.");
        }
    }

    synchronized public void removeAuthor(String name) {
        List<Author> authorsToRemove = this.authors.stream().filter(a -> name.equalsIgnoreCase(a.getName())).collect(Collectors.toList());
        Author authorDB = db.getAuthor(name);
        if (authorsToRemove.isEmpty() && authorDB == null) {
            System.out.println("The name doesn't match with any author in the library.");
        } else {
            for (Author author : authorsToRemove) {
                this.removeAuthor(author);
            }
        }
    }

    synchronized public void listAllBooksFromAuthor(Author author) {
        Author authorDB = db.getAuthor(author.getId());
        if (this.authors.contains(author) || authorDB != null) {
            author.listBooks();
        } else {
            System.out.println("The author " + author.getName() + " doesn't exist in the library.");
        }
    }

    synchronized public void listAllBooksFromAuthor(String name) {
        List<Author> authorsToList = this.authors.stream().filter(a -> name.equalsIgnoreCase(a.getName())).collect(Collectors.toList());
        Author authorDB = db.getAuthor(name);
        if (authorsToList.isEmpty() && authorDB == null) {
            System.out.println("The name doesn't match with any author in the library.");
        } else {
            for (Author author : authorsToList) {
                this.listAllBooksFromAuthor(author);
            }
        }
    }

    synchronized public void addBookFromAuthor(Author author, String title, int noPages, Section section, PublishingHouse publishingHouse) {
        Author authorDB = db.getAuthor(author.getId());
        if (this.authors.contains(author) || authorDB != null) {
            Pbook pbook = author.publish(title, noPages, section, publishingHouse);
            this.addBook(pbook);
        } else {
            System.out.println("The author " + author.getName() + " doesn't exist in the library.");
        }
    }

    synchronized public void addBookFromAuthor(String name, String title, int noPages, Section section, PublishingHouse publishingHouse) {
        List<Author> authorsToAddBook = this.authors.stream().filter(a -> a.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        Author authorDB = db.getAuthor(name);
        if (!authorsToAddBook.isEmpty() || authorDB != null) {
            for (Author author : authorsToAddBook) {
                this.addBookFromAuthor(author, title, noPages, section, publishingHouse);
            }
        } else {
            System.out.println("The name doesn't match with any author in the library.");
        }
    }

    synchronized public void addBookFromAuthor(Author author, String title, int noPages, Section section, PublishingHouse publishingHouse, String format) {
        Author authorDB = db.getAuthor(author.getId());
        if (this.authors.contains(author) || authorDB != null) {
            Ebook ebook = author.publish(title, noPages, section, publishingHouse, format);
            this.addBook(ebook);
        } else {
            System.out.println("The author " + author.getName() + " doesn't exist in the library.");
        }
    }

    synchronized public void addBookFromAuthor(String name, String title, int noPages, Section section, PublishingHouse publishingHouse, String format) {
        List<Author> authorsToAddBook = this.authors.stream().filter(a -> a.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        Author authorDB = db.getAuthor(name);
        if (!authorsToAddBook.isEmpty() || authorDB != null) {
            for (Author author : authorsToAddBook) {
                this.addBookFromAuthor(author, title, noPages, section, publishingHouse, format);
            }
        } else {
            System.out.println("The name doesn't match with any author in the library.");
        }
    }

    synchronized public void updateAuthor(Author author) {
        Author authorDB = db.getAuthor(author.getId());
        if (!this.authors.contains(author) && authorDB == null) {
            System.out.println("The author doesn't exists in the library.");
        } else {
            db.updateAuthor(author);
            List<Author> authorsToUpdate = this.authors.stream().filter(b -> b.getId() == author.getId()).collect(Collectors.toList());
            for (Author author1 : authorsToUpdate) {
                this.authors.remove(author1);
            }
            this.authors.add(author);
            System.out.println("The author " + author.getName() + " was successfully modified.");
        }
    }

    synchronized public void updateAuthor(int id, String name, java.util.Date birthDate, String email, int noBooksWritten) {
        Author author = new Author(id, name, birthDate, email, noBooksWritten);
        this.updateAuthor(author);
    }


    // readers related actions
    synchronized public void listAllReaders() {
        List<Reader> readers = db.getReaders();
        if (this.readers.isEmpty()) {
            System.out.println("There are no readers enrolled into the library at the moment.");
        } else {
            System.out.println("The readers enrolled into the library:");
            for (Reader reader : this.readers) {
                System.out.println(reader);
            }
        }
    }

    synchronized public void addReader(Reader reader) {
        Reader readerDB = db.getReader(reader.getId());
        if (this.readers.contains(reader) || readerDB != null) {
            System.out.println("The reader already exists in the library.");
        } else {
            db.createReader(reader);
            this.readers.add(reader);
            System.out.println("The reader " + reader.getName() + " was successfully enrolled.");
        }
    }

    synchronized public void addReader(String name, java.util.Date birthDate, String email, String address) {
        Reader reader = new Reader(name, birthDate, email, address);
        this.addReader(reader);
    }

    synchronized public void removeReader(Reader reader) {
        Reader readerDB = db.getReader(reader.getId());
        if (this.readers.contains(reader) || readerDB != null) {
            db.deleteReader(reader.getId());
            this.readers.remove(reader);
            System.out.println("The reader was successfully removed from the library.");
        } else {
            System.out.println("The reader " + reader.getName() + " doesn't exist.");
        }
    }

    synchronized public void removeReader(String name) {
        List<Reader> readersToRemove = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        Reader readerDB = db.getReader(name);
        if (!readersToRemove.isEmpty() || readerDB != null) {
            for (Reader reader : readersToRemove) {
                this.removeReader(reader);
            }
        } else {
            System.out.println("The name doesn't match with any enrolled reader.");
        }
    }

    synchronized public void listBooksLent(Reader reader) {
        Reader readerDB = db.getReader(reader.getId());
        if (this.readers.contains(reader) && readerDB == null) {
            if (db.getBooksLentByReader(reader.getId()).isEmpty()) {
                System.out.println("The reader " + reader.getName() + " hasn't lent any book yet.");
            } else {
                reader.listBooksLent();
            }
        } else {
            System.out.println("The reader " + reader.getName() + " doesn't exist.");
        }
    }

    synchronized public void listBooksLent(String name) {
        List<Reader> readersToView = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        Reader readerDB = db.getReader(name);
        if (!readersToView.isEmpty() || readerDB != null) {
            for (Reader reader : readersToView) {
                this.listBooksLent(reader);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }

    synchronized public void lendBook(Reader reader, Book book) {
        Reader readerDB = db.getReader(reader.getId());
        Book bookDB = db.getBook(book.getId());
        if (this.readers.contains(reader) || readerDB != null) {
            if (this.books.contains(book) || bookDB != null) {
                reader.lendBook(book);
                System.out.println("The reader " + reader.getName() + " lent a book.");
            } else {
                System.out.println("The book doesn't exist in the library.");
            }
        } else {
            System.out.println("The reader " + reader.getName() + " isn't enrolled.");
        }
    }

    synchronized public void lendBook(String name, Book book) {
        List<Reader> readersToLendBook = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        Reader readerDB = db.getReader(name);
        if (!readersToLendBook.isEmpty() || readerDB != null) {
            for (Reader reader : readersToLendBook) {
                this.lendBook(reader, book);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }

    synchronized public void lendBook(Reader reader, String title) {
        List<Book> booksToLend = this.books.stream().filter(b -> b.getTitle().equals(title)).collect(Collectors.toList());
        List<Book> bookDB = db.getBook(title);
        if (!booksToLend.isEmpty() || bookDB != null || !bookDB.isEmpty()) {
            for (Book book : booksToLend) {
                this.lendBook(reader, book);
            }
        } else {
            System.out.println("The title doesn't match with any book from the library.");
        }
    }

    synchronized public void lendBook(String name, String title) {
        List<Reader> readersToLendBook = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        Reader readerDB = db.getReader(name);
        if (!readersToLendBook.isEmpty() || readerDB != null) {
            for (Reader reader : readersToLendBook) {
                this.lendBook(reader, title);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }

    synchronized public void returnBook(Reader reader, Book book) {
        Reader readerDB = db.getReader(reader.getId());
        Book bookDB = db.getBook(book.getId());
        if (this.readers.contains(reader) || readerDB != null) {
            if (this.books.contains(book) || bookDB != null) {
                reader.returnBook(book);
                System.out.println("The reader " + reader.getName() + " returned a book.");
            } else {
                System.out.println("The book doesn't exist in the library.");
            }
        } else {
            System.out.println("The reader " + reader.getName() + " isn't enrolled.");
        }
    }

    synchronized public void returnBook(String name, Book book) {
        List<Reader> readersToReturnBook = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        Reader readerDB = db.getReader(name);
        if (!readersToReturnBook.isEmpty() || readerDB != null) {
            for (Reader reader : readersToReturnBook) {
                this.returnBook(reader, book);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }

    synchronized public void returnBook(Reader reader, String title) {
        List<Book> booksToReturn = this.books.stream().filter(b -> b.getTitle().equals(title)).collect(Collectors.toList());
        List<Book> bookDB = db.getBook(title);
        if (!booksToReturn.isEmpty() || bookDB != null || !bookDB.isEmpty()) {
            for (Book book : booksToReturn) {
                this.returnBook(reader, book);
            }
        } else {
            System.out.println("The title doesn't match with any book from the library.");
        }
    }

    synchronized public void returnBook(String name, String title) {
        List<Reader> readersToReturnBook = this.readers.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        Reader readerDB = db.getReader(name);
        if (!readersToReturnBook.isEmpty() || readerDB != null) {
            for (Reader reader : readersToReturnBook) {
                this.returnBook(reader, title);
            }
        } else {
            System.out.println("The name doesn't match with any reader enrolled into the library.");
        }
    }

    synchronized public void updateReader(Reader reader) {
        Reader readerDB = db.getReader(reader.getId());
        if (!this.readers.contains(reader) && readerDB == null) {
            System.out.println("The reader doesn't exists in the library.");
        } else {
            db.updateReader(reader);
            List<Reader> readersToUpdate = this.readers.stream().filter(b -> b.getId() == reader.getId()).collect(Collectors.toList());
            for (Reader reader1 : readersToUpdate) {
                this.readers.remove(reader1);
            }
            this.readers.add(reader);
            System.out.println("The reader " + reader.getName() + " was successfully modified.");
        }
    }

    synchronized public void updateReader(int id, String name, java.util.Date birthDate, String email, String address, int noBooksLent) {
        Reader reader = new Reader(id, name, birthDate, email, address, noBooksLent);
        this.updateReader(reader);
    }


    // publishing houses related actions
    synchronized public void listAllPublishingHouses() {
        List<PublishingHouse> publishingHouses = db.getPublishingHouses();
        if (this.publishingHouses.isEmpty()) {
            System.out.println("There are no publiching houses assosiated with the library at the moment.");
        } else {
            System.out.println("The publishing houses associated with the library are:");
            for (PublishingHouse publishingHouse : this.publishingHouses) {
                System.out.println(publishingHouse);
            }
        }
    }

    synchronized public void addPublishingHouse(PublishingHouse publishingHouse) {
        PublishingHouse publishingHouseDB = db.getPublishingHouse(publishingHouse.getId());
        if (this.publishingHouses.contains(publishingHouse) || publishingHouseDB != null) {
            System.out.println("The publishing house is already associated with the library.");
        } else {
            db.createPublishingHouse(publishingHouse);
            this.publishingHouses.add(publishingHouse);
            System.out.println("The publishing house " + publishingHouse.getName() + " was successfully associated.");
        }
    }

    synchronized public void addPublishingHouse(String name, Date establishmentDate) {
        PublishingHouse publishingHouse = new PublishingHouse(name, establishmentDate);
        this.addPublishingHouse(publishingHouse);
    }

    synchronized public void removePublishingHouse(PublishingHouse publishingHouse) {
        PublishingHouse publishingHouseDB = db.getPublishingHouse(publishingHouse.getId());
        if (this.publishingHouses.contains(publishingHouse) || publishingHouseDB != null) {
            db.deletePublishingHouse(publishingHouse.getId());
            this.publishingHouses.remove(publishingHouse);
            System.out.println("The publishing house was successfully removed from the library.");
        } else {
            System.out.println("The publishing house " + publishingHouse.getName() + " doesn't exist.");
        }
    }

    synchronized public void removePublishingHouse(String name) {
        List<PublishingHouse> publishingHousesToRemove = this.publishingHouses.stream().filter(ph -> ph.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        PublishingHouse publishingHouseDB = db.getPublishingHouse(name);
        if (!publishingHousesToRemove.isEmpty() || publishingHouseDB != null) {
            for (PublishingHouse publishingHouse : publishingHousesToRemove) {
                this.removePublishingHouse(publishingHouse);
            }
        } else {
            System.out.println("The name doesn't match with any associated publishing houses.");
        }
    }

    synchronized public void listBooksFromPublishingHouse(PublishingHouse publishingHouse) {
        PublishingHouse publishingHouseDB = db.getPublishingHouse(publishingHouse.getId());
        if (this.publishingHouses.contains(publishingHouse) || publishingHouseDB != null) {
            publishingHouse.listBooks();
        } else {
            System.out.println("The publishing house isn't associated with the library");
        }
    }

    synchronized public void listBooksFromPublishingHouse(String name) {
        List<PublishingHouse> publishingHousesToList = this.publishingHouses.stream().filter(ph -> name.equalsIgnoreCase(ph.getName())).collect(Collectors.toList());
        PublishingHouse publishingHouseDB = db.getPublishingHouse(name);
        if (publishingHousesToList.isEmpty() && publishingHouseDB == null) {
            System.out.println("The name doesn't match with any publishing house.");
        } else {
            for (PublishingHouse publishingHouse : publishingHousesToList) {
                this.listBooksFromPublishingHouse(publishingHouse);
            }
        }
    }

    synchronized public void updatePublishingHouse(PublishingHouse publishingHouse) {
        PublishingHouse publishingHouseDB = db.getPublishingHouse(publishingHouse.getId());
        if (!this.publishingHouses.contains(publishingHouse) && publishingHouseDB == null) {
            System.out.println("The publishing house isn't associated with the library.");
        } else {
            db.updatePublishingHouse(publishingHouse);
            List<PublishingHouse> publishingHousesToUpdate = this.publishingHouses.stream().filter(b -> b.getId() == publishingHouse.getId()).collect(Collectors.toList());
            for (PublishingHouse publishingHouse1 : publishingHousesToUpdate) {
                this.publishingHouses.remove(publishingHouse1);
            }
            this.publishingHouses.add(publishingHouse);
            System.out.println("The publishing house " + publishingHouse.getName() + " was successfully modified.");
        }
    }

    synchronized public void updatePublishingHouse(int id, String name, Date establishmentDate, int noBooks) {
        PublishingHouse publishingHouse = new PublishingHouse(id, name, establishmentDate, noBooks);
        this.updatePublishingHouse(publishingHouse);
    }
}
