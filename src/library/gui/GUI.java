package library.gui;

import javax.swing.*;

public class GUI {
    private static GUI instance;
    private final JFrame principal;
    private static final JFrame addBook = new JFrame("Add book");

    private GUI() {
        // principal frame
        this.principal = new JFrame("Library");
        principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        principal.setBounds(100, 100, 700, 500);

        // tabbed panel - principal panel
        JTabbedPane content = new JTabbedPane();

        // PANEL 1 - BOOKS
        BooksPanel books = new BooksPanel();
        content.addTab("Books", books);

        // PANEL 2 - SECTIONS
        SectionsPanel sections = new SectionsPanel();
        content.addTab("Sections", sections);

        // PANEL 3 - PUBLISHING HOUSES
        PublishingHousesPanel publishingHouses = new PublishingHousesPanel();
        content.addTab("Publishing Houses", publishingHouses);

        // PANEL 4 - AUTHORS
        AuthorsPanel authors = new AuthorsPanel();
        content.addTab("Authors", authors);

        // PANEL 5 - READERS
        ReadersPanel readers = new ReadersPanel();
        content.addTab("Readers", readers);

        this.principal.setContentPane(content);
        this.principal.setVisible(true);
    }

    public static void setAddBook(AddBookPanel panel){
        addBook.setContentPane(panel);
        addBook.setBounds(100, 100, 700, 500);
        addBook.setVisible(true);
    }

    public static void hideAddBook(){
        addBook.setVisible(false);
    }

    public static GUI getInstance() {
        if (instance == null){
            synchronized (GUI.class) {
                if (instance == null) {
                    instance = new GUI();
                }
            }
        }

        return instance;
    }
}
