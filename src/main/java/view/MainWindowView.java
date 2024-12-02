package view;

import org.korsnaike.controllers.StudentCreateController;
import org.korsnaike.controllers.StudentUpdateController;
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

    /** Информация о текущей странице  **/
    private static final int PAGE_SIZE = 20;
    private static int currentPage = 1;

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

    public MainWindowView() {}

    public void create(Student_list_controller controller) {
        setController(controller);
        controller.firstInitDataList();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.add("Список студентов", createStudentTab());

            frame.add(tabbedPane);
            frame.setVisible(true);
            update();
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

        addButton.addActionListener(e -> {
            StudentCreateController studentCreateController = new StudentCreateController(this.controller);
            StudentFormModal modal = new StudentFormModal();
            modal.controller = studentCreateController;
            modal.create(null, "Создать новую запись");
        });

        editButton.addActionListener(e -> {
            StudentUpdateController studentUpdateController = new StudentUpdateController(this.controller);
            StudentFormModal modal = new StudentFormModal();
            modal.controller = studentUpdateController;
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Student student = studentUpdateController.getStudentById(id);
                if (student == null) {
                    showError("Запись не была найдена!");
                }
                modal.create(student, "Обновить запись");
            }
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

        // Получаем количество страниц
        int lastPage = dataList.getPagination().getTotalPages();

        // Если произошло так, что текущая страница больше, чем последняя, то откатываем страницу и пересчитываем
        if (lastPage < currentPage) {
            currentPage = lastPage;
            controller.refresh_data(PAGE_SIZE, currentPage, getCurrentFilter());
            return;
        }

        updatePageControls(lastPage);
    }

    private void updatePageControls(int lastPage) {

        // Обновление текста метки страницы
        pageInfoLabel.setText("Страница: " + currentPage + " / " + lastPage);

        // Отключение кнопок в зависимости от текущей страницы
        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < lastPage);
    }

    private StudentFilter getCurrentFilter() {
        return null;
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

    public void showError(String message) {
        JDialog dialog = new JDialog((Frame) null, "Ошибка", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(7, 2));
        JOptionPane.showMessageDialog(dialog, "Произошла непредвиденная ошибка: " + message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
