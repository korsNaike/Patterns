package view;

import org.korsnaike.controllers.StudentCreateController;
import org.korsnaike.controllers.StudentUpdateController;
import org.korsnaike.controllers.Student_list_controller;
import org.korsnaike.dto.StudentFilter;
import org.korsnaike.logger.SimpleLogger;
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
        SimpleLogger.info("Инициализация окна управления студентами...");
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
            SimpleLogger.info("Окно управления успешно инициализировано.");
            update();
        });
    }

    private JPanel createStudentTab() {
        SimpleLogger.info("Создание вкладки 'Список студентов'...");
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
            SimpleLogger.info("Нажата кнопка 'Добавить'.");
            StudentCreateController studentCreateController = new StudentCreateController(this.controller);
            StudentFormModal modal = new StudentFormModal();
            modal.controller = studentCreateController;
            modal.create(null, "Создать новую запись");
        });

        editButton.addActionListener(e -> {
            SimpleLogger.info("Нажата кнопка 'Изменить'.");
            StudentUpdateController studentUpdateController = new StudentUpdateController(this.controller);
            StudentFormModal modal = new StudentFormModal();
            modal.controller = studentUpdateController;
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Student student = studentUpdateController.getStudentById(id);
                if (student == null) {
                    SimpleLogger.error("Студент с ID " + id + " не найден.");
                    showError("Запись не была найдена!");
                }
                modal.create(student, "Обновить запись");
            }
        });

        deleteButton.addActionListener(e -> {
            SimpleLogger.info("Нажата кнопка 'Удалить'.");
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
                        SimpleLogger.info("Попытка удаления студента с ID: " + id);
                        if (!controller.deleteStudent(id)) {
                            SimpleLogger.error("Не удалось удалить студента с ID: " + id);
                            success = false;
                        }
                    }

                    if (success) {
                        SimpleLogger.info("Выбранные студенты успешно удалены.");
                        JOptionPane.showMessageDialog(panel, "Выбранные студенты удалены!");
                    } else {
                        SimpleLogger.error("Не удалось удалить некоторых студентов.");
                        JOptionPane.showMessageDialog(panel, "Не удалось удалить некоторых студентов.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                    controller.refresh_data();
                }
            }
        });

        nextPageButton.addActionListener(e -> {
            currentPage++;
            SimpleLogger.info("Переход на следующую страницу: " + currentPage);
            controller.refresh_data(PAGE_SIZE, currentPage, getCurrentFilter());
        });

        prevPageButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                SimpleLogger.info("Переход на предыдущую страницу: " + currentPage);
                controller.refresh_data(PAGE_SIZE, currentPage, getCurrentFilter());
            }
        });

        refreshButton.addActionListener(e -> controller.refresh_data(PAGE_SIZE, currentPage, null));

        // Добавляем кнопки
        SimpleLogger.info("Обновление данных таблицы.");
        buttonPanel.add(pageInfoLabel); // Метка страницы
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(prevPageButton);
        buttonPanel.add(nextPageButton);
        buttonPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        SimpleLogger.info("Вкладка 'Список студентов' успешно создана.");
        return panel;
    }

    @Override
    public void update() {
        SimpleLogger.info("Обновление таблицы студентов.");
        set_table_params();
        set_table_data();
    }

    private void set_table_params() {
        List<String> newColumnNames = dataList.getEntityFields();
        SimpleLogger.info("Загрузка данных студентов в таблицу.");
        tableModel.setColumnIdentifiers(newColumnNames.toArray());

        // Получаем количество страниц
        int lastPage = dataList.getPagination().getTotalPages();

        // Если произошло так, что текущая страница больше, чем последняя, то откатываем страницу и пересчитываем
        if (lastPage < currentPage) {
            currentPage = lastPage;
            controller.refresh_data(PAGE_SIZE, currentPage, getCurrentFilter());
            return;
        }
        SimpleLogger.info("Данные студентов успешно загружены.");

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
