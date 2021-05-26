package library.gui;

import library.Library;
import library.books.Book;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class GUI {
    private static GUI instance;
    private JFrame principal;
    private Library library = Library.getInstance();

    private GUI(){
        // principal frame
        this.principal = new JFrame("Library");
        principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        principal.setBounds(100, 100, 700, 500);

        // tabbed panel - principal panel
        JTabbedPane content = new JTabbedPane();

        // PANEL 1 - BOOKS
        JPanel books = new BooksPanel();
        content.addTab("Books", books);

        // PANEL 2 - BOOKS
        JPanel sections = new SectionsPanel();
        content.addTab("Sections", sections);


        this.principal.setContentPane(content);
        this.principal.setVisible(true);
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
