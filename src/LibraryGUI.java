import javax.swing.*;

public class LibraryGUI extends JFrame{
    private JPanel mainPanel;
    private JTextField textField1;

    public LibraryGUI() {
        super("Library");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setBounds(100, 100, 700, 400);
        this.setVisible(true);
    }
}
