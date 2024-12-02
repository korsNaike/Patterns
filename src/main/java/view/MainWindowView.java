package view;

import org.jetbrains.annotations.NotNull;
import org.korsnaike.controllers.Student_list_controller;
import org.korsnaike.dto.StudentFilter;
import org.korsnaike.pattern.student.Data_list_student_short;
import org.korsnaike.student.Student;
import org.korsnaike.student.Student_short;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainWindowView implements ViewInterface {

    /** ���������� � ������� ��������  **/
    private static final int PAGE_SIZE = 20;
    private static int currentPage = 1;

    /** ���������� **/
    private Student_list_controller controller;

    public void setController(Student_list_controller controller) {
        this.controller = controller;
    }

    private Data_list_student_short dataList;

    public void setDataList(Data_list_student_short dataList) {
        this.dataList = dataList;
    }

    /**
     * �������
     */
    private DefaultTableModel tableModel;

    /** ���� ���������� **/
    private final JTextField nameField = new JTextField();

    private final JComboBox<String> gitComboBox = new JComboBox<>(new String[] { "�� �����", "��", "���" });
    private final JTextField gitField = new JTextField();

    private final JTextField emailField = new JTextField();
    private final JComboBox<String> emailComboBox = new JComboBox<>(new String[] { "�� �����", "��", "���" });

    private final JTextField phoneField = new JTextField();
    private final JComboBox<String> phoneComboBox = new JComboBox<>(new String[] { "�� �����", "��", "���" });

    private final JTextField telegramField = new JTextField();
    private final JComboBox<String> telegramComboBox = new JComboBox<>(new String[] { "�� �����", "��", "���" });

    /** �������� ��������� **/
    private final JLabel pageInfoLabel = new JLabel("��������: 1 / ?");
    private final JButton prevPageButton = new JButton("����������");
    private final JButton nextPageButton = new JButton("���������");

    /** ������ ���������� **/
    private final JButton refreshButton = new JButton("��������");
    private final JButton addButton = new JButton("��������");
    private final JButton editButton = new JButton("��������");
    private final JButton deleteButton = new JButton("�������");

    public MainWindowView() {}

    public void create(Student_list_controller controller) {
        setController(controller);
        controller.firstInitDataList();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.add("������ ���������", createStudentTab());

            frame.add(tabbedPane);
            frame.setVisible(true);
            update();
        });
    }

    private JPanel createStudentTab() {
        JPanel panel = new JPanel(new BorderLayout());
//        addFilters(panel);

        // ������� ���������
        String[] columnNames = dataList.getEntityFields().toArray(new String[0]);
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ������ ��������������
            }
        };
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // ������ ����������
        JPanel buttonPanel = new JPanel();

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRowCount = table.getSelectedRowCount();
            editButton.setEnabled(selectedRowCount == 1); // "��������" �������� ������ ��� ��������� ����� ������
            deleteButton.setEnabled(selectedRowCount > 0); // "�������" �������� ��� ��������� ����� ��� ����� �����
        });

        nextPageButton.addActionListener(e -> {
            currentPage++;
            controller.refresh_data(PAGE_SIZE, currentPage, getCurrentFilter());
        });

        prevPageButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                controller.refresh_data(PAGE_SIZE, currentPage, getCurrentFilter());
            }
        });

        refreshButton.addActionListener(e -> controller.refresh_data(PAGE_SIZE, currentPage, null));

        // ��������� ������
        buttonPanel.add(pageInfoLabel); // ����� ��������
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

    @Override
    public void update() {
        set_table_params();
        set_table_data();
    }

    private void set_table_params() {
        List<String> newColumnNames = dataList.getEntityFields();
        tableModel.setColumnIdentifiers(newColumnNames.toArray());

        // �������� ���������� �������
        int lastPage = dataList.getPagination().getTotalPages();

        // ���� ��������� ���, ��� ������� �������� ������, ��� ���������, �� ���������� �������� � �������������
        if (lastPage < currentPage) {
            currentPage = lastPage;
            controller.refresh_data(PAGE_SIZE, currentPage, getCurrentFilter());
            return;
        }

        updatePageControls(lastPage);
    }

    private void updatePageControls(int lastPage) {

        // ���������� ������ ����� ��������
        pageInfoLabel.setText("��������: " + currentPage + " / " + lastPage);

        // ���������� ������ � ����������� �� ������� ��������
        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < lastPage);
    }

    private StudentFilter getCurrentFilter() {
        return null;
    }

    private void set_table_data() {
        tableModel.setRowCount(0); // ������� �������

        // �������� ������ ��������� � ������ ��������
        List<Student_short> students = dataList.toList();

        for (Student_short student : students) {
            tableModel.addRow(new Object[] {
                    student.getId(),
                    student.getLastNameWithInitials(),
                    student.getGitInfo(),
                    student.getContactInfo(),
            });
        }
    }
}
