package library;

import library.books.PublishingHouse;
import library.books.Section;
import library.people.Author;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        Audit audit = Audit.getInstance();
        int option;
        boolean OK;

        do {
            Library.listServices();
            System.out.println("What do you want to do?");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    library.details();
                    audit.write("Listing details", new Date());
                    break;

                case 2:
                    library.listBooks();
                    audit.write("Listing books", new Date());
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

                    audit.write("Adding book " + title, new Date());
                    break;

                case 4:
                    System.out.print("Book title: ");
                    title = scanner.next();

                    library.removeBook(title);
                    audit.write("Removing book " + title, new Date());
                    break;

                case 5:
                    System.out.print("Book title: ");
                    title = scanner.next();

                    System.out.print("One(1) or more copies? ");
                    int typeCopies = scanner.nextInt();

                    if (typeCopies == 1) {
                        library.addCopies(title);
                    } else {
                        System.out.print("Number of copies: ");
                        int noCopies = scanner.nextInt();

                        library.addCopies(title, noCopies);
                    }

                    audit.write("Adding copies of book " + title, new Date());
                    break;

                case 6:
                    System.out.print("Book title: ");
                    title = scanner.next();

                    System.out.print("One(1) or more copies? ");
                    typeCopies = scanner.nextInt();

                    if (typeCopies == 1) {
                        library.lostCopy(title);
                    } else {
                        System.out.print("Number of copies: ");
                        int noCopies = scanner.nextInt();

                        library.lostCopy(title, noCopies);
                    }

                    audit.write("Removing copies of book " + title, new Date());
                    break;

                case 7:
                    library.listSections();
                    audit.write("Listing sections", new Date());
                    break;

                case 8:
                    System.out.print("Section name: ");
                    String sectionName = scanner.next();

                    library.addSection(sectionName);
                    audit.write("Adding section " + sectionName, new Date());
                    break;

                case 9:
                    System.out.print("Section name: ");
                    sectionName = scanner.next();

                    library.removeSection(sectionName);
                    audit.write("Removing section " + sectionName, new Date());
                    break;

                case 10:
                    System.out.print("Section name: ");
                    sectionName = scanner.next();

                    library.listBooksFromSection(sectionName);
                    audit.write("Listing books from section " + sectionName, new Date());
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

                    audit.write("Adding book " + title + " into section " + sectionName, new Date());
                    break;

                case 12:
                    System.out.print("Section name: ");
                    sectionName = scanner.next();

                    System.out.print("Book title: ");
                    title = scanner.next();

                    library.removeBookFromSection(sectionName, title);
                    audit.write("Removing book " + title + " into section " + sectionName, new Date());
                    break;

                case 13:
                    library.listAllAuthors();
                    audit.write("Listing authors", new Date());
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
                    audit.write("Adding author " + authorName, new Date());
                    break;

                case 15:
                    System.out.print("Author name: ");
                    authorName = scanner.next();

                    library.removeAuthor(authorName);
                    audit.write("Removing author " + authorName, new Date());
                    break;

                case 16:
                    System.out.print("Author name: ");
                    authorName = scanner.next();

                    library.listAllBooksFromAuthor(authorName);
                    audit.write("Listing books from author " + authorName, new Date());
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

                    audit.write("Adding book " + title + " from author " + authorName, new Date());
                    break;

                case 18:
                    library.listAllReaders();
                    audit.write("Listing readers", new Date());
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
                    audit.write("Enrolling reader " + readerName, new Date());
                    break;

                case 20:
                    System.out.print("Reader name: ");
                    readerName = scanner.next();

                    library.removeReader(readerName);
                    audit.write("Removing reader " + readerName, new Date());
                    break;

                case 21:
                    System.out.print("Reader name: ");
                    readerName = scanner.next();

                    library.listBooksLent(readerName);
                    audit.write("Listing lent books from reader " + readerName, new Date());
                    break;

                case 22:
                    System.out.print("Reader name: ");
                    readerName = scanner.next();

                    System.out.print("Book title: ");
                    title = scanner.next();

                    library.lendBook(readerName, title);
                    audit.write("Lending book " + title + " by reader "+ readerName, new Date());
                    break;

                case 23:
                    System.out.print("Reader name: ");
                    readerName = scanner.next();

                    System.out.print("Book title: ");
                    title = scanner.next();

                    library.returnBook(readerName, title);
                    audit.write("Returning book " + title + " by reader " + readerName, new Date());
                    break;

                case 24:
                    library.listAllPublishingHouses();
                    audit.write("Listing publishing houses", new Date());
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
                    audit.write("Adding publishing house " + publishingHouseName, new Date());
                    break;

                case 26:
                    System.out.print("Publishing house name: ");
                    publishingHouseName = scanner.next();

                    library.removePublishingHouse(publishingHouseName);
                    audit.write("Removing publishing house " + publishingHouseName, new Date());
                    break;

                case 27:
                    System.out.print("Publishing house name: ");
                    publishingHouseName = scanner.next();

                    library.listBooksFromPublishingHouse(publishingHouseName);
                    audit.write("Listing books from publishing house " + publishingHouseName, new Date());
                    break;
            }

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
