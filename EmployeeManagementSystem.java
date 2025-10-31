import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class EmployeeManagementSystem extends JFrame {
    private JTextField idField, nameField, ageField, designationField, perfField, expField;
    private JTextField aiIdField;                 // for AI tab input
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextArea aiArea;
    private JTabbedPane tabs;

    public EmployeeManagementSystem() {
        setTitle("Employee Manager — AI Enhanced EMS");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel title = new JLabel("Employee Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(new Color(45, 118, 232));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(12, 10, 12, 10));
        add(title, BorderLayout.NORTH);

        // Tabs
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel employeePanel = buildEmployeePanel();
        JPanel aiPanel = buildAIPanel();

        tabs.addTab("Employee Records", employeePanel);
        tabs.addTab("AI Insights", aiPanel);

        add(tabs, BorderLayout.CENTER);

        loadEmployees();
    }

    // Build the employee records tab (form + table + buttons)
    private JPanel buildEmployeePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Left form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(247, 248, 252));
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        idField = new JTextField();         // IMPORTANT: keep editable so user can type ID
        nameField = new JTextField();
        ageField = new JTextField();
        designationField = new JTextField();
        perfField = new JTextField();
        expField = new JTextField();

        JTextField[] fields = {idField, nameField, ageField, designationField, perfField, expField};
        for (JTextField f : fields) {
            f.setPreferredSize(new Dimension(220, 35));
            f.setFont(fieldFont);
            f.setForeground(Color.BLACK);
            f.setBackground(Color.WHITE);
            f.setOpaque(true);
        }

        int r = 0;
        gbc.gridx = 0; gbc.gridy = r; formPanel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1; formPanel.add(idField, gbc);
        r++;
        gbc.gridx = 0; gbc.gridy = r; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        r++;
        gbc.gridx = 0; gbc.gridy = r; formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; formPanel.add(ageField, gbc);
        r++;
        gbc.gridx = 0; gbc.gridy = r; formPanel.add(new JLabel("Designation:"), gbc);
        gbc.gridx = 1; formPanel.add(designationField, gbc);
        r++;
        gbc.gridx = 0; gbc.gridy = r; formPanel.add(new JLabel("Performance (0–10):"), gbc);
        gbc.gridx = 1; formPanel.add(perfField, gbc);
        r++;
        gbc.gridx = 0; gbc.gridy = r; formPanel.add(new JLabel("Experience (Years):"), gbc);
        gbc.gridx = 1; formPanel.add(expField, gbc);
        r++;

        // Buttons (bright, black text)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        JButton addBtn = makeButton("Add", new Color(76, 175, 80));
        JButton updateBtn = makeButton("Update", new Color(33, 150, 243));
        JButton deleteBtn = makeButton("Delete", new Color(244, 67, 54));
        JButton clearBtn = makeButton("Clear", new Color(255, 193, 7));
        JButton aiPredictBtn = makeButton("AI Predict", new Color(255, 152, 0));

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(aiPredictBtn);

        gbc.gridx = 0; gbc.gridy = ++r; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        // Table (right)
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Age", "Designation", "Score", "Experience"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Employees"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, scrollPane);
        split.setDividerLocation(400);
        mainPanel.add(split, BorderLayout.CENTER);

        // --- Event handlers ---
        addBtn.addActionListener(e -> addEmployee());

        updateBtn.addActionListener(e -> {
            if (idField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Employee ID to update.", "Input required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            updateEmployee();
        });

        deleteBtn.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Employee ID to delete.", "Input required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int conf = JOptionPane.showConfirmDialog(this, "Delete employee with ID " + idText + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                deleteEmployee();
            }
        });

        clearBtn.addActionListener(e -> clearForm());

        // When AI Predict clicked on this tab: copy ID to AI tab field and show results
        aiPredictBtn.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Employee ID to analyze or select a row from table.", "Input required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // set the AI tab input and switch
            aiIdField.setText(idText);
            showAIForEmployeeId(idText);
            tabs.setSelectedIndex(1);
        });

        // Fill form when clicking a table row
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                ageField.setText(tableModel.getValueAt(row, 2).toString());
                designationField.setText(tableModel.getValueAt(row, 3).toString());
                perfField.setText(tableModel.getValueAt(row, 4).toString());
                expField.setText(tableModel.getValueAt(row, 5).toString());
            }
        });

        return mainPanel;
    }

    // Build the AI tab with its own ID input and Analyze button
    private JPanel buildAIPanel() {
        JPanel aiPanel = new JPanel(new BorderLayout());
        aiArea = new JTextArea();
        aiArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        aiArea.setEditable(false);
        aiArea.setForeground(Color.BLACK);
        aiArea.setBorder(BorderFactory.createTitledBorder("Predicted Results"));

        // top control area: label, id input, analyze button
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        JLabel lbl = new JLabel("Enter Employee ID:");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aiIdField = new JTextField();
        aiIdField.setPreferredSize(new Dimension(120, 30));
        aiIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton analyzeBtn = new JButton("Analyze");
        analyzeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        analyzeBtn.setBackground(new Color(45,118,232));
        analyzeBtn.setForeground(Color.WHITE);
        analyzeBtn.setPreferredSize(new Dimension(110, 32));

        top.add(lbl);
        top.add(aiIdField);
        top.add(analyzeBtn);

        // action: when analyze clicked, show AI for typed id
        analyzeBtn.addActionListener(e -> {
            String idText = aiIdField.getText().trim();
            if (idText.isEmpty()) {
                aiArea.setText("Please enter an Employee ID to analyze.");
                return;
            }
            showAIForEmployeeId(idText);
        });

        aiPanel.add(top, BorderLayout.NORTH);
        aiPanel.add(new JScrollPane(aiArea), BorderLayout.CENTER);
        return aiPanel;
    }

    // Styled button helper (black text)
    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setOpaque(true);
        return btn;
    }

    // ---------------- CRUD ----------------
    private void loadEmployees() {
        tableModel.setRowCount(0);
        try (Connection con = DBUtil.getConnection(); Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM employees");
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getInt("age"));
                row.add(rs.getString("designation"));
                row.add(rs.getDouble("performance_score"));
                row.add(rs.getDouble("experience"));
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load error: " + ex.getMessage());
        }
    }

    private void addEmployee() {
        try (Connection con = DBUtil.getConnection()) {
            String q = "INSERT INTO employees(name, age, designation, performance_score, experience) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, nameField.getText().trim());
            ps.setInt(2, Integer.parseInt(ageField.getText().trim()));
            ps.setString(3, designationField.getText().trim());
            ps.setDouble(4, Double.parseDouble(perfField.getText().trim()));
            ps.setDouble(5, Double.parseDouble(expField.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Added!");
            loadEmployees();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Add error: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        try (Connection con = DBUtil.getConnection()) {
            String q = "UPDATE employees SET name=?, age=?, designation=?, performance_score=?, experience=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, nameField.getText().trim());
            ps.setInt(2, Integer.parseInt(ageField.getText().trim()));
            ps.setString(3, designationField.getText().trim());
            ps.setDouble(4, Double.parseDouble(perfField.getText().trim()));
            ps.setDouble(5, Double.parseDouble(expField.getText().trim()));
            ps.setInt(6, Integer.parseInt(idField.getText().trim()));
            int n = ps.executeUpdate();
            if (n > 0) JOptionPane.showMessageDialog(this, "Employee Updated!");
            else JOptionPane.showMessageDialog(this, "No record updated - check ID.");
            loadEmployees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update error: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        try (Connection con = DBUtil.getConnection()) {
            String q = "DELETE FROM employees WHERE id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, Integer.parseInt(idField.getText().trim()));
            int n = ps.executeUpdate();
            if (n > 0) JOptionPane.showMessageDialog(this, "Employee Deleted!");
            else JOptionPane.showMessageDialog(this, "No record found to delete.");
            loadEmployees();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        designationField.setText("");
        perfField.setText("");
        expField.setText("");
        table.clearSelection();
    }

    // ---------------- AI logic ----------------
    // Called from AI tab analyze button, or when user clicks AI Predict in records tab
    private void showAIForEmployeeId(String idText) {
        aiArea.setText("");
        try (Connection con = DBUtil.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT name, performance_score, experience FROM employees WHERE id=?");
            ps.setInt(1, Integer.parseInt(idText));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                double perf = rs.getDouble("performance_score");
                double exp = rs.getDouble("experience");

                String attrition = (perf < 4.0) ? "High Attrition Risk" : "Stable";
                String training = (perf < 6.0) ? "Training Recommended" : "No Training Needed";
                String promotion = (perf > 8.0 && exp > 3) ? "Eligible for Promotion" : "Not Yet Eligible";

                StringBuilder sb = new StringBuilder();
                sb.append("AI Insights for ").append(name).append(" (ID: ").append(idText).append(")\n\n");
                sb.append("Performance Score: ").append(perf).append("\n");
                sb.append("Experience (yrs): ").append(exp).append("\n\n");
                sb.append("Attrition Risk: ").append(attrition).append("\n");
                sb.append("Training Advice: ").append(training).append("\n");
                sb.append("Promotion Status: ").append(promotion).append("\n");

                aiArea.setText(sb.toString());
            } else {
                aiArea.setText("No employee found with ID: " + idText);
            }
        } catch (NumberFormatException nf) {
            aiArea.setText("Invalid ID format. Enter a numeric ID.");
        } catch (Exception ex) {
            aiArea.setText("AI Error: " + ex.getMessage());
        }
    }

    // ---------------- main ----------------
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new EmployeeManagementSystem().setVisible(true));
    }
}
