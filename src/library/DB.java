package library;

import library.books.*;
import library.people.Author;
import library.people.Reader;

import java.sql.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class DB implements AutoCloseable {
    private static DB instance;
    private final Connection connection;

    private DB() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:derby:libraryDB;create=true");
        boolean notFoundBooks = true;
        boolean notFoundSections = true;
        boolean notFoundPublishingHouses = true;
        boolean notFoundAuthors = true;
        boolean notFoundReaders = true;
        boolean notFoundLent = true;

        ResultSet results = connection.getMetaData().getTables(null, null, null, new String[]{"TABLE"});

        // verifying if the tables exists
        while (results.next()) {
            switch (results.getString("TABLE_NAME").toUpperCase()) {
                case "BOOKS" -> notFoundBooks = false;
                case "SECTIONS" -> notFoundSections = false;
                case "PUBLISHINGHOUSES" -> notFoundPublishingHouses = false;
                case "AUTHORS" -> notFoundAuthors = false;
                case "READERS" -> notFoundReaders = false;
                case "LENT" -> notFoundLent = false;
            }

            // creating the tables if they don't exist yet
            if (notFoundBooks) {
                connection.createStatement()
                        .execute("CREATE TABLE books (book_id number(3) primary key, title varchar(100), type varchar(20), no_pages number(3), no_copies number(3), publish_date date, format varchar(20), section_id number(3) references sections(section_id) on delete cascade, author_id number(3) references authors(author_id) on delete cascade, publishing_house_id number(3) references publishingHouses(publishing_house_id) on delete cascade)");
            }

            if (notFoundSections) {
                connection.createStatement()
                        .execute("CREATE TABLE sections (section_id number(3) primary key, name varchar(30), no_books number(3))");
            }

            if (notFoundPublishingHouses) {
                connection.createStatement()
                        .execute("CREATE TABLE publishingHouses (publishing_house_id number(3) primary key, name varchar(50), establishment_date date)");
            }

            if (notFoundAuthors) {
                connection.createStatement()
                        .execute("CREATE TABLE authors (author_id number(3) primary key, name varchar(50), birthdate date, e-mail varchar(50), no_books_written number(3))");
            }

            if (notFoundReaders) {
                connection.createStatement()
                        .execute("CREATE TABLE readers (reader_id number(3) primary key, name varchar(50), birthdate date, e-mail varchar(50), address varchar(100), no_books_lent number(3)");
            }

            if (notFoundLent) {
                connection.createStatement()
                        .execute("CREATE TABLE lent (book_id number(3) references books(book_id) on delete cascade, reader_id number(3) references readers(reader_id) on delete cascade, lent_date date, returned bit default 0)");
            }
        }
    }

    public static DB getInstance() {
        if (instance == null) {
            synchronized (DB.class) {
                if (instance == null) {
                    try {
                        instance = new DB();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return instance;
    }

    @Override
    public void close() throws Exception {
        //closing the connection
        connection.close();
    }

    public Section getSection(int sectionId) {
        // getting the section with the given ID from the database
        Section section = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM sections WHERE section_id = ?");
            stmt.setInt(1, sectionId);
            ResultSet results = stmt.executeQuery();
            section = new Section(results.getInt(1), results.getString(2), results.getInt(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return section;
    }

    public Author getAuthor(int authorId) {
        // getting the author with the given ID from the database
        Author author = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM authors WHERE author_id = ?");
            stmt.setInt(1, authorId);
            ResultSet results = stmt.executeQuery();
            author = new Author(results.getInt(1), results.getString(2), results.getDate(3), results.getString(4), results.getInt(5));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return author;
    }

    public PublishingHouse getPublishingHouse(int publishingHOuseId) {
        // getting the publishing house with the given ID from the database
        PublishingHouse publishingHouse = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM publishingHouses WHERE publishing_house_id = ?");
            stmt.setInt(1, publishingHOuseId);
            ResultSet results = stmt.executeQuery();
            publishingHouse = new PublishingHouse(results.getInt(1), results.getString(2), results.getDate(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publishingHouse;
    }

    public Book getBook(int bookId){
        // getting the book with the given ID from the database
        Book book = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books WHERE book_id = ?");
            stmt.setInt(1, bookId);
            ResultSet results = stmt.executeQuery();

            Section section = getSection(results.getInt(8));
            Author author = getAuthor(results.getInt(9));
            PublishingHouse publishingHouse = getPublishingHouse(results.getInt(10));

            if (results.getString(3).equalsIgnoreCase("pbook")) {
                book = new Pbook(results.getInt(1), results.getString(2), results.getInt(4), results.getDate(6), section, author, publishingHouse, results.getInt(5));
            } else {
                book = new Ebook(results.getInt(1), results.getString(2), results.getInt(4), results.getDate(6), section, author, publishingHouse, results.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    public Reader getReader(int readerId){
        // getting the reader with the given ID from the database
        Reader reader = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM readers WHERE reader_id = ?");
            stmt.setInt(1, readerId);
            ResultSet results = stmt.executeQuery();
            reader = new Reader(results.getInt(1), results.getString(2), results.getDate(3), results.getString(4), results.getString(5), results.getInt(6));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reader;
    }

    public Section getSection(String sectionName) {
        // getting the section with the given ID from the database
        Section section = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM sections WHERE name = ?");
            stmt.setString(1, sectionName);
            ResultSet results = stmt.executeQuery();
            section = new Section(results.getInt(1), results.getString(2), results.getInt(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return section;
    }

    public Author getAuthor(String authorName) {
        // getting the author with the given ID from the database
        Author author = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM authors WHERE name = ?");
            stmt.setString(1, authorName);
            ResultSet results = stmt.executeQuery();
            author = new Author(results.getInt(1), results.getString(2), results.getDate(3), results.getString(4), results.getInt(5));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return author;
    }

    public PublishingHouse getPublishingHouse(String publishingHouseName) {
        // getting the publishing house with the given ID from the database
        PublishingHouse publishingHouse = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM publishingHouses WHERE name = ?");
            stmt.setString(1, publishingHouseName);
            ResultSet results = stmt.executeQuery();
            publishingHouse = new PublishingHouse(results.getInt(1), results.getString(2), results.getDate(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publishingHouse;
    }

    public Book getBook(String bookTitle){
        // getting the book with the given ID from the database
        Book book = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books WHERE title = ?");
            stmt.setString(1, bookTitle);
            ResultSet results = stmt.executeQuery();

            Section section = getSection(results.getInt(8));
            Author author = getAuthor(results.getInt(9));
            PublishingHouse publishingHouse = getPublishingHouse(results.getInt(10));

            if (results.getString(3).equalsIgnoreCase("pbook")) {
                book = new Pbook(results.getInt(1), results.getString(2), results.getInt(4), results.getDate(6), section, author, publishingHouse, results.getInt(5));
            } else {
                book = new Ebook(results.getInt(1), results.getString(2), results.getInt(4), results.getDate(6), section, author, publishingHouse, results.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    public Reader getReader(String readerName){
        // getting the reader with the given ID from the database
        Reader reader = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM readers WHERE name = ?");
            stmt.setString(1, readerName);
            ResultSet results = stmt.executeQuery();
            reader = new Reader(results.getInt(1), results.getString(2), results.getDate(3), results.getString(4), results.getString(5), results.getInt(6));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reader;
    }



    // books related actions
    public void createBook(Book book) {
        // adding a book into the database
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO books (book_id, title, type, no_pages, no_copies, publish_date, format, section_id, author_id, publishing_house_id) VALUES (?,?,?,?,?,?,?,?,?,?)");
            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getType());
            statement.setInt(4, book.getNoPages());

            if (book.getNoCopies() != Double.POSITIVE_INFINITY) {
                statement.setDouble(5, book.getNoCopies());
            } else {
                statement.setDouble(5, Double.POSITIVE_INFINITY);
            }

            statement.setDate(6, (java.sql.Date) book.getPublishDate());
            statement.setString(7, book.getFormat());
            statement.setInt(8, book.getSection().getId());
            statement.setInt(9, book.getAuthor().getId());
            statement.setInt(10, book.getPublishingHouse().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBooks() {
        // getting all the books from th database
        List<Book> books = new ArrayList<>();

        try {
            ResultSet results = connection.createStatement().executeQuery("SELECT book_id FROM books");
            while (results.next()) {
                books.add(getBook(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public void updateBook(Book book) {
        // updating a book from the database
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE books SET title = ? type = ? no_pages = ? no_copies = ? publish_date = ? format = ? section_id = ? author_id = ? publishing_house_id = ? WHERE book_id = ?");
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getType());
            statement.setInt(3, book.getNoPages());
            statement.setDouble(4, book.getNoCopies());
            statement.setDate(5, (java.sql.Date) book.getPublishDate());
            statement.setString(6, book.getFormat());
            statement.setInt(7, book.getSection().getId());
            statement.setInt(8, book.getAuthor().getId());
            statement.setInt(9, book.getPublishingHouse().getId());
            statement.setInt(10, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int bookId) {
        // deleting a book from the database
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM books WHERE book_id = ?");
            statement.setInt(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // sections related actions
    public void createSection(Section section) {
        // adding a section into the database
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO sections (section_id, name, no_books) VALUES (?,?,?)");
            statement.setInt(1, section.getId());
            statement.setString(2, section.getName());
            statement.setInt(3, section.getNoBooks());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Section> getSections() {
        // getting all the sections from the database
        List<Section> sections = new ArrayList<>();

        try {
            ResultSet results = connection.createStatement().executeQuery("SELECT section_id FROM sections");
            while (results.next()) {
                sections.add(getSection(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sections;
    }

    public void updateSection(Section section) {
        // updating a section from the database
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE sections SET name = ? no_books = ? WHERE section_id = ?");
            statement.setString(1, section.getName());
            statement.setInt(2, section.getNoBooks());
            statement.setInt(3, section.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSection(int sectionId) {
        // deleting a section from the database
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM sections WHERE section_id = ?");
            statement.setInt(1, sectionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Book> getBooksFromSection(int sectionId) {
        // getting all the books from a given section from th database
        List<Book> books = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT book_id FROM books WHERE section_id = ?");
            statement.setInt(1, sectionId);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                books.add(getBook(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }


    // publishing houses related actions
    public void createPublishingHouse(PublishingHouse publishingHouse) {
        // adding a publishing house into the database
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO publishingHouses (publishing_house_id, name, establishment_date) VALUES (?,?,?)");
            statement.setInt(1, publishingHouse.getId());
            statement.setString(2, publishingHouse.getName());
            statement.setDate(3, (java.sql.Date) publishingHouse.getEstablishmentDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PublishingHouse> getPublishingHouses() {
        // getting all the publishing houses from the database
        List<PublishingHouse> publishingHouses = new ArrayList<>();

        try {
            ResultSet results = connection.createStatement().executeQuery("SELECT publishing_house_id FROM publishingHouses");
            while (results.next()) {
                publishingHouses.add(getPublishingHouse(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publishingHouses;
    }

    public void updatePublishingHouse(PublishingHouse publishingHouse) {
        // updating a publishing house from the database
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE publishingHouses SET name = ? establishment_date = ? WHERE publishing_house_id = ?");
            statement.setString(1, publishingHouse.getName());
            statement.setDate(2, (java.sql.Date) publishingHouse.getEstablishmentDate());
            statement.setInt(3, publishingHouse.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePublishingHouse(int publishingHouseId) {
        // deleting a publishing house from the database
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM publishingHouses WHERE publishing_house_id = ?");
            statement.setInt(1, publishingHouseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Book> getBooksFromPublishingHouse(int publishingHouseId) {
        // getting all the books published by a given publishing house from the database
        List<Book> books = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT book_id FROM books WHERE publishing_house_id = ?");
            statement.setInt(1, publishingHouseId);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                books.add(getBook(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }


    // authors related actions
    public void createAuthor(Author author) {
        // adding an author into the database
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO authors (author_id, name, birthdate, e-mail, no_books_written) VALUES (?,?,?,?,?)");
            statement.setInt(1, author.getId());
            statement.setString(2, author.getName());
            statement.setDate(3, (Date) author.getBirthDate());
            statement.setString(4, author.getMail());
            statement.setInt(5, author.getNoBooksWritten());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Author> getAuthors() {
        // getting all the authors from the database
        List<Author> authors = new ArrayList<>();

        try {
            ResultSet results = connection.createStatement().executeQuery("SELECT author_id FROM authors");
            while (results.next()) {
                authors.add(getAuthor(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    public void updateAuthor(Author author) {
        // updating an author from the database
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE authors SET name = ? birthdate = ? e-mail = ? no_books_written = ? WHERE author_id = ?");
            statement.setString(1, author.getName());
            statement.setDate(2, (Date) author.getBirthDate());
            statement.setString(3, author.getMail());
            statement.setInt(4, author.getNoBooksWritten());
            statement.setInt(5, author.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAuthor(int authorId){
        // deleting an author from the database
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM authors WHERE author_id = ?");
            statement.setInt(1, authorId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBooksFromAuthor(int authorId) {
        // getting all the books written by a given author from the database
        List<Book> books = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT book_id FROM books WHERE author_id = ?");
            statement.setInt(1, authorId);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                books.add(getBook(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }


    // readers related actions
    public void createReader(Reader reader) {
        // adding a reader into the database
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO readers (reader_id, name, birthdate, e-mail, address, no_books_lent) VALUES (?,?,?,?,?)");
            statement.setInt(1, reader.getId());
            statement.setString(2, reader.getName());
            statement.setDate(3, (Date) reader.getBirthDate());
            statement.setString(4, reader.getMail());
            statement.setString(5, reader.getAddress());
            statement.setInt(6, reader.getNoBooksLent());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reader> getReaders() {
        // getting all the readers from the database
        List<Reader> readers = new ArrayList<>();

        try {
            ResultSet results = connection.createStatement().executeQuery("SELECT reader_id FROM readers");
            while (results.next()) {
                readers.add(getReader(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return readers;
    }

    public void updateReader(Reader reader) {
        // updating a reader from the database
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE readers SET name = ? birthdate = ? e-mail = ? address = ? no_books_lent = ? WHERE reader_id = ?");
            statement.setString(1, reader.getName());
            statement.setDate(2, (Date) reader.getBirthDate());
            statement.setString(3, reader.getMail());
            statement.setString(4, reader.getAddress());
            statement.setInt(5, reader.getNoBooksLent());
            statement.setInt(6, reader.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReader(int readerId){
        // deleting a reader from the database
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM readers WHERE reader_id = ?");
            statement.setInt(1, readerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBooksLentByReader(int readerId) {
        // getting all the books lent a given reader from th database
        List<Book> books = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT book_id FROM lent WHERE reader_id = ?");
            statement.setInt(1, readerId);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                books.add(getBook(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }


    //book lendings related actions
    public void createLent(int bookId, int readerId, Date lentDate) {
        // adding a book lending into the database
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO lent (book_id, reader_id, lent_date) VALUES(?,?,?)");
            statement.setInt(1, bookId);
            statement.setInt(2, readerId);
            statement.setDate(3, lentDate);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Map<Date, SimpleEntry<SimpleEntry<Book, Reader>, Boolean>> getLending() {
        // getting all the book lendings from the library
        Map<Date, SimpleEntry<SimpleEntry<Book, Reader>, Boolean>> lendings =  new HashMap<>();

        try {
            ResultSet results = connection.createStatement().executeQuery("SELECT * FROM lent");
            while(results.next()){
                SimpleEntry entry = new SimpleEntry(getBook(results.getInt(1)), getReader(results.getInt(2)));
                SimpleEntry entry1 = new SimpleEntry(entry, results.getByte(4));
                lendings.put(results.getDate(3), entry1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lendings;
    }

    public void updateLent(int bookId, int readerId, Date lentDate, Boolean returned){
        // updating a book lending from the database
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE lent SET lent_date = ? returned = ? WHERE book_id = ? AND reader_id = ?");
            statement.setDate(1, lentDate);
            statement.setByte(2, (byte) (returned ? 1 : 0));
            statement.setInt(2, bookId);
            statement.setInt(3, readerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLent(int bookId, int readerId){
        // deleting a book lending from the database
        try {
            PreparedStatement statement = connection.prepareStatement("DELTE FROM lent WHERE book_id = ? AND reader_id = ?");
            statement.setInt(1, bookId);
            statement.setInt(2, readerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
