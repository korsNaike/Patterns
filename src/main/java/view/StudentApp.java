package view;

import org.korsnaike.dto.StudentFilter;
import org.korsnaike.enums.SearchParam;
import org.korsnaike.strategy.Student_list_DB;
import org.korsnaike.student.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class StudentApp {
    private static final int PAGE_SIZE = 20;
    private static int currentPage = 1;
    private static final Student_list_DB studentDB = new Student_list_DB();

    private static final JTextField nameField = new JTextField();
    private static final JComboBox<String> gitComboBox = new JComboBox<>(new String[] { "�� �����", "��", "���" });
    private static final JTextField gitField = new JTextField();

    private static final JTextField emailField = new JTextField();
    private static final JComboBox<String> emailComboBox = new JComboBox<>(new String[] { "�� �����", "��", "���" });
    private static final JTextField phoneField = new JTextField();
    private static final JComboBox<String> phoneComboBox = new JComboBox<>(new String[] { "�� �����", "��", "���" });
    private static final JTextField telegramField = new JTextField();
    private static final JComboBox<String> telegramComboBox = new JComboBox<>(new String[] { "�� �����", "��", "���" });

    public static void create(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.add("������ ���������", createStudentTab());
            tabbedPane.add("������� 2", new JLabel("���������� ������� 2"));
            tabbedPane.add("������� 3", new JLabel("���������� ������� 3"));

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }

    private static JPanel createStudentTab() {
        JPanel panel = new JPanel(new BorderLayout());

        addFilters(panel);

        // ������� ���������
        String[] columnNames = { "ID", "������� � ��������", "���", "Email", "�������", "Telegram" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ������ ��������������
            }
        };
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // ������ ����������
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("��������");
        JButton editButton = new JButton("��������");
        JButton deleteButton = new JButton("�������");
        JButton nextPageButton = new JButton("���������");
        JButton prevPageButton = new JButton("����������");
        JButton refreshButton = new JButton("��������");

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = table.getSelectedRow() >= 0;
            editButton.setEnabled(rowSelected);
            deleteButton.setEnabled(rowSelected);
        });

        // �������� ������ � �������
        refreshInfo(tableModel);

        // ����������� ������
        addButton.addActionListener(e -> {
            // ������� ����� ���������� ��������
            Student student = new Student(0, "����", "������", "��������", "@ivan", "git", "123-456", "ivan@mail.com");
            int id = studentDB.addStudent(student);
            if (id > 0) {
                JOptionPane.showMessageDialog(panel, "������� ��������!");
                refreshInfo(tableModel);
            } else {
                JOptionPane.showMessageDialog(panel, "������ ��� ���������� ��������.");
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Student student = studentDB.getStudentById(id);
                if (student != null) {
                    student.setFirstName("���������");
                    if (studentDB.updateStudent(student)) {
                        JOptionPane.showMessageDialog(panel, "������� ��������!");
                        refreshInfo(tableModel);
                    } else {
                        JOptionPane.showMessageDialog(panel, "������ ��� ���������� ��������.");
                    }
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                if (studentDB.deleteStudent(id)) {
                    JOptionPane.showMessageDialog(panel, "������� ������!");
                    refreshInfo(tableModel);
                } else {
                    JOptionPane.showMessageDialog(panel, "������ ��� �������� ��������.");
                }
            }
        });

        nextPageButton.addActionListener(e -> {
            currentPage++;
            refreshInfo(tableModel);
        });

        prevPageButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                refreshInfo(tableModel);
            }
        });

        refreshButton.addActionListener(e -> {
            refreshInfo(tableModel);
        });

        // ��������� ������
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(prevPageButton);
        buttonPanel.add(nextPageButton);
        buttonPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static void addFilters(JPanel panel) {
        // ������ ����������
        JPanel filterPanel = new JPanel(new GridLayout(5, 3));
        filterPanel.setBorder(BorderFactory.createTitledBorder("�������"));

        // ��������� ��������
        setupFilter(gitComboBox, gitField);
        setupFilter(emailComboBox, emailField);
        setupFilter(phoneComboBox, phoneField);
        setupFilter(telegramComboBox, telegramField);

        // ��������� �������� ��������
        filterPanel.add(new JLabel("������� � ��������:"));
        filterPanel.add(nameField);
        filterPanel.add(new JLabel()); // �����������

        filterPanel.add(new JLabel("GitHub:"));
        filterPanel.add(gitComboBox);
        filterPanel.add(gitField);

        filterPanel.add(new JLabel("Email:"));
        filterPanel.add(emailComboBox);
        filterPanel.add(emailField);

        filterPanel.add(new JLabel("�������:"));
        filterPanel.add(phoneComboBox);
        filterPanel.add(phoneField);

        filterPanel.add(new JLabel("Telegram:"));
        filterPanel.add(telegramComboBox);
        filterPanel.add(telegramField);

        panel.add(filterPanel, BorderLayout.NORTH);
    }


    private static void setupFilter(JComboBox<String> comboBox, JTextField textField) {
        textField.setEnabled(false); // �� ��������� ���� ���������
        comboBox.addActionListener(e -> {
            // ���� �������� ������ ���� ������ "��"
            textField.setEnabled(comboBox.getSelectedItem().equals("��"));
        });
    }


    private static void refreshInfo(DefaultTableModel tableModel) {
        String nameFilter = nameField.getText().trim();
        SearchParam gitSearch = SearchParam.create(
                (String) Objects.requireNonNull(gitComboBox.getSelectedItem())
        );
        String gitFilter = gitField.getText().trim();
        SearchParam emailSearch = SearchParam.create(
                (String) Objects.requireNonNull(emailComboBox.getSelectedItem())
        );
        String emailFilter = emailField.getText().trim();

        SearchParam phoneSearch = SearchParam.create(
                (String) Objects.requireNonNull(phoneComboBox.getSelectedItem())
        );
        String phoneFilter = phoneField.getText().trim();

        SearchParam telegramSearch = SearchParam.create(
                (String) Objects.requireNonNull(telegramComboBox.getSelectedItem())
        );
        String telegramFilter = telegramField.getText().trim();

        StudentFilter studentFilter = new StudentFilter(
                nameFilter,
                gitFilter,
                emailFilter,
                phoneFilter,
                telegramFilter,
                gitSearch,
                phoneSearch,
                telegramSearch,
                emailSearch
        );

        // ��������� ������ � ������ ��������
        loadStudents(tableModel, studentFilter);
    }


    private static void loadStudents(
            DefaultTableModel tableModel,
            StudentFilter studentFilter
    ) {
        tableModel.setRowCount(0); // ������� �������

        // �������� ������ ��������� � ������ ��������
        List<Student> students = studentDB.getFilteredStudentList(
                currentPage, PAGE_SIZE,
                studentFilter
        );

        for (Student student : students) {
            tableModel.addRow(new Object[] {
                    student.getId(),
                    student.getLastNameWithInitials(),
                    student.getGitInfo(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getTelegram(),
            });
        }
    }

}
