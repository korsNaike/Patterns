package view;

import org.jetbrains.annotations.NotNull;
import org.korsnaike.controllers.Student_list_controller;
import org.korsnaike.pattern.student.Data_list_student_short;
import org.korsnaike.student.Student;
import org.korsnaike.student.Student_short;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainWindowView implements ViewInterface {

    /** Контроллер **/
    private Student_list_controller controller;

    public void setController(Student_list_controller controller) {
        this.controller = controller;
    }

    private Data_list_student_short dataList;

    public void setDataList(Data_list_student_short dataList) {
        this.dataList = dataList;
    }

    /**
     * Таблица
     */
    private DefaultTableModel tableModel;

    /** Поля фильтрации **/
    private final JTextField nameField = new JTextField();

    private final JComboBox<String> gitComboBox = new JComboBox<>(new String[] { "Не важно", "Да", "Нет" });
    private final JTextField gitField = new JTextField();

    private final JTextField emailField = new JTextField();
    private final JComboBox<String> emailComboBox = new JComboBox<>(new String[] { "Не важно", "Да", "Нет" });

    private final JTextField phoneField = new JTextField();
    private final JComboBox<String> phoneComboBox = new JComboBox<>(new String[] { "Не важно", "Да", "Нет" });

    private final JTextField telegramField = new JTextField();
    private final JComboBox<String> telegramComboBox = new JComboBox<>(new String[] { "Не важно", "Да", "Нет" });

    /** Элементы пагинации **/
    private final JLabel pageInfoLabel = new JLabel("Страница: 1 / ?");
    private final JButton prevPageButton = new JButton("Предыдущая");
    private final JButton nextPageButton = new JButton("Следующая");

    /** Кнопки управления **/
    private final JButton refreshButton = new JButton("Обновить");
    private final JButton addButton = new JButton("Добавить");
    private final JButton editButton = new JButton("Изменить");
    private final JButton deleteButton = new JButton("Удалить");

    MainWindowView() {}

    public void create(Student_list_controller controller) {
        setController(controller);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.add("Список студентов", createStudentTab());

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }

    private JPanel createStudentTab() {
        JPanel panel = new JPanel(new BorderLayout());

//        addFilters(panel);

        // Таблица студентов
        String[] columnNames = dataList.getEntityFields().toArray(new String[0]);
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Запрет редактирования
            }
        };
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Панель управления
        JPanel buttonPanel = new JPanel();

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRowCount = table.getSelectedRowCount();
            editButton.setEnabled(selectedRowCount == 1); // "Изменить" доступна только при выделении одной строки
            deleteButton.setEnabled(selectedRowCount > 0); // "Удалить" доступна при выделении одной или более строк
        });

        // Загрузка данных в таблицу
        refreshInfo(tableModel);

        // Обработчики кнопок
        addButton.addActionListener(e -> {
            showStudentForm(null, "Добавить студента", student -> {
                int id = studentDB.addStudent(student);
                if (id > 0) {
                    JOptionPane.showMessageDialog(panel, "Студент добавлен!");
                    refreshInfo(tableModel);
                } else {
                    JOptionPane.showMessageDialog(panel, "Ошибка при добавлении студента.");
                }
            });
        });


        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Student student = studentDB.getStudentById(id);
                if (student != null) {
                    showStudentForm(student, "Редактировать студента", updatedStudent -> {
                        if (studentDB.updateStudent(updatedStudent)) {
                            JOptionPane.showMessageDialog(panel, "Студент обновлен!");
                            refreshInfo(tableModel);
                        } else {
                            JOptionPane.showMessageDialog(panel, "Ошибка при обновлении студента.");
                        }
                    });
                }
            }
        });


        deleteButton.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();
            if (selectedRows.length > 0) {
                int confirm = JOptionPane.showConfirmDialog(
                        panel,
                        "Вы уверены, что хотите удалить выбранных студентов?",
                        "Подтверждение удаления",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = true;

                    // Удаляем студентов по их ID
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        int id = (int) tableModel.getValueAt(selectedRows[i], 0);
                        if (!studentDB.deleteStudent(id)) {
                            success = false;
                        }
                    }

                    if (success) {
                        JOptionPane.showMessageDialog(panel, "Выбранные студенты удалены!");
                    } else {
                        JOptionPane.showMessageDialog(panel, "Не удалось удалить некоторых студентов.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }

                    refreshInfo(tableModel);
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

        refreshButton.addActionListener(e -> refreshInfo(tableModel));

        // Добавляем кнопки
        buttonPanel.add(pageInfoLabel); // Метка страницы
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
    }

    private void set_table_data() {
        tableModel.setRowCount(0); // Очищаем таблицу

        // Получаем список студентов с учетом фильтров
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
