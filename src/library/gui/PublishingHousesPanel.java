package library.gui;

import library.Library;
import library.books.Book;
import library.books.PublishingHouse;
import library.database.DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PublishingHousesPanel extends JPanel {
    private final Library library = Library.getInstance();
    private final DB db = DB.getInstance();
    private List<PublishingHouse> currentPublishingHouses = new ArrayList(library.getPublishingHouses());
    private static JList publishingHousesJList;

    public PublishingHousesPanel() {
        this.setLayout(new BorderLayout());

        // LIST PUBLISHING HOUSES PANEL
        JSplitPane listPublishingHouses = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // listing publishingHouses
        publishingHousesJList = this.createPublishingHousesJList(currentPublishingHouses);
        JScrollPane listOfPublishingHouses = new JScrollPane(publishingHousesJList);

        listPublishingHouses.setLeftComponent(listOfPublishingHouses);

        // add publishingHouses
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        // name
        JLabel nameLabel = new JLabel("Name*");
        JTextField nameField = new JTextField(20);
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        addPanel.add(namePanel);

        // establishmentDate
        JLabel establishmentDateLabel = new JLabel("Establishment date*");
        JTextField establishmentDateField = new JTextField("dd/mm/yyyy", 20);
        JPanel establishmentDatePanel = new JPanel();
        establishmentDatePanel.add(establishmentDateLabel);
        establishmentDatePanel.add(establishmentDateField);
        addPanel.add(establishmentDatePanel);

        // add button
        JButton addPublishingHouse = new JButton("Add publishingHouse");
        addPublishingHouse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nameField.getText().equalsIgnoreCase("") && !establishmentDateField.getText().equalsIgnoreCase("dd/mm/yyyy")) {
                    String name = nameField.getText();
                    java.sql.Date establishmentDate = null;
                    try {
                        establishmentDate = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(establishmentDateField.getText()).getTime());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                    library.addPublishingHouse(name, establishmentDate);

                    // updating publishingHouses list
                    currentPublishingHouses = new ArrayList<>(library.getPublishingHouses());
                    PublishingHousesPanel.publishingHousesJList = createPublishingHousesJList(currentPublishingHouses);
                    listPublishingHouses.setLeftComponent(publishingHousesJList);

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
        buttonPanel.add(addPublishingHouse);
        buttonPanel.add(reset);
        addPanel.add(buttonPanel);

        // message
        JLabel message = new JLabel("The fields marked with * are mandatory!");
        JPanel messagePanel = new JPanel();
        messagePanel.add(message);
        addPanel.add(messagePanel);

        listPublishingHouses.setRightComponent(addPanel);


        // SEARCH PUBLISHING HOUSES PANEL
        JPanel searchPublishingHouses = new JPanel();

        // search field
        JTextField searchField = new JTextField(30);

        // search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String searchValue = searchField.getText();
            if (!searchValue.equalsIgnoreCase("")) {
                // the search field is not empty
                // => listing the publishingHouses which contain in their name the searched string
                List<PublishingHouse> searchedPublishingHouses = currentPublishingHouses.stream().filter(b -> b.getName().toLowerCase().contains(searchValue.toLowerCase())).collect(Collectors.toList());
                PublishingHousesPanel.publishingHousesJList = createPublishingHousesJList(searchedPublishingHouses);
            } else {
                // the search field is empty
                // => listing all the publishingHouses in tge library
                PublishingHousesPanel.publishingHousesJList = createPublishingHousesJList(currentPublishingHouses);
            }

            listPublishingHouses.setLeftComponent(new JScrollPane(publishingHousesJList));
        });

        searchPublishingHouses.add(searchField);
        searchPublishingHouses.add(searchButton);


        // CRUD BUTTONS
        JPanel crud = new JPanel();

        // show button
        JButton show = new JButton("Show");
        show.addActionListener(e -> {
            if (PublishingHousesPanel.publishingHousesJList.getSelectedIndex() != -1) {
                // listing the details about the selected publishingHouse
                int selectedPublishingHouseId = Integer.parseInt(PublishingHousesPanel.publishingHousesJList.getSelectedValue().toString().split("\\) ")[0]);
                PublishingHouse publishingHouse = db.getPublishingHouse(selectedPublishingHouseId);

                // details about publishingHouse
                String publishingHouseString = "Name: " + publishingHouse.getName() +
                        "\nEstablishment date: " + new SimpleDateFormat("dd MMMM yyyy").format(publishingHouse.getEstablishmentDate()) +
                        "\nNumber of books from the publishing house: " + publishingHouse.getNoBooks() +
                        "\nBooks from the publishing house:\n";

                for (Book book : db.getBooksFromPublishingHouse(publishingHouse.getId())) {
                    publishingHouseString += book.getTitle() + "\n";
                }

                JOptionPane.showMessageDialog(listPublishingHouses, publishingHouseString, publishingHouse.getName(), JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // edit button
        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
            if (PublishingHousesPanel.publishingHousesJList.getSelectedIndex() != -1) {
                // editing the selected publishingHouse
                int selectedPublishingHouseId = Integer.parseInt(PublishingHousesPanel.publishingHousesJList.getSelectedValue().toString().split("\\) ")[0]);
                PublishingHouse publishingHouse = db.getPublishingHouse(selectedPublishingHouseId);

                // edit panel
                JPanel editPanel = new JPanel();
                editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

                // name
                JLabel nameLabel1 = new JLabel("Title*");
                JTextField nameField1 = new JTextField(publishingHouse.getName(), 20);
                JPanel namePanel1 = new JPanel();
                namePanel1.add(nameLabel1);
                namePanel1.add(nameField1);
                editPanel.add(namePanel1);

                // establishmentDate
                JLabel establishmentDateLabel1 = new JLabel("Establishment date*");
                JTextField establishmentDateField1 = new JTextField(new SimpleDateFormat("dd/mm/yyyy").format(publishingHouse.getEstablishmentDate()), 20);
                JPanel establishmentDatePanel1 = new JPanel();
                establishmentDatePanel1.add(establishmentDateLabel1);
                establishmentDatePanel1.add(establishmentDateField1);
                editPanel.add(establishmentDatePanel1);

                int result = JOptionPane.showConfirmDialog(listPublishingHouses, editPanel, "Edit publishingHouse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (!nameField1.getText().equalsIgnoreCase("") && !establishmentDateField1.getText().equalsIgnoreCase("dd/mm/yyyy")) {
                        String name = nameField1.getText();
                        java.sql.Date establishmentDate = null;
                        try {
                            establishmentDate = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(establishmentDateField1.getText()).getTime());
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }

                        library.updatePublishingHouse(publishingHouse.getId(), name, establishmentDate, publishingHouse.getNoBooks());

                        // updating publishingHouses list
                        currentPublishingHouses = new ArrayList<>(library.getPublishingHouses());
                        PublishingHousesPanel.publishingHousesJList = createPublishingHousesJList(currentPublishingHouses);
                        listPublishingHouses.setLeftComponent(publishingHousesJList);

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
            if (PublishingHousesPanel.publishingHousesJList.getSelectedIndex() != -1) {
                // deleting the selected publishingHouse
                int selectedPublishingHouseId = Integer.parseInt(PublishingHousesPanel.publishingHousesJList.getSelectedValue().toString().split("\\) ")[0]);
                PublishingHouse publishingHouse = db.getPublishingHouse(selectedPublishingHouseId);

                library.removePublishingHouse(new PublishingHouse(publishingHouse.getId(), publishingHouse.getName(), publishingHouse.getEstablishmentDate(), publishingHouse.getNoBooks()));
                currentPublishingHouses = new ArrayList<>(library.getPublishingHouses());
                PublishingHousesPanel.publishingHousesJList = createPublishingHousesJList(currentPublishingHouses);
                listPublishingHouses.setLeftComponent(new JScrollPane(publishingHousesJList));

                // updating books list
                BooksPanel.setCurrentBooks(new ArrayList<>(library.getBooks()));
                BooksPanel.setAddBookPanel();
            }
        });

        // add book from publishingHouse
        JButton addBook = new JButton("Add book");
        addBook.addActionListener(e -> {
            if (PublishingHousesPanel.publishingHousesJList.getSelectedIndex() != -1) {
                // adding a book from the selected publishingHouse
                int selectedPublishingHouseId = Integer.parseInt(PublishingHousesPanel.publishingHousesJList.getSelectedValue().toString().split("\\) ")[0]);
                PublishingHouse publishingHouse = db.getPublishingHouse(selectedPublishingHouseId);

                GUI.setAddBook(new AddBookPanel(null, null, publishingHouse));
            }
        });

        // remove book from publishingHouse
        JButton removeBook = new JButton("Remove book");
        removeBook.addActionListener(e -> {
            if (PublishingHousesPanel.publishingHousesJList.getSelectedIndex() != -1) {
                // adding a book from the selected publishingHouse
                int selectedPublishingHouseId = Integer.parseInt(PublishingHousesPanel.publishingHousesJList.getSelectedValue().toString().split("\\) ")[0]);
                PublishingHouse publishingHouse = db.getPublishingHouse(selectedPublishingHouseId);

                // values for ComboBox
                String[] booksValues = new String[db.getBooksFromPublishingHouse(publishingHouse.getId()).size()];
                int i = 0;
                for (Book book : db.getBooksFromPublishingHouse(publishingHouse.getId())) {
                    booksValues[i] = book.getId() + ") " + book.getTitle();
                    i++;
                }

                JLabel bookLabel = new JLabel("Book");
                JComboBox bookField = new JComboBox(booksValues);
                JPanel removeBookPanel = new JPanel();
                removeBookPanel.add(bookLabel);
                removeBookPanel.add(bookField);

                int result = JOptionPane.showConfirmDialog(listPublishingHouses, removeBookPanel, "Remove book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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

        this.add(searchPublishingHouses, BorderLayout.NORTH);
        this.add(listPublishingHouses, BorderLayout.CENTER);
        this.add(crud, BorderLayout.SOUTH);
    }

    public JList createPublishingHousesJList(List<PublishingHouse> publishingHouses) {
        String[] publishingHousesString = new String[publishingHouses.size()];
        int i = 0;
        for (PublishingHouse publishingHouse : publishingHouses) {
            publishingHousesString[i] = publishingHouse.getId() + ") " + publishingHouse.getName();
            i++;
        }
        JList list = new JList(publishingHousesString);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }
}
