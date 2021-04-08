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
        int option;
        boolean OK;

        do {
            Library.listServices();
            System.out.println("What do you want to do?");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    library.details();
                    break;

                case 2:
                    library.listBooks();
                    break;

                case 3:
                    System.out.println("Available types:");
                    System.out.println("1. Physical book");
                    System.out.println("2. Ebook");
                    int typeOption = scanner.nextInt();

                    System.out.print("Title: ");
                    String title = scanner.nextLine();

                    System.out.print("Number of pages: ");
                    int noPages = scanner.nextInt();

                    Date publishingDate = null;
                    boolean ok = Boolean.FALSE;
                    do {
                        System.out.print("Publishing date (format dd/mm/yyyy): ");
                        try {
                            publishingDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
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
                        String sectionName = scanner.nextLine();
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
                        String authorName = scanner.nextLine();
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
                                    birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
                                    ok1 = Boolean.TRUE;
                                } catch (ParseException e) {
                                    System.out.println("The date does not match the format.");
                                }
                            } while (!ok1);

                            System.out.print("Email: ");
                            String email = scanner.nextLine();

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
                        String publishingHouseName = scanner.nextLine();
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
                                    establishmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
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
                        String format = scanner.nextLine();

                        library.addBook(title, noPages, publishingDate, section, author, publishingHouse, format);
                    }
                    break;

                case 4:
                    System.out.print("Book title: ");
                    title = scanner.nextLine();

                    library.removeBook(title);
                    break;

                case 5:
                    System.out.print("Book title: ");
                    title = scanner.nextLine();

                    System.out.print("One(1) or more copies? ");
                    int typeCopies = scanner.nextInt();

                    if (typeCopies == 1) {
                        library.addCopies(title);
                    } else {
                        System.out.print("Number of copies: ");
                        int noCopies = scanner.nextInt();

                        library.addCopies(title, noCopies);
                    }
                    break;

                case 6:
                    System.out.print("Book title: ");
                    title = scanner.nextLine();

                    System.out.print("One(1) or more copies? ");
                    typeCopies = scanner.nextInt();

                    if (typeCopies == 1) {
                        library.lostCopy(title);
                    } else {
                        System.out.print("Number of copies: ");
                        int noCopies = scanner.nextInt();

                        library.lostCopy(title, noCopies);
                    }
                    break;

                case 7:
                    library.listSections();
                    break;

                case 8:
                    System.out.print("Section name: ");
                    String sectionName = scanner.nextLine();

                    library.addSection(sectionName);
                    break;

                case 9:
                    System.out.print("Section name: ");
                    sectionName = scanner.nextLine();

                    library.removeSection(sectionName);
                    break;

                case 10:
                    System.out.print("Section name: ");
                    sectionName = scanner.nextLine();

                    library.listBooksFromSection(sectionName);
                    break;

                case 11:
                    System.out.print("Section name: ");
                    sectionName = scanner.nextLine();

                    System.out.println("Available types:");
                    System.out.println("1. Physical book");
                    System.out.println("2. Ebook");
                    typeOption = scanner.nextInt();

                    System.out.print("Title: ");
                    title = scanner.nextLine();

                    System.out.print("Number of pages: ");
                    noPages = scanner.nextInt();

                    publishingDate = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Publishing date (format dd/mm/yyyy): ");
                        try {
                            publishingDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
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
                        String authorName = scanner.nextLine();
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
                                    birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
                                    ok1 = Boolean.TRUE;
                                } catch (ParseException e) {
                                    System.out.println("The date does not match the format.");
                                }
                            } while (!ok1);

                            System.out.print("Email: ");
                            String email = scanner.nextLine();

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
                        String publishingHouseName = scanner.nextLine();
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
                                    establishmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
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
                        String format = scanner.nextLine();

                        library.addBookToSection(sectionName, title, noPages, publishingDate, author, publishingHouse, format);
                    }
                    break;

                case 12:
                    System.out.print("Section name: ");
                    sectionName = scanner.nextLine();

                    System.out.print("Book title: ");
                    title = scanner.nextLine();

                    library.removeBookFromSection(sectionName, title);
                    break;

                case 13:
                    library.listAllAuthors();
                    break;

                case 14:
                    System.out.print("Author name: ");
                    String authorName = scanner.nextLine();

                    Date birthdate = null;
                    boolean ok1 = Boolean.FALSE;
                    do {
                        System.out.print("Birthdate (format dd/mm/yyyy): ");
                        try {
                            birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
                            ok1 = Boolean.TRUE;
                        } catch (ParseException e) {
                            System.out.println("The date does not match the format.");
                        }
                    } while (!ok1);

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    library.addAuthor(authorName, birthdate, email);
                    break;

                case 15:
                    System.out.print("Author name: ");
                    authorName = scanner.nextLine();

                    library.removeAuthor(authorName);
                    break;

                case 16:
                    System.out.print("Author name: ");
                    authorName = scanner.nextLine();

                    library.listAllBooksFromAuthor(authorName);
                    break;

                case 17:
                    System.out.print("Author name: ");
                    authorName = scanner.nextLine();

                    System.out.println("Available types:");
                    System.out.println("1. Physical book");
                    System.out.println("2. Ebook");
                    typeOption = scanner.nextInt();

                    System.out.print("Title: ");
                    title = scanner.nextLine();

                    System.out.print("Number of pages: ");
                    noPages = scanner.nextInt();

                    section = null;
                    ok = Boolean.FALSE;
                    do {
                        System.out.print("Existing section(1) or new section(2)? ");
                        int typeSection = scanner.nextInt();

                        System.out.print("Section name: ");
                        sectionName = scanner.nextLine();
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
                        String publishingHouseName = scanner.nextLine();
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
                                    establishmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
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
                        String format = scanner.nextLine();

                        library.addBookFromAuthor(authorName, title, noPages, section, publishingHouse, format);
                    }
                    break;

                case 18:
                    library.listAllReaders();
                    break;

                case 19:
                    System.out.print("Reader name: ");
                    String readerName = scanner.nextLine();

                    birthdate = null;
                    ok1 = Boolean.FALSE;
                    do {
                        System.out.print("Birthdate (format dd/mm/yyyy): ");
                        try {
                            birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
                            ok1 = Boolean.TRUE;
                        } catch (ParseException e) {
                            System.out.println("The date does not match the format.");
                        }
                    } while (!ok1);

                    System.out.print("Email: ");
                    email = scanner.nextLine();

                    System.out.print("Address: ");
                    String address = scanner.nextLine();

                    library.addReader(readerName, birthdate, email, address);
                    break;

                case 20:
                    System.out.print("Reader name: ");
                    readerName = scanner.nextLine();

                    library.removeReader(readerName);
                    break;

                case 21:
                    System.out.print("Reader name: ");
                    readerName = scanner.nextLine();

                    library.listBooksLent(readerName);
                    break;

                case 22:
                    System.out.print("Reader name: ");
                    readerName = scanner.nextLine();

                    System.out.print("Book title: ");
                    title = scanner.nextLine();

                    library.lendBook(readerName, title);
                    break;

                case 23:
                    System.out.print("Reader name: ");
                    readerName = scanner.nextLine();

                    System.out.print("Book title: ");
                    title = scanner.nextLine();

                    library.returnBook(readerName, title);
                    break;

                case 24:
                    library.listAllPublishingHouses();
                    break;

                case 25:
                    System.out.print("Publishing house name: ");
                    String publishingHouseName = scanner.nextLine();

                    Date establishmentDate = null;
                    ok1 = Boolean.FALSE;
                    do {
                        System.out.print("Establishment date (format dd/mm/yyyy): ");
                        try {
                            establishmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
                            ok1 = Boolean.TRUE;
                        } catch (ParseException e) {
                            System.out.println("The date does not match the format.");
                        }
                    } while (!ok1);

                    library.addPublishingHouse(publishingHouseName, establishmentDate);
                    break;

                case 26:
                    System.out.print("Publishing house name: ");
                    publishingHouseName = scanner.nextLine();

                    library.removePublishingHouse(publishingHouseName);
                    break;

                case 27:
                    System.out.print("Publishing house name: ");
                    publishingHouseName = scanner.nextLine();

                    library.listBooksFromPublishingHouse(publishingHouseName);
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
