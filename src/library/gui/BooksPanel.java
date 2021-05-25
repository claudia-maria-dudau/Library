package library.gui;

import library.Library;
import library.books.Book;
import library.books.PublishingHouse;
import library.books.Section;
import library.people.Author;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BooksPanel extends JPanel {
    private final Library library = Library.getInstance();
    private List<Book> currentBooks = new ArrayList<>(library.getBooks());
    private static JList booksJList;

    public BooksPanel() {
        this.setLayout(new BorderLayout());

        // LIST BOOKS PANEL
        JSplitPane listBooks = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // listing books
        booksJList = this.createBooksJList(currentBooks);
        JScrollPane listOfBooks = new JScrollPane(booksJList);

        listBooks.setLeftComponent(listOfBooks);

        // add book
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        // values for combo box
        String[] formatValues = {"physical", "pdf", "epub", "mobi", "azw", "iba"};

        String[] sectionValues = new String[library.getSections().size()];
        int i = 0;
        for (Section section : library.getSections()){
            sectionValues[i] = section.getName();
            i++;
        }

        String[] authorValues = new String[library.getAuthors().size()];
        i = 0;
        for (Author author : library.getAuthors()){
            authorValues[i] = author.getName();
            i++;
        }

        String[] publishingHouseValues = new String[library.getPublishingHouses().size()];
        i = 0;
        for (PublishingHouse publishingHouse : library.getPublishingHouses()){
            publishingHouseValues[i] = publishingHouse.getName();
            i++;
        }

        // title
        JLabel titleLabel = new JLabel("Title");
        JTextField titleField = new JTextField(20);
        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        titlePanel.add(titleField);
        addPanel.add(titlePanel);

        // number of pages
        JLabel noPagesLabel = new JLabel("Number of pages");
        JTextField noPagesField = new JTextField(20);
        JPanel noPagesPanel = new JPanel();
        noPagesPanel.add(noPagesLabel);
        noPagesPanel.add(noPagesField);
        addPanel.add(noPagesPanel);

        // number of copies
        JLabel noCopiesLabel = new JLabel("Number of copies");
        JTextField noCopiesField = new JTextField(20);
        JPanel noCopiesPanel = new JPanel();
        noCopiesPanel.add(noCopiesLabel);
        noCopiesPanel.add(noCopiesField);
        addPanel.add(noCopiesPanel);

        // publish date
        JLabel publishDateLabel = new JLabel("Publish date");
        JTextField publishDateField = new JTextField("dd/mm/yyyy", 20);
        JPanel publishDatePanel = new JPanel();
        publishDatePanel.add(publishDateLabel);
        publishDatePanel.add(publishDateField);
        addPanel.add(publishDatePanel);

        // format
        JLabel formatLabel = new JLabel("Format");
        JComboBox formatField = new JComboBox(formatValues);
        JPanel formatPanel = new JPanel();
        formatPanel.add(formatLabel);
        formatPanel.add(formatField);
        addPanel.add(formatPanel);

        // section
        JLabel sectionLabel = new JLabel("Section");
        JComboBox sectionField = new JComboBox(sectionValues);
        JPanel sectionPanel = new JPanel();
        sectionPanel.add(sectionLabel);
        sectionPanel.add(sectionField);
        addPanel.add(sectionPanel);

        // author
        JLabel authorLabel = new JLabel("Author");
        JComboBox authorField = new JComboBox(authorValues);
        JPanel authorPanel = new JPanel();
        authorPanel.add(authorLabel);
        authorPanel.add(authorField);
        addPanel.add(authorPanel);

        // publishing house
        JLabel publishingHouseLabel = new JLabel("Publishing House");
        JComboBox publishingHouseField = new JComboBox(publishingHouseValues);
        JPanel publishingHousePanel = new JPanel();
        publishDatePanel.add(publishDateLabel);
        publishDatePanel.add(publishDateField);
        addPanel.add(publishingHousePanel);

        // button
        JButton addBook = new JButton("Add book");
        addBook.addActionListener(e -> {
            String title = titleField.getText();
            int noPages = Integer.parseInt(noPagesField.getText());
            java.sql.Date publishDate = null;
            try {
                publishDate = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(publishDateField.getText()).getTime());
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            Section section = library.getSection(String.valueOf(sectionField.getSelectedItem()));
            Author author = library.getAuthor(String.valueOf(authorField.getSelectedItem()));
            PublishingHouse publishingHouse = library.getPublishingHouse(String.valueOf(publishingHouseField.getSelectedItem()));
            String format = String.valueOf(formatField.getSelectedItem());

            if (format.equalsIgnoreCase("physical")){
                int noCopies = Integer.parseInt(noCopiesField.getText());
                library.addBook(title, noPages, publishDate, section, author, publishingHouse, noCopies);
            } else {
                library.addBook(title, noPages, publishDate, section, author, publishingHouse, format);
            }

            // updating books list
            currentBooks = new ArrayList<>(library.getBooks());
            BooksPanel.booksJList = createBooksJList(currentBooks);
            listBooks.setLeftComponent(booksJList);
        });

        // reset button
        JButton reset = new JButton("Reset");
        reset.addActionListener(e -> {
            titleField.setText("");
            noPagesField.setText("");
            noCopiesField.setText("");
            publishDateField.setText("dd/mm/yyyy");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBook);
        buttonPanel.add(reset);
        addPanel.add(buttonPanel);

        listBooks.setRightComponent(addPanel);


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
            if (BooksPanel.booksJList.getSelectedIndex() != -1){
                // listing the details about the selected book
                int selectedBookId = Integer.parseInt(BooksPanel.booksJList.getSelectedValue().toString().split("\\) ")[0]);
                List<Book> selectedBooks = library.getBooks().stream().filter(b -> b.getId() == selectedBookId).collect(Collectors.toList());
                Book book = selectedBooks.get(0);

                // details about book
                String bookString = "<p><b>Title:</b> " + book.getTitle() + "</p>" +
                        "<p><b>Type:</b> " + ((book.getType().equalsIgnoreCase("pbook") ? "physical" : "e-book")) + "</p>" +
                        "<p><b>Number of pages:</b> " + ((book.getNoPages() == Integer.MAX_VALUE) ? "-" : book.getNoPages()) + "</p>" +
                        "<p><b>Number of copies:</b> " + book.getNoCopies() + "</p>" +
                        "<p><b>Publishing date:</b> " + new SimpleDateFormat("dd MMMM yyyy").format(book.getPublishDate()) + "</p>" +
                        "<p><b>Format:</b> " + book.getFormat() + "</p>" +
                        "<p><b>Section:</b> " + book.getSection().getName() + "</p>" +
                        "<p><b>Author:</b> " + book.getAuthor().getName() + "</p>" +
                        "<p><b>Publishing house:</b> " + book.getPublishingHouse().getName() + "</p>";
            }
        });

        // edit button
        JButton edit = new JButton("Edit");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BooksPanel.booksJList.getSelectedIndex() != -1){
                    // editing the selected book
                    int selectedBookId = Integer.parseInt(BooksPanel.booksJList.getSelectedValue().toString().split("\\) ")[0]);
                    List<Book> selectedBooks = library.getBooks().stream().filter(b -> b.getId() == selectedBookId).collect(Collectors.toList());
                    Book book = selectedBooks.get(0);
                }
            }
        });

        // delete button
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            if (BooksPanel.booksJList.getSelectedIndex() != -1){
                // deleting the selected book
                int selectedBookId = Integer.parseInt(BooksPanel.booksJList.getSelectedValue().toString().split("\\) ")[0]);
                List<Book> selectedBooks = library.getBooks().stream().filter(b -> b.getId() == selectedBookId).collect(Collectors.toList());
                Book book = selectedBooks.get(0);

                library.removeBook(book);
                currentBooks = new ArrayList<>(library.getBooks());
                BooksPanel.booksJList = createBooksJList(currentBooks);
                listBooks.setLeftComponent(new JScrollPane(booksJList));
            }
        });

        crud.add(show);
        crud.add(edit);
        crud.add(delete);

        this.add(searchBooks, BorderLayout.NORTH);
        this.add(listBooks, BorderLayout.CENTER);
        this.add(crud, BorderLayout.SOUTH);
    }

    public JList createBooksJList(List<Book> books) {
        String[] booksString = new String[books.size()];
        int i = 0;
        for (Book book : books) {
            booksString[i] = book.getId() + ") " + book.getTitle() + " - " + book.getAuthor().getName();
            i++;
        }
        JList list = new JList(booksString);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return  list;
    }
}
