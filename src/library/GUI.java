package library;

import library.books.Book;
import library.books.Pbook;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class GUI {
    private static GUI instance;
    private JFrame principal;
    private Library library = Library.getInstance();

    private GUI(){
        // principal frame
        this.principal = new JFrame("Library");
        principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        principal.setBounds(100, 100, 700, 400);

        // tabbed panel - principal panel
        JTabbedPane content = new JTabbedPane();

        // PANEL 1 - BOOKS
        JSplitPane books = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // list panel books
        JSplitPane listBooks = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // details about one book
        JPanel detailsBook = new JPanel();
        detailsBook.add(new JPanel());

        // listing books
        String[] booksString = new String[this.library.getBooks().size()];
        int i = 0;
        for (Book book : this.library.getBooks()){
            booksString[i] = book.getTitle() + " - " + book.getAuthor().getName();
            i ++;
        }
        JList booksJList = new JList(booksString);
        JScrollPane listOfBooks = new JScrollPane(booksJList);

        listBooks.add(listOfBooks);
        listBooks.add(detailsBook);

        System.out.println(listBooks.getComponents()[1]);
//        listBooks.getComponents()[1].getAccessibleContext().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                int selectedIndex = booksJList.getAnchorSelectionIndex();
//                System.out.println(listBooks.getComponents()[1].getAccessibleContext());
//                String selectedBookTitle = booksString[selectedIndex].split(" - ")[0];
//                List<Book> selectedBooks = library.getBooks().stream().filter(b -> b.getTitle().equalsIgnoreCase(selectedBookTitle)).collect(Collectors.toList());
//                String[] bookString = new String[selectedBooks.size()];
//                int i = 0;
//                for (Book book : selectedBooks) {
//                    bookString[i] = "Title: " + book.getTitle() +
//                            "\nType: " + book.getType() +
//                            "\nNumber of pages: " + book.getNoPages() +
//                            "\nNumber of copies: " + book.getNoCopies() +
//                            "\nPublishing date: " + book.getPublishDate() +
//                            "\nSection: " + book.getSection().getName() +
//                            "\nAuthor: " + book.getAuthor().getName() +
//                            "\nPublishing house: " + book.getPublishingHouse().getName();
//                    i++;
//                }
//                detailsBook.remove(0);
//                detailsBook.add(new JScrollPane(new JTextArea(bookString[0])));
//            }
//        });

        // search panel books
        JPanel searchBooks = new JPanel();

        // search field
        JTextField searchField = new JTextField(30);

        // search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchValue = searchField.getText();
                if (!searchValue.equalsIgnoreCase("")) {
                    List<Book> searchedBooks = library.getBooks().stream().filter(b -> b.getTitle().toLowerCase().contains(searchValue.toLowerCase())).collect(Collectors.toList());
                    String[] booksString = new String[searchedBooks.size()];
                    int i = 0;
                    for (Book book : searchedBooks) {
                        booksString[i] = book.getTitle() + " - " + book.getAuthor().getName();
                        i++;
                    }
                    listBooks.remove(1);
                    listBooks.remove(1);
                    listBooks.add(new JScrollPane(new JList(booksString)));
                    listBooks.add(detailsBook);
                } else {
                    listBooks.remove(1);
                    listBooks.remove(1);
                    listBooks.add(listOfBooks);
                    listBooks.add(detailsBook);
                }

                detailsBook.remove(0);
                detailsBook.add(new JPanel());
            }
        });

        searchBooks.add(searchField);
        searchBooks.add(searchButton);

        books.add(searchBooks);
        books.add(listBooks);

        content.addTab("Books", books);

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
