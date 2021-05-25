package library;

import library.books.PublishingHouse;
import library.books.Section;
import library.database.Audit;
import library.database.CSV;
import library.database.DB;
import library.gui.GUI;
import library.people.Author;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Program {
    private static final Library library = Library.getInstance();
    private static final Audit audit = Audit.getInstance();
    private static final CSV csv = CSV.getInstance();
    private static final DB db = DB.getInstance();
    private static GUI gui;

    public static void main(String[] args) {
        readAuthors();
        readSections();
        readPublishingHouse();
        readBooks();
        readReaders();

        gui = GUI.getInstance();

        // runProgram();
    }

    // file reading
    public static void readAuthors(){
        for (String line : csv.read("authors.csv")){
            String[] authorDetails = line.split(", ");
            String name = authorDetails[0];

            Date birthdate = null;
            try {
                birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(authorDetails[1]);
            } catch (Exception e){
                System.out.println("The date format is incorrect.");
            }

            String email = authorDetails[2];

            library.addAuthor(name, birthdate, email);
            audit.log("Adding author " + name, new Date(), Thread.currentThread().getName());
        }
    }

    public static void readSections(){
        for (String name : csv.read("sections.csv")){
            library.addSection(name);
            audit.log("Adding section " + name, new Date(), Thread.currentThread().getName());
        }
    }

    public static void readPublishingHouse(){
        for (String line : csv.read("publishingHouses.csv")){
            String[] publishingHouseDetails = line.split(", ");
            String name = publishingHouseDetails[0];

            Date establishmentDate = null;
            try {
                establishmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(publishingHouseDetails[1]);
            } catch (Exception e){
                System.out.println("The date format is incorrect.");
            }

            library.addPublishingHouse(name, establishmentDate);
            audit.log("Adding publishing house " + name, new Date(), Thread.currentThread().getName());
        }
    }

    public static void readBooks(){
        for (String line : csv.read("books.csv")){
            String[] bookDetails = line.split(", ");
            String title = bookDetails[1];
            int noPages = Integer.parseInt(bookDetails[2]);

            Date publishingDate = null;
            try {
                publishingDate = new SimpleDateFormat("dd/MM/yyyy").parse(bookDetails[3]);
            } catch (Exception e){
                System.out.println("The date format is incorrect.");
            }


            Section section = null;
            try {
                section = library.getSection(bookDetails[4]);
            } catch (Exception e){
                System.out.println("The section " + bookDetails[4] + " doesn't exist.");
            }

            Author author = null;
            try {
                author = library.getAuthor(bookDetails[5]);
            } catch (Exception e){
                System.out.println("The author " + bookDetails[5] + " doesn't exist.");
            }

            PublishingHouse publishingHouse = null;
            try {
                publishingHouse = library.getPublishingHouse(bookDetails[6]);
            } catch (Exception e){
                System.out.println("The publishing house " + bookDetails[6] + " doesn't exist");
            }

            if (bookDetails[0].equalsIgnoreCase("pbook")){
                int noCopies = Integer.parseInt(bookDetails[7]);
                library.addBook(title, noPages, publishingDate, section, author, publishingHouse, noCopies);
            } else {
                String format = bookDetails[7];
                library.addBook(title, noPages, publishingDate, section, author, publishingHouse, format);
            }

            audit.log("Adding book " + title, new Date(), Thread.currentThread().getName());
        }
    }

    public static void readReaders(){
        for (String line : csv.read("readers.csv")){
            String[] readerDetails = line.split(", ");
            String name = readerDetails[0];

            Date birthdate = null;
            try {
                birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(readerDetails[1]);
            } catch (Exception e){
                System.out.println("The date format is incorrect.");
            }

            String email = readerDetails[2];
            String address = readerDetails[3];

            library.addReader(name, birthdate, email, address);
            audit.log("Adding reader " + name, new Date(), Thread.currentThread().getName());
        }
    }


    // running program
    public static void runProgram(){
        Scanner scanner = new Scanner(System.in);
        int option;
        boolean OK;
        String action = "";

        do {
            Library.listServices();
            System.out.println("What do you want to do?");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    library.details();
                    action = "Listing details";
                    break;

                case 2:
                    library.listBooks();
                    action = "Listing books";
                    break;

                case 3:
                    System.out.println("Available types:");
                    System.out.println("1. Physical book");
                    System.out.println("2. Ebook");
                    int typeOption = scanner.nextInt();

                    System.out.print("Title: ");
                    String title = scanner.next();

                    System.out.print("Number of pages: ");
                    int noPages = scanner.nextInt();

                    Date publishingDate = null;
                    boolean ok = Boolean.FALSE;
                    do {
                        System.out.print("Publishing date (format dd/mm/yyyy): ");
                        try {
                            publishingDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                            ok = Boolean.TRUE;
                        } catch (ParseException e) {
                            System.out.println("The date does not match the format.");
                        }
                    } while (!ok);

                    Section section = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Existing section(1) or new section(2)? ");
                        int typeSection = scanner.nextInt();

                        System.out.print("Section name: ");
                        String sectionName = scanner.next();
                        if (typeSection == 1) {
                            try {
                                section = library.getSection(sectionName);
                                ok = Boolean.TRUE;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            section = new Section(sectionName);
                            ok = Boolean.TRUE;
                        }
                    } while (!ok);

                    Author author = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Existing author(1) or new author(2)? ");
                        int typeAuthor = scanner.nextInt();

                        System.out.print("Author name: ");
                        String authorName = scanner.next();
                        if (typeAuthor == 1) {
                            try {
                                author = library.getAuthor(authorName);
                                ok = Boolean.TRUE;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            Date birthdate = null;
                            boolean ok1 = Boolean.FALSE;
                            do {
                                System.out.print("Birthdate (format dd/mm/yyyy): ");
                                try {
                                    birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                                    ok1 = Boolean.TRUE;
                                } catch (ParseException e) {
                                    System.out.println("The date does not match the format.");
                                }
                            } while (!ok1);

                            System.out.print("Email: ");
                            String email = scanner.next();

                            author = new Author(authorName, birthdate, email);
                            ok = Boolean.TRUE;
                        }
                    } while (!ok);

                    PublishingHouse publishingHouse = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Existing publishing house(1) or new publishing house(2)? ");
                        int typePublishingHouse = scanner.nextInt();

                        System.out.print("Publishing house name: ");
                        String publishingHouseName = scanner.next();
                        if (typePublishingHouse == 1) {
                            try {
                                publishingHouse = library.getPublishingHouse(publishingHouseName);
                                ok = Boolean.TRUE;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            Date establishmentDate = null;
                            boolean ok1 = Boolean.FALSE;
                            do {
                                System.out.print("Establishment date (format dd/mm/yyyy): ");
                                try {
                                    establishmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                                    ok1 = Boolean.TRUE;
                                } catch (ParseException e) {
                                    System.out.println("The date does not match the format.");
                                }
                            } while (!ok1);

                            publishingHouse = new PublishingHouse(publishingHouseName, establishmentDate);
                            ok = Boolean.TRUE;
                        }
                    } while (!ok);

                    if (typeOption == 1) {
                        System.out.print("Number of copies: ");
                        int noCopies = scanner.nextInt();

                        library.addBook(title, noPages, publishingDate, section, author, publishingHouse, noCopies);
                    } else {
                        System.out.print("Format: ");
                        String format = scanner.next();

                        library.addBook(title, noPages, publishingDate, section, author, publishingHouse, format);
                    }

                    action = "Adding book " + title;
                    break;

                case 4:
                    System.out.print("Book title: ");
                    title = scanner.next();

                    library.removeBook(title);
                    action = "Removing book " + title;
                    break;

                case 5:
                    System.out.print("Book title: ");
                    title = scanner.next();

                    System.out.print("One(1) or more copies(2)? ");
                    int typeCopies = scanner.nextInt();

                    if (typeCopies == 1) {
                        library.addCopies(title);
                    } else {
                        System.out.print("Number of copies: ");
                        int noCopies = scanner.nextInt();

                        library.addCopies(title, noCopies);
                    }

                    action = "Adding copies of book " + title;
                    break;

                case 6:
                    System.out.print("Book title: ");
                    title = scanner.next();

                    System.out.print("One(1) or more copies(2)? ");
                    typeCopies = scanner.nextInt();

                    if (typeCopies == 1) {
                        library.lostCopy(title);
                    } else {
                        System.out.print("Number of copies: ");
                        int noCopies = scanner.nextInt();

                        library.lostCopy(title, noCopies);
                    }

                    action = "Removing copies of book " + title;
                    break;

                case 7:
                    library.listSections();
                    action = "Listing sections";
                    break;

                case 8:
                    System.out.print("Section name: ");
                    String sectionName = scanner.next();

                    library.addSection(sectionName);
                    action = "Adding section " + sectionName;
                    break;

                case 9:
                    System.out.print("Section name: ");
                    sectionName = scanner.next();

                    library.removeSection(sectionName);
                    action = "Removing section " + sectionName;
                    break;

                case 10:
                    System.out.print("Section name: ");
                    sectionName = scanner.next();

                    library.listBooksFromSection(sectionName);
                    action = "Listing books from section " + sectionName;
                    break;

                case 11:
                    System.out.print("Section name: ");
                    sectionName = scanner.next();

                    System.out.println("Available types:");
                    System.out.println("1. Physical book");
                    System.out.println("2. Ebook");
                    typeOption = scanner.nextInt();

                    System.out.print("Title: ");
                    title = scanner.next();

                    System.out.print("Number of pages: ");
                    noPages = scanner.nextInt();

                    publishingDate = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Publishing date (format dd/mm/yyyy): ");
                        try {
                            publishingDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                            ok = Boolean.TRUE;
                        } catch (ParseException e) {
                            System.out.println("The date does not match the format.");
                        }
                    } while (!ok);

                    author = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Existing author(1) or new author(2)? ");
                        int typeAuthor = scanner.nextInt();

                        System.out.print("Author name: ");
                        String authorName = scanner.next();
                        if (typeAuthor == 1) {
                            try {
                                author = library.getAuthor(authorName);
                                ok = Boolean.TRUE;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            Date birthdate = null;
                            boolean ok1 = Boolean.FALSE;
                            do {
                                System.out.print("Birthdate (format dd/mm/yyyy): ");
                                try {
                                    birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                                    ok1 = Boolean.TRUE;
                                } catch (ParseException e) {
                                    System.out.println("The date does not match the format.");
                                }
                            } while (!ok1);

                            System.out.print("Email: ");
                            String email = scanner.next();

                            author = new Author(authorName, birthdate, email);
                            ok = Boolean.TRUE;
                        }
                    } while (!ok);

                    publishingHouse = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Existing publishing house(1) or new publishing house(2)? ");
                        int typePublishingHouse = scanner.nextInt();

                        System.out.print("Publishing house name: ");
                        String publishingHouseName = scanner.next();
                        if (typePublishingHouse == 1) {
                            try {
                                publishingHouse = library.getPublishingHouse(publishingHouseName);
                                ok = Boolean.TRUE;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            Date establishmentDate = null;
                            boolean ok1 = Boolean.FALSE;
                            do {
                                System.out.print("Establishment date (format dd/mm/yyyy): ");
                                try {
                                    establishmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                                    ok1 = Boolean.TRUE;
                                } catch (ParseException e) {
                                    System.out.println("The date does not match the format.");
                                }
                            } while (!ok1);

                            publishingHouse = new PublishingHouse(publishingHouseName, establishmentDate);
                            ok = Boolean.TRUE;
                        }
                    } while (!ok);

                    if (typeOption == 1) {
                        System.out.print("Number of copies: ");
                        int noCopies = scanner.nextInt();

                        library.addBookToSection(sectionName, title, noPages, publishingDate, author, publishingHouse, noCopies);
                    } else {
                        System.out.print("Format: ");
                        String format = scanner.next();

                        library.addBookToSection(sectionName, title, noPages, publishingDate, author, publishingHouse, format);
                    }

                    action = "Adding book " + title + " into section " + sectionName;
                    break;

                case 12:
                    System.out.print("Section name: ");
                    sectionName = scanner.next();

                    System.out.print("Book title: ");
                    title = scanner.next();

                    library.removeBookFromSection(sectionName, title);
                    action = "Removing book " + title + " into section " + sectionName;
                    break;

                case 13:
                    library.listAllAuthors();
                    action = "Listing authors";
                    break;

                case 14:
                    System.out.print("Author name: ");
                    String authorName = scanner.next();

                    Date birthdate = null;
                    boolean ok1 = Boolean.FALSE;
                    do {
                        System.out.print("Birthdate (format dd/mm/yyyy): ");
                        try {
                            birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                            ok1 = Boolean.TRUE;
                        } catch (ParseException e) {
                            System.out.println("The date does not match the format.");
                        }
                    } while (!ok1);

                    System.out.print("Email: ");
                    String email = scanner.next();

                    library.addAuthor(authorName, birthdate, email);
                    action = "Adding author " + authorName;
                    break;

                case 15:
                    System.out.print("Author name: ");
                    authorName = scanner.next();

                    library.removeAuthor(authorName);
                    action = "Removing author " + authorName;
                    break;

                case 16:
                    System.out.print("Author name: ");
                    authorName = scanner.next();

                    library.listAllBooksFromAuthor(authorName);
                    action = "Listing books from author " + authorName;
                    break;

                case 17:
                    System.out.print("Author name: ");
                    authorName = scanner.next();

                    System.out.println("Available types:");
                    System.out.println("1. Physical book");
                    System.out.println("2. Ebook");
                    typeOption = scanner.nextInt();

                    System.out.print("Title: ");
                    title = scanner.next();

                    System.out.print("Number of pages: ");
                    noPages = scanner.nextInt();

                    section = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Existing section(1) or new section(2)? ");
                        int typeSection = scanner.nextInt();

                        System.out.print("Section name: ");
                        sectionName = scanner.next();
                        if (typeSection == 1) {
                            try {
                                section = library.getSection(sectionName);
                                ok = Boolean.TRUE;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            section = new Section(sectionName);
                            ok = Boolean.TRUE;
                        }
                    } while (!ok);

                    publishingHouse = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Existing publishing house(1) or new publishing house(2)? ");
                        int typePublishingHouse = scanner.nextInt();

                        System.out.print("Publishing house name: ");
                        String publishingHouseName = scanner.next();
                        if (typePublishingHouse == 1) {
                            try {
                                publishingHouse = library.getPublishingHouse(publishingHouseName);
                                ok = Boolean.TRUE;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            Date establishmentDate = null;
                            ok1 = Boolean.FALSE;
                            do {
                                System.out.print("Establishment date (format dd/mm/yyyy): ");
                                try {
                                    establishmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                                    ok1 = Boolean.TRUE;
                                } catch (ParseException e) {
                                    System.out.println("The date does not match the format.");
                                }
                            } while (!ok1);

                            publishingHouse = new PublishingHouse(publishingHouseName, establishmentDate);
                            ok = Boolean.TRUE;
                        }
                    } while (!ok);

                    if (typeOption == 1) {
                        library.addBookFromAuthor(authorName, title, noPages, section, publishingHouse);
                    } else {
                        System.out.print("Format: ");
                        String format = scanner.next();

                        library.addBookFromAuthor(authorName, title, noPages, section, publishingHouse, format);
                    }

                    action = "Adding book " + title + " from author " + authorName;
                    break;

                case 18:
                    library.listAllReaders();
                    action = "Listing readers";
                    break;

                case 19:
                    System.out.print("Reader name: ");
                    String readerName = scanner.next();

                    birthdate = null;
                    ok1 = Boolean.FALSE;
                    do {
                        System.out.print("Birthdate (format dd/mm/yyyy): ");
                        try {
                            birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                            ok1 = Boolean.TRUE;
                        } catch (ParseException e) {
                            System.out.println("The date does not match the format.");
                        }
                    } while (!ok1);

                    System.out.print("Email: ");
                    email = scanner.next();

                    System.out.print("Address: ");
                    String address = scanner.next();

                    library.addReader(readerName, birthdate, email, address);
                    action = "Enrolling reader " + readerName;
                    break;

                case 20:
                    System.out.print("Reader name: ");
                    readerName = scanner.next();

                    library.removeReader(readerName);
                    action = "Removing reader " + readerName;
                    break;

                case 21:
                    System.out.print("Reader name: ");
                    readerName = scanner.next();

                    library.listBooksLent(readerName);
                    action = "Listing lent books from reader " + readerName;
                    break;

                case 22:
                    System.out.print("Reader name: ");
                    readerName = scanner.next();

                    System.out.print("Book title: ");
                    title = scanner.next();

                    library.lendBook(readerName, title);
                    action = "Lending book " + title + " by reader "+ readerName;
                    break;

                case 23:
                    System.out.print("Reader name: ");
                    readerName = scanner.next();

                    System.out.print("Book title: ");
                    title = scanner.next();

                    library.returnBook(readerName, title);
                    action = "Returning book " + title + " by reader " + readerName;
                    break;

                case 24:
                    library.listAllPublishingHouses();
                    action = "Listing publishing houses";
                    break;

                case 25:
                    System.out.print("Publishing house name: ");
                    String publishingHouseName = scanner.next();

                    Date establishmentDate = null;
                    ok1 = Boolean.FALSE;
                    do {
                        System.out.print("Establishment date (format dd/mm/yyyy): ");
                        try {
                            establishmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
                            ok1 = Boolean.TRUE;
                        } catch (ParseException e) {
                            System.out.println("The date does not match the format.");
                        }
                    } while (!ok1);

                    library.addPublishingHouse(publishingHouseName, establishmentDate);
                    action = "Adding publishing house " + publishingHouseName;
                    break;

                case 26:
                    System.out.print("Publishing house name: ");
                    publishingHouseName = scanner.next();

                    library.removePublishingHouse(publishingHouseName);
                    action = "Removing publishing house " + publishingHouseName;
                    break;

                case 27:
                    System.out.print("Publishing house name: ");
                    publishingHouseName = scanner.next();

                    library.listBooksFromPublishingHouse(publishingHouseName);
                    action = "Listing books from publishing house " + publishingHouseName;
                    break;
            }

            audit.log(action, new Date(), Thread.currentThread().getName());
            System.out.println();
            System.out.println("Do you want to continue? Yes(1) No(2): ");
            int typeOK = scanner.nextInt();
            if (typeOK == 1) {
                System.out.println("---------------------------------------------");
                OK = Boolean.TRUE;
            } else {
                OK = Boolean.FALSE;
            }
        } while (OK && option >= 1 && option <= 27);
    }
}
