package library.gui;

import library.Library;
import library.books.Book;
import library.database.DB;
import library.people.Reader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReadersPanel extends JPanel {
    private final Library library = Library.getInstance();
    private final DB db = DB.getInstance();
    private List<Reader> currentReaders = new ArrayList(library.getReaders());
    private static JList readersJList;

    public ReadersPanel() {
        this.setLayout(new BorderLayout());

        // LIST READERS PANEL
        JSplitPane listReaders = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // listing readers
        readersJList = this.createReadersJList(currentReaders);
        JScrollPane listOfReaders = new JScrollPane(readersJList);

        listReaders.setLeftComponent(listOfReaders);

        // add readers
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
        JLabel emailLabel = new JLabel("Email*");
        JTextField emailField = new JTextField(20);
        JPanel emailPanel = new JPanel();
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        addPanel.add(emailPanel);

        // address
        JLabel addressLabel = new JLabel("Address*");
        JTextField addressField = new JTextField(20);
        JPanel addressPanel = new JPanel();
        addressPanel.add(addressLabel);
        addressPanel.add(addressField);
        addPanel.add(addressPanel);


        // add button
        JButton addReader = new JButton("Add reader");
        addReader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nameField.getText().equalsIgnoreCase("") && !birthdateField.getText().equalsIgnoreCase("dd/mm/yyyy") && !emailField.getText().equalsIgnoreCase("") && !addressField.getText().equalsIgnoreCase("")) {
                    String name = nameField.getText();
                    java.sql.Date birthdate = null;
                    try {
                        birthdate = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(birthdateField.getText()).getTime());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    String email = emailField.getText();
                    String address = addressField.getText();

                    library.addReader(name, birthdate, email, address);

                    // updating readers list
                    currentReaders = new ArrayList<>(library.getReaders());
                    ReadersPanel.readersJList = createReadersJList(currentReaders);
                    listReaders.setLeftComponent(readersJList);
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
        buttonPanel.add(addReader);
        buttonPanel.add(reset);
        addPanel.add(buttonPanel);

        // message
        JLabel message = new JLabel("The fields marked with * are mandatory!");
        JPanel messagePanel = new JPanel();
        messagePanel.add(message);
        addPanel.add(messagePanel);

        listReaders.setRightComponent(addPanel);


        // SEARCH READERS PANEL
        JPanel searchReaders = new JPanel();

        // search field
        JTextField searchField = new JTextField(30);

        // search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String searchValue = searchField.getText();
            if (!searchValue.equalsIgnoreCase("")) {
                // the search field is not empty
                // => listing the readers which contain in their name the searched string
                List<Reader> searchedReaders = currentReaders.stream().filter(b -> b.getName().toLowerCase().contains(searchValue.toLowerCase())).collect(Collectors.toList());
                ReadersPanel.readersJList = createReadersJList(searchedReaders);
            } else {
                // the search field is empty
                // => listing all the readers in tge library
                ReadersPanel.readersJList = createReadersJList(currentReaders);
            }

            listReaders.setLeftComponent(new JScrollPane(readersJList));
        });

        searchReaders.add(searchField);
        searchReaders.add(searchButton);


        // CRUD BUTTONS
        JPanel crud = new JPanel();

        // show button
        JButton show = new JButton("Show");
        show.addActionListener(e -> {
            if (ReadersPanel.readersJList.getSelectedIndex() != -1) {
                // listing the details about the selected reader
                int selectedReaderId = Integer.parseInt(ReadersPanel.readersJList.getSelectedValue().toString().split("\\) ")[0]);
                Reader reader = db.getReader(selectedReaderId);

                // details about reader
                StringBuilder readerString = new StringBuilder("Name: " + reader.getName() +
                        "\nBirthdate: " + new SimpleDateFormat("dd MMMM yyyy").format(reader.getBirthDate()) +
                        "\nEmail: " + reader.getMail() +
                        "\nAdress: " + reader.getAddress() +
                        "\nNumber of books lent by the reader: " + db.getBooksLentByReader(reader.getId()).size() +
                        "\nBooks lent by the reader:\n");

                for (Book book : db.getBooksLentByReader(reader.getId())) {
                    String status;
                    if (!db.bookReturned(reader.getId(), book.getId())){
                        if (book.getType().equalsIgnoreCase("pbook")){
                            status = "not returned";
                        } else {
                            status = "no need for return";
                        }
                    } else {
                        status = "returned";
                    }

                    readerString.append(book.getTitle()).append(" - ").append(status).append("\n");
                }

                JOptionPane.showMessageDialog(listReaders, readerString.toString(), reader.getName(), JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // edit button
        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
            if (ReadersPanel.readersJList.getSelectedIndex() != -1) {
                // editing the selected reader
                int selectedReaderId = Integer.parseInt(ReadersPanel.readersJList.getSelectedValue().toString().split("\\) ")[0]);
                Reader reader = db.getReader(selectedReaderId);

                // edit panel
                JPanel editPanel = new JPanel();
                editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

                // name
                JLabel nameLabel1 = new JLabel("Title*");
                JTextField nameField1 = new JTextField(reader.getName(), 20);
                JPanel namePanel1 = new JPanel();
                namePanel1.add(nameLabel1);
                namePanel1.add(nameField1);
                editPanel.add(namePanel1);

                // birthdate
                JLabel birthdateLabel1 = new JLabel("Birthdate*");
                JTextField birthdateField1 = new JTextField(new SimpleDateFormat("dd/mm/yyyy").format(reader.getBirthDate()), 20);
                JPanel birthdatePanel1 = new JPanel();
                birthdatePanel1.add(birthdateLabel1);
                birthdatePanel1.add(birthdateField1);
                editPanel.add(birthdatePanel1);

                // email
                JLabel emailLabel1 = new JLabel("Email*");
                JTextField emailField1 = new JTextField(reader.getMail(), 20);
                JPanel emailPanel1 = new JPanel();
                emailPanel1.add(emailLabel1);
                emailPanel1.add(emailField1);
                editPanel.add(emailPanel1);

                // address
                JLabel addressLabel1 = new JLabel("Address*");
                JTextField addressField1 = new JTextField(reader.getAddress(), 20);
                JPanel addressPanel1 = new JPanel();
                addressPanel1.add(addressLabel1);
                addressPanel1.add(addressField1);
                editPanel.add(addressPanel1);

                int result = JOptionPane.showConfirmDialog(listReaders, editPanel, "Edit reader", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (!nameField1.getText().equalsIgnoreCase("") && !birthdateField1.getText().equalsIgnoreCase("dd/mm/yyyy") && !emailField1.getText().equalsIgnoreCase("") && !addressField1.getText().equalsIgnoreCase("")) {
                        String name = nameField1.getText();
                        java.sql.Date birthdate = null;
                        try {
                            birthdate = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(birthdateField1.getText()).getTime());
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        String email = emailField1.getText();
                        String address = addressField1.getText();

                        library.updateReader(reader.getId(), name, birthdate, email, address, reader.getNoBooksLent());

                        // updating readers list
                        currentReaders = new ArrayList<>(library.getReaders());
                        ReadersPanel.readersJList = createReadersJList(currentReaders);
                        listReaders.setLeftComponent(readersJList);
                    } else {
                        JOptionPane.showMessageDialog(editPanel, "Please complete all the required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        // delete button
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            if (ReadersPanel.readersJList.getSelectedIndex() != -1) {
                // deleting the selected reader
                int selectedReaderId = Integer.parseInt(ReadersPanel.readersJList.getSelectedValue().toString().split("\\) ")[0]);
                Reader reader = db.getReader(selectedReaderId);

                library.removeReader(new Reader(reader.getId(), reader.getName(), reader.getBirthDate(), reader.getMail(), reader.getAddress(), reader.getNoBooksLent()));
                currentReaders = new ArrayList<>(library.getReaders());
                ReadersPanel.readersJList = createReadersJList(currentReaders);
                listReaders.setLeftComponent(new JScrollPane(readersJList));
            }
        });

        // lend book by reader
        JButton addBook = new JButton("Lend book");
        addBook.addActionListener(e -> {
            if (ReadersPanel.readersJList.getSelectedIndex() != -1) {
                // lending a book by the selected reader
                int selectedReaderId = Integer.parseInt(ReadersPanel.readersJList.getSelectedValue().toString().split("\\) ")[0]);
                Reader reader = db.getReader(selectedReaderId);

                if (reader.getNoBooksLent() < 5) {
                    // values for ComboBox
                    String[] booksValues = new String[db.getBooksForLent(reader.getId()).size()];
                    int i = 0;
                    for (Book book : db.getBooksForLent(reader.getId())) {
                        booksValues[i] = book.getId() + ") " + book.getTitle();
                        i++;
                    }

                    JLabel bookLabel = new JLabel("Book");
                    JComboBox bookField = new JComboBox(booksValues);
                    JPanel lendBookPanel = new JPanel();
                    lendBookPanel.add(bookLabel);
                    lendBookPanel.add(bookField);

                    int result = JOptionPane.showConfirmDialog(listReaders, lendBookPanel, "Remove book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION && bookField.getSelectedItem() != null) {
                        Book book = db.getBook(Integer.parseInt((String.valueOf(bookField.getSelectedItem())).split("\\) ")[0]));
                        library.lendBook(reader, book);
                    }
                } else {
                    JOptionPane.showMessageDialog(listReaders, "The reader can't lend any more books before he returns at least one of the books he has lent already!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // return book by reader
        JButton removeBook = new JButton("Return book");
        removeBook.addActionListener(e -> {
            if (ReadersPanel.readersJList.getSelectedIndex() != -1) {
                // returning a book by the selected reader
                int selectedReaderId = Integer.parseInt(ReadersPanel.readersJList.getSelectedValue().toString().split("\\) ")[0]);
                Reader reader = db.getReader(selectedReaderId);

                // values for ComboBox
                String[] booksValues = new String[db.getBooksLentByReaderUnreturned(reader.getId()).size()];
                int i = 0;
                for (Book book : db.getBooksLentByReaderUnreturned(reader.getId())) {
                    booksValues[i] = book.getId() + ") " + book.getTitle();
                    i++;
                }

                JLabel bookLabel = new JLabel("Book");
                JComboBox bookField = new JComboBox(booksValues);
                JPanel returnBookPanel = new JPanel();
                returnBookPanel.add(bookLabel);
                returnBookPanel.add(bookField);

                int result = JOptionPane.showConfirmDialog(listReaders, returnBookPanel, "Remove book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION && bookField.getSelectedItem() != null) {
                    Book book = db.getBook(Integer.parseInt((String.valueOf(bookField.getSelectedItem())).split("\\) ")[0]));
                    library.returnBook(reader, book);
                }
            }
        });

        crud.add(show);
        crud.add(edit);
        crud.add(delete);
        crud.add(addBook);
        crud.add(removeBook);

        this.add(searchReaders, BorderLayout.NORTH);
        this.add(listReaders, BorderLayout.CENTER);
        this.add(crud, BorderLayout.SOUTH);
    }

    public JList createReadersJList(List<Reader> readers) {
        String[] readersString = new String[readers.size()];
        int i = 0;
        for (Reader reader : readers) {
            readersString[i] = reader.getId() + ") " + reader.getName();
            i++;
        }
        JList list = new JList(readersString);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }
}
