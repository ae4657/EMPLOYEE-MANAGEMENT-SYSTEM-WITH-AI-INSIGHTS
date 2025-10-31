import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login — AI Enhanced EMS");
        setSize(520, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JLabel title = new JLabel("AI Enhanced Employee Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setOpaque(true);
        title.setBackground(new Color(45, 118, 232));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0, 60));
        add(title, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(247, 248, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        centerPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        centerPanel.add(passwordField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");

        loginBtn.setBackground(new Color(144, 238, 144)); // light green
        loginBtn.setForeground(Color.BLACK);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);

        exitBtn.setBackground(new Color(255, 160, 122)); // light red
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exitBtn.setFocusPainted(false);

        buttonPanel.add(loginBtn);
        buttonPanel.add(exitBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        loginBtn.addActionListener(e -> checkLogin());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private void checkLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.equalsIgnoreCase("admin") && password.equals("admin123")) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome HR.");
            dispose();
            new Dashboard().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials! Try again.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
