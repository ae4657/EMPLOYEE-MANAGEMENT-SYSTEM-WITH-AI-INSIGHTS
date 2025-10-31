import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Dashboard — AI EMS");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Welcome, HR", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setOpaque(true);
        header.setBackground(new Color(255, 249, 230)); // soft cream
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(0, 60));
        add(header, BorderLayout.NORTH);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(247, 248, 252));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton openBtn = new JButton("Open Employee Management");
        openBtn.setBackground(new Color(173, 216, 230)); // light blue
        openBtn.setForeground(Color.BLACK);
        openBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        openBtn.setFocusPainted(false);
        openBtn.setPreferredSize(new Dimension(250, 40));

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(openBtn, gbc);
        add(mainPanel, BorderLayout.CENTER);

        // Footer buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(247, 248, 252));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(255, 255, 153)); // light yellow
        logoutBtn.setForeground(Color.BLACK);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setPreferredSize(new Dimension(100, 35));

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(new Color(255, 160, 122)); // light red
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exitBtn.setPreferredSize(new Dimension(100, 35));

        bottomPanel.add(logoutBtn);
        bottomPanel.add(exitBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners
        openBtn.addActionListener(e -> {
            new EmployeeManagementSystem().setVisible(true);
            dispose();
        });

        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
            dispose();
            new LoginPage().setVisible(true);
        });

        exitBtn.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
