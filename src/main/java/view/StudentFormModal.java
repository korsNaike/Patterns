package view;

import org.korsnaike.controllers.StudentFormController;
import org.korsnaike.exceptions.ValidateException;
import org.korsnaike.student.Student;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StudentFormModal {

    StudentFormController controller;

    public void create(Student existingStudent, String title) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(7, 2));

        // ���� ��� ����� ������
        JTextField lastNameField = new JTextField(existingStudent != null ? existingStudent.getLastName() : "");
        JTextField firstNameField = new JTextField(existingStudent != null ? existingStudent.getFirstName() : "");
        JTextField middleNameField = new JTextField(existingStudent != null ? existingStudent.getMiddleName() : "");
        JTextField telegramField = new JTextField(existingStudent != null && existingStudent.getTelegram() != null ? existingStudent.getTelegram() : "");
        JTextField gitField = new JTextField(existingStudent != null && existingStudent.getGit() != null ? existingStudent.getGit() : "");
        JTextField emailField = new JTextField(existingStudent != null && existingStudent.getEmail() != null ? existingStudent.getEmail() : "");

        // ��������� ����������
        ArrayList<String> accessFields = controller.getAccessFields();
        System.out.println(accessFields.toString());
        dialog.add(new JLabel("�������:"));
        dialog.add(lastNameField);
        if (!accessFields.contains("�������")) {
            lastNameField.setEnabled(false);
        }

        dialog.add(new JLabel("���:"));
        dialog.add(firstNameField);
        if (!accessFields.contains("���")) {
            firstNameField.setEnabled(false);
        }

        dialog.add(new JLabel("��������:"));
        dialog.add(middleNameField);
        if (!accessFields.contains("��������")) {
            middleNameField.setEnabled(false);
        }

        dialog.add(new JLabel("Telegram:"));
        dialog.add(telegramField);
        if (!accessFields.contains("Telegram")) {
            telegramField.setEnabled(false);
        }

        dialog.add(new JLabel("GitHub:"));
        dialog.add(gitField);
        if (!accessFields.contains("GitHub")) {
            gitField.setEnabled(false);
        }

        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        if (!accessFields.contains("Email")) {
            emailField.setEnabled(false);
        }

        // ������
        JButton saveButton = new JButton("���������");
        JButton cancelButton = new JButton("������");

        dialog.add(saveButton);
        dialog.add(cancelButton);

        // ����������� ������
        saveButton.addActionListener(e -> {
            // ������� ���������

            // ������� ��� ��������� ������ Student
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

                String resultMessage = controller.saveProcessedStudent(student, existingStudent != null ? existingStudent.getId() : null);
                JOptionPane.showMessageDialog(dialog, resultMessage);
                dialog.dispose();
            } catch (ValidateException exception) {
                JOptionPane.showMessageDialog(
                        dialog,
                        exception.getMessage(),
                        "������",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

}
