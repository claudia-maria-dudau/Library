package library.gui;

import library.Library;
import library.books.Book;
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

public class AuthorsPanel extends JPanel {
    private final Library library = Library.getInstance();
    private final DB db = DB.getInstance();
    private List<Author> currentAuthors = new ArrayList(library.getAuthors());
    private static JList authorsJList;

    public AuthorsPanel() {
        this.setLayout(new BorderLayout());

        // LIST AUTHORS PANEL
        JSplitPane listAuthors = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // listing authors
        authorsJList = this.createAuthorsJList(currentAuthors);
        JScrollPane listOfAuthors = new JScrollPane(authorsJList);

        listAuthors.setLeftComponent(listOfAuthors);

        // add authors
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        // name
        JLabel nameLabel = new JLabel("Name*");
        JTextField nameField = new JTextField(20);
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        addPanel.add(namePanel);

        // birthdate
        JLabel birthdateLabel = new JLabel("Birthdate*");
        JTextField birthdateField = new JTextField("dd/mm/yyyy", 20);
        JPanel birthdatePanel = new JPanel();
        birthdatePanel.add(birthdateLabel);
        birthdatePanel.add(birthdateField);
        addPanel.add(birthdatePanel);

        // email
        JLabel emailLabel = new JLabel("Email");
        JTextField emailField = new JTextField(20);
        JPanel emailPanel = new JPanel();
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        addPanel.add(emailPanel);


        // add button
        JButton addAuthor = new JButton("Add author");
        addAuthor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nameField.getText().equalsIgnoreCase("") && !birthdateField.getText().equalsIgnoreCase("dd/mm/yyyy")) {
                    String name = nameField.getText();
                    java.sql.Date birthdate = null;
                    try {
                        birthdate = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(birthdateField.getText()).getTime());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    String email = emailField.getText().equalsIgnoreCase("") ? null : emailField.getText();

                    library.addAuthor(name, birthdate, email);

                    // updating authors list
                    currentAuthors = new ArrayList<>(library.getAuthors());
                    AuthorsPanel.authorsJList = createAuthorsJList(currentAuthors);
                    listAuthors.setLeftComponent(authorsJList);

                    BooksPanel.setAddBookPanel();
                } else {
                    JOptionPane.showMessageDialog(addPanel, "Please complete all the required fields!", "Warning", JOptionPane.WARNING_MESSAGE);

                }
            }
        });

        // reset button
        JButton reset = new JButton("Reset");
        reset.addActionListener(e -> {
            nameField.setText("");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addAuthor);
        buttonPanel.add(reset);
        addPanel.add(buttonPanel);

        // message
        JLabel message = new JLabel("The fields marked with * are mandatory!");
        JPanel messagePanel = new JPanel();
        messagePanel.add(message);
        addPanel.add(messagePanel);

        listAuthors.setRightComponent(addPanel);


        // SEARCH AUTHORS PANEL
        JPanel searchAuthors = new JPanel();

        // search field
        JTextField searchField = new JTextField(30);

        // search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String searchValue = searchField.getText();
            if (!searchValue.equalsIgnoreCase("")) {
                // the search field is not empty
                // => listing the authors which contain in their name the searched string
                List<Author> searchedAuthors = currentAuthors.stream().filter(b -> b.getName().toLowerCase().contains(searchValue.toLowerCase())).collect(Collectors.toList());
                AuthorsPanel.authorsJList = createAuthorsJList(searchedAuthors);
            } else {
                // the search field is empty
                // => listing all the authors in tge library
                AuthorsPanel.authorsJList = createAuthorsJList(currentAuthors);
            }

            listAuthors.setLeftComponent(new JScrollPane(authorsJList));
        });

        searchAuthors.add(searchField);
        searchAuthors.add(searchButton);


        // CRUD BUTTONS
        JPanel crud = new JPanel();

        // show button
        JButton show = new JButton("Show");
        show.addActionListener(e -> {
            if (AuthorsPanel.authorsJList.getSelectedIndex() != -1) {
                // listing the details about the selected author
                int selectedAuthorId = Integer.parseInt(AuthorsPanel.authorsJList.getSelectedValue().toString().split("\\) ")[0]);
                Author author = db.getAuthor(selectedAuthorId);

                // details about author
                String authorString = "Name: " + author.getName() +
                        "\nBirthdate: " + new SimpleDateFormat("dd MMMM yyyy").format(author.getBirthDate()) +
                        "\nEmail: " + author.getMail() +
                        "\nNumber of books written by the author: " + author.getNoBooksWritten() +
                        "\nBooks written by the author:\n";

                for (Book book : db.getBooksFromAuthor(author.getId())) {
                    authorString += book.getTitle() + "\n";
                }

                JOptionPane.showMessageDialog(listAuthors, authorString, author.getName(), JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // edit button
        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
            if (AuthorsPanel.authorsJList.getSelectedIndex() != -1) {
                // editing the selected author
                int selectedAuthorId = Integer.parseInt(AuthorsPanel.authorsJList.getSelectedValue().toString().split("\\) ")[0]);
                Author author = db.getAuthor(selectedAuthorId);

                // edit panel
                JPanel editPanel = new JPanel();
                editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

                // name
                JLabel nameLabel1 = new JLabel("Title*");
                JTextField nameField1 = new JTextField(author.getName(), 20);
                JPanel namePanel1 = new JPanel();
                namePanel1.add(nameLabel1);
                namePanel1.add(nameField1);
                editPanel.add(namePanel1);

                // birthdate
                JLabel birthdateLabel1 = new JLabel("Birthdate*");
                JTextField birthdateField1 = new JTextField(new SimpleDateFormat("dd/mm/yyyy").format(author.getBirthDate()), 20);
                JPanel birthdatePanel1 = new JPanel();
                birthdatePanel1.add(birthdateLabel1);
                birthdatePanel1.add(birthdateField1);
                editPanel.add(birthdatePanel1);

                // email
                JLabel emailLabel1 = new JLabel("Email");
                JTextField emailField1 = new JTextField(author.getMail(), 20);
                JPanel emailPanel1 = new JPanel();
                emailPanel1.add(emailLabel1);
                emailPanel1.add(emailField1);
                editPanel.add(emailPanel1);

                int result = JOptionPane.showConfirmDialog(listAuthors, editPanel, "Edit author", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (!nameField1.getText().equalsIgnoreCase("") && !birthdateField1.getText().equalsIgnoreCase("dd/mm/yyyy")) {
                        String name = nameField1.getText();
                        java.sql.Date birthdate = null;
                        try {
                            birthdate = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(birthdateField1.getText()).getTime());
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        String email = emailField1.getText().equalsIgnoreCase("") ? null : emailField1.getText();

                        library.updateAuthor(author.getId(), name, birthdate, email, author.getNoBooksWritten());

                        // updating authors list
                        currentAuthors = new ArrayList<>(library.getAuthors());
                        AuthorsPanel.authorsJList = createAuthorsJList(currentAuthors);
                        listAuthors.setLeftComponent(authorsJList);

                        BooksPanel.setAddBookPanel();
                    } else {
                        JOptionPane.showMessageDialog(editPanel, "Please complete all the required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        // delete button
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            if (AuthorsPanel.authorsJList.getSelectedIndex() != -1) {
                // deleting the selected author
                int selectedAuthorId = Integer.parseInt(AuthorsPanel.authorsJList.getSelectedValue().toString().split("\\) ")[0]);
                Author author = db.getAuthor(selectedAuthorId);

                library.removeAuthor(new Author(author.getId(), author.getName(), author.getBirthDate(), author.getMail(), author.getNoBooksWritten()));
                currentAuthors = new ArrayList<>(library.getAuthors());
                AuthorsPanel.authorsJList = createAuthorsJList(currentAuthors);
                listAuthors.setLeftComponent(new JScrollPane(authorsJList));

                // updating books list
                BooksPanel.setCurrentBooks(new ArrayList<>(library.getBooks()));
                BooksPanel.setAddBookPanel();
            }
        });

        // add book from author
        JButton addBook = new JButton("Add book");
        addBook.addActionListener(e -> {
            if (AuthorsPanel.authorsJList.getSelectedIndex() != -1) {
                // adding a book from the selected author
                int selectedAuthorId = Integer.parseInt(AuthorsPanel.authorsJList.getSelectedValue().toString().split("\\) ")[0]);
                Author author = db.getAuthor(selectedAuthorId);

                GUI.setAddBook(new AddBookPanel(null, author, null));
            }
        });

        // remove book from author
        JButton removeBook = new JButton("Remove book");
        removeBook.addActionListener(e -> {
            if (AuthorsPanel.authorsJList.getSelectedIndex() != -1) {
                // adding a book from the selected author
                int selectedAuthorId = Integer.parseInt(AuthorsPanel.authorsJList.getSelectedValue().toString().split("\\) ")[0]);
                Author author = db.getAuthor(selectedAuthorId);

                // values for ComboBox
                String[] booksValues = new String[db.getBooksFromAuthor(author.getId()).size()];
                int i = 0;
                for (Book book : db.getBooksFromAuthor(author.getId())) {
                    booksValues[i] = book.getId() + ") " + book.getTitle();
                    i++;
                }

                JLabel bookLabel = new JLabel("Book");
                JComboBox bookField = new JComboBox(booksValues);
                JPanel removeBookPanel = new JPanel();
                removeBookPanel.add(bookLabel);
                removeBookPanel.add(bookField);

                int result = JOptionPane.showConfirmDialog(listAuthors, removeBookPanel, "Remove book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    Book book = db.getBook(Integer.parseInt((String.valueOf(bookField.getSelectedItem())).split("\\) ")[0]));
                    library.removeBook(book);

                    // updating books list
                    BooksPanel.setCurrentBooks(new ArrayList<>(library.getBooks()));
                }
            }
        });

        crud.add(show);
        crud.add(edit);
        crud.add(delete);
        crud.add(addBook);
        crud.add(removeBook);

        this.add(searchAuthors, BorderLayout.NORTH);
        this.add(listAuthors, BorderLayout.CENTER);
        this.add(crud, BorderLayout.SOUTH);
    }

    public JList createAuthorsJList(List<Author> authors) {
        String[] authorsString = new String[authors.size()];
        int i = 0;
        for (Author author : authors) {
            authorsString[i] = author.getId() + ") " + author.getName();
            i++;
        }
        JList list = new JList(authorsString);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }
}
