package view;

import org.korsnaike.controllers.StudentFormController;
import org.korsnaike.exceptions.ValidateException;
import org.korsnaike.student.Student;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class StudentFormModal {

    StudentFormController controller;

    public void create(Student existingStudent, String title) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(7, 2));

        // Поля для ввода данных
        JTextField lastNameField = new JTextField(existingStudent != null ? existingStudent.getLastName() : "");
        JTextField firstNameField = new JTextField(existingStudent != null ? existingStudent.getFirstName() : "");
        JTextField middleNameField = new JTextField(existingStudent != null ? existingStudent.getMiddleName() : "");
        JTextField telegramField = new JTextField(existingStudent != null && existingStudent.getTelegram() != null ? existingStudent.getTelegram() : "");
        JTextField gitField = new JTextField(existingStudent != null && existingStudent.getGit() != null ? existingStudent.getGit() : "");
        JTextField emailField = new JTextField(existingStudent != null && existingStudent.getEmail() != null ? existingStudent.getEmail() : "");

        // Добавляем компоненты
        dialog.add(new JLabel("Фамилия:"));
        dialog.add(lastNameField);

        dialog.add(new JLabel("Имя:"));
        dialog.add(firstNameField);

        dialog.add(new JLabel("Отчество:"));
        dialog.add(middleNameField);

        dialog.add(new JLabel("Telegram:"));
        dialog.add(telegramField);

        dialog.add(new JLabel("GitHub:"));
        dialog.add(gitField);

        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);

        // Кнопки
        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отмена");

        dialog.add(saveButton);
        dialog.add(cancelButton);

        // Обработчики кнопок
        saveButton.addActionListener(e -> {
            // Простая валидация

            // Создаем или обновляем объект Student
            try {
               Student student = controller.processForm(
                       existingStudent,
                       lastNameField.getText().trim(),
                       firstNameField.getText().trim(),
                       middleNameField.getText().trim(),
                       telegramField.getText().trim(),
                       gitField.getText().trim(),
                       emailField.getText().trim()
               );

                String resultMessage = controller.saveProcessedStudent(student);
                JOptionPane.showMessageDialog(dialog, resultMessage);
                dialog.dispose();
            } catch (ValidateException exception) {
                JOptionPane.showMessageDialog(
                        dialog,
                        exception.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

}
