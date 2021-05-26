package library.gui;

import library.Library;
import library.books.PublishingHouse;
import library.books.Section;
import library.database.DB;
import library.people.Author;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AddBookPanel extends JPanel {
    private final Library library = Library.getInstance();
    private final DB db = DB.getInstance();
    private String[] formatValues = {"physical", "pdf", "epub", "mobi", "azw", "iba"};
    private String[] sectionValues;
    private String[] authorValues;
    private String[] publishingHouseValues;

    public AddBookPanel(Section section1, Author author1){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setComboBoxValues();

        // title
        JLabel titleLabel = new JLabel("Title*");
        JTextField titleField = new JTextField(20);
        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        titlePanel.add(titleField);
        this.add(titlePanel);

        // number of pages
        JLabel noPagesLabel = new JLabel("Number of pages*");
        JTextField noPagesField = new JTextField(20);
        JPanel noPagesPanel = new JPanel();
        noPagesPanel.add(noPagesLabel);
        noPagesPanel.add(noPagesField);
        this.add(noPagesPanel);

        // number of copies
        JLabel noCopiesLabel = new JLabel("Number of copies");
        JTextField noCopiesField = new JTextField(20);
        JPanel noCopiesPanel = new JPanel();
        noCopiesPanel.add(noCopiesLabel);
        noCopiesPanel.add(noCopiesField);
        this.add(noCopiesPanel);

        // publish date
        JLabel publishDateLabel = new JLabel("Publish date*");
        JTextField publishDateField = new JTextField("dd/mm/yyyy", 20);
        JPanel publishDatePanel = new JPanel();
        publishDatePanel.add(publishDateLabel);
        publishDatePanel.add(publishDateField);
        this.add(publishDatePanel);

        // format
        JLabel formatLabel = new JLabel("Format*");
        JComboBox formatField = new JComboBox(this.formatValues);
        JPanel formatPanel = new JPanel();
        formatPanel.add(formatLabel);
        formatPanel.add(formatField);
        this.add(formatPanel);

        // section
        JLabel sectionLabel = new JLabel("Section*");
        JComboBox sectionField = new JComboBox(this.sectionValues);
        JPanel sectionPanel = new JPanel();
        sectionPanel.add(sectionLabel);
        sectionPanel.add(sectionField);
        if (section1 == null) {
            this.add(sectionPanel);
        }

        // author
        JLabel authorLabel = new JLabel("Author*");
        JComboBox authorField = new JComboBox(this.authorValues);
        JPanel authorPanel = new JPanel();
        authorPanel.add(authorLabel);
        authorPanel.add(authorField);
        if (author1 == null) {
            this.add(authorPanel);
        }

        // publishing house
        JLabel publishingHouseLabel = new JLabel("Publishing House*");
        JComboBox publishingHouseField = new JComboBox(this.publishingHouseValues);
        JPanel publishingHousePanel = new JPanel();
        publishingHousePanel.add(publishingHouseLabel);
        publishingHousePanel.add(publishingHouseField);
        this.add(publishingHousePanel);

        // add button
        JButton addBook = new JButton("Add book");
        addBook.addActionListener(e -> {
            if (!titleField.getText().equalsIgnoreCase("") && !noPagesField.getText().equalsIgnoreCase("") && !publishDateField.getText().equalsIgnoreCase("dd/mm/yyyy")) {
                String title = titleField.getText();
                int noPages = Integer.parseInt(noPagesField.getText());
                java.sql.Date publishDate = null;
                try {
                    publishDate = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(publishDateField.getText()).getTime());
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                Section section = section1 == null ? db.getSection(String.valueOf(sectionField.getSelectedItem())) : section1;
                Author author = author1 == null ? db.getAuthor(String.valueOf(authorField.getSelectedItem())) : author1;
                PublishingHouse publishingHouse = db.getPublishingHouse(String.valueOf(publishingHouseField.getSelectedItem()));

                String format = String.valueOf(formatField.getSelectedItem());

                if (format.equalsIgnoreCase("physical")) {
                    int noCopies = noCopiesField.getText().equals("") ? 1 : Integer.parseInt(noCopiesField.getText());
                    library.addBook(title, noPages, publishDate, section, author, publishingHouse, noCopies);
                } else {
                    library.addBook(title, noPages, publishDate, section, author, publishingHouse, format);
                }

                // updating books list
                BooksPanel.setCurrentBooks(new ArrayList<>(library.getBooks()));

                GUI.hideAddBook();

            } else {
                JOptionPane.showMessageDialog(this , "Please complete all the required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
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
        this.add(buttonPanel);

        // message
        JLabel message = new JLabel("The fields marked with * are mandatory!");
        JPanel messagePanel = new JPanel();
        messagePanel.add(message);
        this.add(messagePanel);
    }

    public void setComboBoxValues(){
        this.sectionValues = new String[library.getSections().size()];
        int i = 0;
        for (Section section : library.getSections()) {
            sectionValues[i] = section.getName();
            i++;
        }

        this.authorValues = new String[library.getAuthors().size()];
        i = 0;
        for (Author author : library.getAuthors()) {
            authorValues[i] = author.getName();
            i++;
        }

        this.publishingHouseValues = new String[library.getPublishingHouses().size()];
        i = 0;
        for (PublishingHouse publishingHouse : library.getPublishingHouses()) {
            publishingHouseValues[i] = publishingHouse.getName();
            i++;
        }

    }
}
