package library.gui;

import library.Library;
import library.books.Book;
import library.books.Section;
import library.database.DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SectionsPanel extends JPanel {
    private final Library library = Library.getInstance();
    private final DB db = DB.getInstance();
    private List<Section> currentSections = new ArrayList(library.getSections());
    private static JList sectionsJList;

    public SectionsPanel() {
        this.setLayout(new BorderLayout());

        // LIST SECTIONS PANEL
        JSplitPane listSections = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // listing sections
        sectionsJList = this.createSectionsJList(currentSections);
        JScrollPane listOfSections = new JScrollPane(sectionsJList);

        listSections.setLeftComponent(listOfSections);

        // add sections
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        // name
        JLabel nameLabel = new JLabel("Name*");
        JTextField nameField = new JTextField(20);
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        addPanel.add(namePanel);

        // add button
        JButton addSection = new JButton("Add section");
        addSection.addActionListener(e -> {
            if (!nameField.getText().equalsIgnoreCase("")) {
                String name = nameField.getText();
                library.addSection(name);

                // updating sections list
                currentSections = new ArrayList<>(library.getSections());
                SectionsPanel.sectionsJList = createSectionsJList(currentSections);
                listSections.setLeftComponent(sectionsJList);

                BooksPanel.setAddBookPanel();
            } else {
                JOptionPane.showMessageDialog(addPanel, "Please complete all the required fields!", "Warning", JOptionPane.WARNING_MESSAGE);

            }
        });

        // reset button
        JButton reset = new JButton("Reset");
        reset.addActionListener(e -> nameField.setText(""));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addSection);
        buttonPanel.add(reset);
        addPanel.add(buttonPanel);

        // message
        JLabel message = new JLabel("The fields marked with * are mandatory!");
        JPanel messagePanel = new JPanel();
        messagePanel.add(message);
        addPanel.add(messagePanel);

        listSections.setRightComponent(addPanel);


        // SEARCH SECTIONS PANEL
        JPanel searchSections = new JPanel();

        // search field
        JTextField searchField = new JTextField(30);

        // search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String searchValue = searchField.getText();
            if (!searchValue.equalsIgnoreCase("")) {
                // the search field is not empty
                // => listing the sections which contain in their name the searched string
                List<Section> searchedSections = currentSections.stream().filter(b -> b.getName().toLowerCase().contains(searchValue.toLowerCase())).collect(Collectors.toList());
                SectionsPanel.sectionsJList = createSectionsJList(searchedSections);
            } else {
                // the search field is empty
                // => listing all the sections in tge library
                SectionsPanel.sectionsJList = createSectionsJList(currentSections);
            }

            listSections.setLeftComponent(new JScrollPane(sectionsJList));
        });

        searchSections.add(searchField);
        searchSections.add(searchButton);


        // CRUD BUTTONS
        JPanel crud = new JPanel();

        // show button
        JButton show = new JButton("Show");
        show.addActionListener(e -> {
            if (SectionsPanel.sectionsJList.getSelectedIndex() != -1) {
                // listing the details about the selected section
                int selectedSectionId = Integer.parseInt(SectionsPanel.sectionsJList.getSelectedValue().toString().split("\\) ")[0]);
                Section section = db.getSection(selectedSectionId);

                // details about section
                StringBuilder sectionString = new StringBuilder("Name: " + section.getName() +
                        "\nNumber of books in the section: " + section.getNoBooks() +
                        "\nBooks in the section:\n");

                for (Book book : db.getBooksFromSection(section.getId())) {
                    sectionString.append(book.getTitle()).append("\n");
                }

                JOptionPane.showMessageDialog(listSections, sectionString.toString(), section.getName(), JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // edit button
        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
            if (SectionsPanel.sectionsJList.getSelectedIndex() != -1) {
                // editing the selected section
                int selectedSectionId = Integer.parseInt(SectionsPanel.sectionsJList.getSelectedValue().toString().split("\\) ")[0]);
                Section section = db.getSection(selectedSectionId);

                // edit panel
                JPanel editPanel = new JPanel();
                editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

                // name
                JLabel nameLabel1 = new JLabel("Title*");
                JTextField nameField1 = new JTextField(section.getName(), 20);
                JPanel namePanel1 = new JPanel();
                namePanel1.add(nameLabel1);
                namePanel1.add(nameField1);
                editPanel.add(namePanel1);

                int result = JOptionPane.showConfirmDialog(listSections, editPanel, "Edit section", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (!nameField1.getText().equalsIgnoreCase("")) {
                        String name = nameField1.getText();
                        library.updateSection(section.getId(), name, section.getNoBooks());

                        // updating sections list
                        currentSections = new ArrayList<>(library.getSections());
                        SectionsPanel.sectionsJList = createSectionsJList(currentSections);
                        listSections.setLeftComponent(sectionsJList);

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
            if (SectionsPanel.sectionsJList.getSelectedIndex() != -1) {
                // deleting the selected section
                int selectedSectionId = Integer.parseInt(SectionsPanel.sectionsJList.getSelectedValue().toString().split("\\) ")[0]);
                Section section = db.getSection(selectedSectionId);

                library.removeSection(new Section(section.getId(), section.getName(), section.getNoBooks()));
                currentSections = new ArrayList<>(library.getSections());
                SectionsPanel.sectionsJList = createSectionsJList(currentSections);
                listSections.setLeftComponent(new JScrollPane(sectionsJList));

                // updating books list
                BooksPanel.setCurrentBooks(new ArrayList<>(library.getBooks()));
                BooksPanel.setAddBookPanel();
            }
        });

        // add book from section
        JButton addBook = new JButton("Add book");
        addBook.addActionListener(e -> {
            if (SectionsPanel.sectionsJList.getSelectedIndex() != -1) {
                // adding a book into the selected section
                int selectedSectionId = Integer.parseInt(SectionsPanel.sectionsJList.getSelectedValue().toString().split("\\) ")[0]);
                Section section = db.getSection(selectedSectionId);

                GUI.setAddBook(new AddBookPanel(section, null, null));
            }
        });

        // remove book from section
        JButton removeBook = new JButton("Remove book");
        removeBook.addActionListener(e -> {
            if (SectionsPanel.sectionsJList.getSelectedIndex() != -1) {
                // adding a book into the selected section
                int selectedSectionId = Integer.parseInt(SectionsPanel.sectionsJList.getSelectedValue().toString().split("\\) ")[0]);
                Section section = db.getSection(selectedSectionId);

                // values for ComboBox
                String[] booksValues = new String[db.getBooksFromSection(section.getId()).size()];
                int i = 0;
                for (Book book : db.getBooksFromSection(section.getId())) {
                    booksValues[i] = book.getId() + ") " + book.getTitle();
                    i++;
                }

                JLabel bookLabel = new JLabel("Book");
                JComboBox bookField = new JComboBox(booksValues);
                JPanel removeBookPanel = new JPanel();
                removeBookPanel.add(bookLabel);
                removeBookPanel.add(bookField);

                int result = JOptionPane.showConfirmDialog(listSections, removeBookPanel, "Remove book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    Book book = db.getBook(Integer.parseInt((String.valueOf(bookField.getSelectedItem())).split("\\) ")[0]));
                    if (db.allCopiesReturned(book.getId())) {
                        library.removeBook(book);

                        // updating books list
                        BooksPanel.setCurrentBooks(new ArrayList<>(library.getBooks()));
                    } else {
                        JOptionPane.showMessageDialog(listOfSections, "Cannot delete a book which has still lent copies of it!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        crud.add(show);
        crud.add(edit);
        crud.add(delete);
        crud.add(addBook);
        crud.add(removeBook);

        this.add(searchSections, BorderLayout.NORTH);
        this.add(listSections, BorderLayout.CENTER);
        this.add(crud, BorderLayout.SOUTH);
    }

    public JList createSectionsJList(List<Section> sections) {
        String[] sectionsString = new String[sections.size()];
        int i = 0;
        for (Section section : sections) {
            sectionsString[i] = section.getId() + ") " + section.getName();
            i++;
        }
        JList list = new JList(sectionsString);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }
}