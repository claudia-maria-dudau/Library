package library.gui;

import library.Library;
import library.books.Book;
import library.books.PublishingHouse;
import library.books.Section;
import library.database.DB;
import library.people.Author;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BooksPanel extends JPanel {
    private final Library library = Library.getInstance();
    private final DB db = DB.getInstance();
    private List<Book> currentBooks = new ArrayList<>(library.getBooks());
    private static JList booksJList;
    private static  JSplitPane listBooks = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    public BooksPanel() {
        this.setLayout(new BorderLayout());

        // LIST BOOKS PANEL
        // JSplitPane listBooks = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // listing books
        booksJList = createBooksJList(currentBooks);
        JScrollPane listOfBooks = new JScrollPane(booksJList);

        listBooks.setLeftComponent(listOfBooks);

        // add book
        this.setAddBookPanel();


        // SEARCH BOOKS PANEL
        JPanel searchBooks = new JPanel();

        // search field
        JTextField searchField = new JTextField(30);

        // search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String searchValue = searchField.getText();
            if (!searchValue.equalsIgnoreCase("")) {
                // the search field is not empty
                // => listing the books which contain in their title the searched string
                List<Book> searchedBooks = currentBooks.stream().filter(b -> b.getTitle().toLowerCase().contains(searchValue.toLowerCase())).collect(Collectors.toList());
                BooksPanel.booksJList = createBooksJList(searchedBooks);
            } else {
                // the search field is empty
                // => listing all the books in tge library
                BooksPanel.booksJList = createBooksJList(currentBooks);
            }

            listBooks.setLeftComponent(new JScrollPane(booksJList));
        });

        // filter buttons
        // all button
        JButton allButton = new JButton("All");
        allButton.setEnabled(false);

        // pbook button
        JButton pbookButton = new JButton("Physical");

        // pbook button
        JButton ebookButton = new JButton("E-book");

        allButton.addActionListener(e -> {
            // listing all the books in the library
            currentBooks = new ArrayList<>(library.getBooks());
            booksJList = createBooksJList(currentBooks);
            listBooks.setLeftComponent(new JScrollPane(booksJList));

            // setting the buttons availability
            allButton.setEnabled(false);
            pbookButton.setEnabled(true);
            ebookButton.setEnabled(true);
        });

        pbookButton.addActionListener(e -> {
            // listing only the physical books
            currentBooks = library.getBooks().stream().filter(b -> b.getType().equalsIgnoreCase("pbook")).collect(Collectors.toList());
            booksJList = createBooksJList(currentBooks);
            listBooks.setLeftComponent(new JScrollPane(booksJList));

            // setting the buttons availability
            allButton.setEnabled(true);
            pbookButton.setEnabled(false);
            ebookButton.setEnabled(true);
        });

        ebookButton.addActionListener(e -> {
            // listing only the e-books
            currentBooks = library.getBooks().stream().filter(b -> b.getType().equalsIgnoreCase("ebook")).collect(Collectors.toList());
            booksJList = createBooksJList(currentBooks);
            listBooks.setLeftComponent(new JScrollPane(booksJList));

            // setting the buttons availability
            allButton.setEnabled(true);
            pbookButton.setEnabled(true);
            ebookButton.setEnabled(false);
        });

        searchBooks.add(searchField);
        searchBooks.add(searchButton);
        searchBooks.add(allButton);
        searchBooks.add(pbookButton);
        searchBooks.add(ebookButton);

        // CRUD BUTTONS
        JPanel crud = new JPanel();

        // show button
        JButton show = new JButton("Show");
        show.addActionListener(e -> {
            if (BooksPanel.booksJList.getSelectedIndex() != -1) {
                // listing the details about the selected book
                int selectedBookId = Integer.parseInt(BooksPanel.booksJList.getSelectedValue().toString().split("\\) ")[0]);
                Book book = db.getBook(selectedBookId);

                // details about book
                String bookString = "Title: " + book.getTitle() +
                        "\nType: " + ((book.getType().equalsIgnoreCase("pbook") ? "physical" : "e-book")) +
                        "\nNumber of pages: " + book.getNoPages() +
                        "\nNumber of copies: " + ((book.getNoCopies() == Integer.MAX_VALUE) ? "-" : book.getNoCopies()) +
                        "\nPublishing date: " + new SimpleDateFormat("dd MMMM yyyy").format(book.getPublishDate()) +
                        "\nFormat: " + book.getFormat() +
                        "\nSection: " + book.getSection().getName() +
                        "\nAuthor: " + book.getAuthor().getName() +
                        "\nPublishing house: " + book.getPublishingHouse().getName();

                JOptionPane.showMessageDialog(listBooks, bookString, book.getTitle(), JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // edit button
        JButton edit = new JButton("Edit");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BooksPanel.booksJList.getSelectedIndex() != -1) {
                    // editing the selected book
                    int selectedBookId = Integer.parseInt(BooksPanel.booksJList.getSelectedValue().toString().split("\\) ")[0]);
                    Book book = db.getBook(selectedBookId);

                    // edit panel
                    JPanel editPanel = new JPanel();
                    editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

                    // values for combo box
                    String[] formatValues = {"physical", "pdf", "epub", "mobi", "azw", "iba"};

                    String[] sectionValues = new String[library.getSections().size()];
                    int i = 0;
                    for (Section section : library.getSections()) {
                        sectionValues[i] = section.getName();
                        i++;
                    }

                    String[] authorValues = new String[library.getAuthors().size()];
                    i = 0;
                    for (Author author : library.getAuthors()) {
                        authorValues[i] = author.getName();
                        i++;
                    }

                    String[] publishingHouseValues = new String[library.getPublishingHouses().size()];
                    i = 0;
                    for (PublishingHouse publishingHouse : library.getPublishingHouses()) {
                        publishingHouseValues[i] = publishingHouse.getName();
                        i++;
                    }

                    // title
                    JLabel titleLabel = new JLabel("Title*");
                    JTextField titleField = new JTextField(book.getTitle(), 20);
                    JPanel titlePanel = new JPanel();
                    titlePanel.add(titleLabel);
                    titlePanel.add(titleField);
                    editPanel.add(titlePanel);

                    // number of pages
                    JLabel noPagesLabel = new JLabel("Number of pages*");
                    JTextField noPagesField = new JTextField(String.valueOf(book.getNoPages()), 20);
                    JPanel noPagesPanel = new JPanel();
                    noPagesPanel.add(noPagesLabel);
                    noPagesPanel.add(noPagesField);
                    editPanel.add(noPagesPanel);

                    // number of copies
                    JLabel noCopiesLabel = new JLabel("Number of copies");
                    JTextField noCopiesField = new JTextField(book.getType().equalsIgnoreCase("pbook") ? String.valueOf(book.getNoCopies()) : "-", 20);
                    JPanel noCopiesPanel = new JPanel();
                    noCopiesPanel.add(noCopiesLabel);
                    noCopiesPanel.add(noCopiesField);
                    editPanel.add(noCopiesPanel);

                    // publish date
                    JLabel publishDateLabel = new JLabel("Publish date*");
                    JTextField publishDateField = new JTextField(new SimpleDateFormat("dd/mm/yyyy").format(book.getPublishDate()), 20);
                    JPanel publishDatePanel = new JPanel();
                    publishDatePanel.add(publishDateLabel);
                    publishDatePanel.add(publishDateField);
                    editPanel.add(publishDatePanel);

                    // format
                    JLabel formatLabel = new JLabel("Format*");
                    JComboBox formatField = new JComboBox(formatValues);
                    formatField.setSelectedItem(book.getFormat());
                    JPanel formatPanel = new JPanel();
                    formatPanel.add(formatLabel);
                    formatPanel.add(formatField);
                    editPanel.add(formatPanel);

                    // section
                    JLabel sectionLabel = new JLabel("Section*");
                    JComboBox sectionField = new JComboBox(sectionValues);
                    sectionField.setSelectedItem(book.getSection().getName());
                    JPanel sectionPanel = new JPanel();
                    sectionPanel.add(sectionLabel);
                    sectionPanel.add(sectionField);
                    editPanel.add(sectionPanel);

                    // author
                    JLabel authorLabel = new JLabel("Author*");
                    JComboBox authorField = new JComboBox(authorValues);
                    authorField.setSelectedItem(book.getAuthor().getName());
                    JPanel authorPanel = new JPanel();
                    authorPanel.add(authorLabel);
                    authorPanel.add(authorField);
                    editPanel.add(authorPanel);

                    // publishing house
                    JLabel publishingHouseLabel = new JLabel("Publishing House*");
                    JComboBox publishingHouseField = new JComboBox(publishingHouseValues);
                    publishingHouseField.setSelectedItem(book.getPublishingHouse().getName());
                    JPanel publishingHousePanel = new JPanel();
                    publishingHousePanel.add(publishingHouseLabel);
                    publishingHousePanel.add(publishingHouseField);
                    editPanel.add(publishingHousePanel);

                    int result = JOptionPane.showConfirmDialog(listBooks, editPanel, "Edit book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        if (!titleField.getText().equalsIgnoreCase("") && !noPagesField.getText().equalsIgnoreCase("") && !publishDateField.getText().equalsIgnoreCase("dd/mm/yyyy")) {
                            String title = titleField.getText();
                            int noPages = Integer.parseInt(noPagesField.getText());
                            java.sql.Date publishDate = null;
                            try {
                                publishDate = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(publishDateField.getText()).getTime());
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                            Section section = db.getSection(String.valueOf(sectionField.getSelectedItem()));
                            Author author = db.getAuthor(String.valueOf(authorField.getSelectedItem()));
                            PublishingHouse publishingHouse = db.getPublishingHouse(String.valueOf(publishingHouseField.getSelectedItem()));
                            String format = String.valueOf(formatField.getSelectedItem());

                            if (format.equalsIgnoreCase("physical")) {
                                int noCopies = noCopiesField.getText().equals("") ? 1 : Integer.parseInt(noCopiesField.getText());
                                library.updateBook(book.getId(), title, noPages, publishDate, section, author, publishingHouse, noCopies);
                            } else {
                                library.updateBook(book.getId(), title, noPages, publishDate, section, author, publishingHouse, format);
                            }

                            // updating books list
                            currentBooks = new ArrayList<>(library.getBooks());
                            BooksPanel.booksJList = createBooksJList(currentBooks);
                            listBooks.setLeftComponent(booksJList);
                        } else {
                            JOptionPane.showMessageDialog(editPanel, "Please complete all the required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });

        // delete button
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            if (BooksPanel.booksJList.getSelectedIndex() != -1) {
                // deleting the selected book
                int selectedBookId = Integer.parseInt(BooksPanel.booksJList.getSelectedValue().toString().split("\\) ")[0]);
                Book book = db.getBook(selectedBookId);

                library.removeBook(book);
                currentBooks = new ArrayList<>(library.getBooks());
                BooksPanel.booksJList = createBooksJList(currentBooks);
                listBooks.setLeftComponent(new JScrollPane(booksJList));
            }
        });

        // add copy button
        JButton addCopy = new JButton("Add copy");
        addCopy.addActionListener(e -> {
            if (BooksPanel.booksJList.getSelectedIndex() != -1) {
                // adding copies for the selected book
                int selectedBookId = Integer.parseInt(BooksPanel.booksJList.getSelectedValue().toString().split("\\) ")[0]);
                Book book = db.getBook(selectedBookId);

                if (book.getType().equalsIgnoreCase("pbook")) {
                    // add copy panel
                    JPanel addCopyPanel = new JPanel();

                    // number of copies field
                    JTextField addCopyField = new JTextField("1", 20);
                    addCopyPanel.add(addCopyField);

                    int result = JOptionPane.showConfirmDialog(listBooks, addCopyPanel, "Add copies", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        int noCopies = Integer.parseInt(addCopyField.getText());
                        library.addCopies(book, noCopies);
                    }
                } else {
                    JOptionPane.showMessageDialog(listBooks, "Can only add copies to physical books!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // lost copy button
        JButton lostCopy = new JButton("Lost copy");
        lostCopy.addActionListener(e -> {
            if (BooksPanel.booksJList.getSelectedIndex() != -1) {
                // removing copies for the selected book
                int selectedBookId = Integer.parseInt(BooksPanel.booksJList.getSelectedValue().toString().split("\\) ")[0]);
                Book book = db.getBook(selectedBookId);

                if (book.getType().equalsIgnoreCase("pbook")) {
                    // lost copy panel
                    JPanel lostCopyPanel = new JPanel();

                    // number of copies field
                    JTextField lostCopyField = new JTextField("1", 20);
                    lostCopyPanel.add(lostCopyField);

                    int result = JOptionPane.showConfirmDialog(listBooks, lostCopyPanel, "Remove copies", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        int noCopies = Integer.parseInt(lostCopyField.getText());
                        library.addCopies(book, noCopies);
                    }
                } else {
                    JOptionPane.showMessageDialog(listBooks, "Can only remove copies from physical books!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        crud.add(show);
        crud.add(edit);
        crud.add(delete);
        crud.add(addCopy);
        crud.add(lostCopy);

        this.add(searchBooks, BorderLayout.NORTH);
        this.add(listBooks, BorderLayout.CENTER);
        this.add(crud, BorderLayout.SOUTH);
    }

    public static void setAddBookPanel() {
        listBooks.setRightComponent(new AddBookPanel(null, null));
    }

    public static void setCurrentBooks(List<Book> currentBooks) {
        currentBooks = currentBooks;
        BooksPanel.booksJList = createBooksJList(currentBooks);
        listBooks.setLeftComponent(new JScrollPane(booksJList));
    }

    public static JList createBooksJList(List<Book> books) {
        String[] booksString = new String[books.size()];
        int i = 0;
        for (Book book : books) {
            booksString[i] = book.getId() + ") " + book.getTitle() + " - " + book.getAuthor().getName();
            i++;
        }
        JList list = new JList(booksString);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }
}
