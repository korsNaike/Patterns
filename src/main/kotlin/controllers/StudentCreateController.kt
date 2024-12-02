package org.korsnaike.controllers

import org.korsnaike.strategy.Student_list
import org.korsnaike.strategy.Student_list_DB
import org.korsnaike.student.Student

class StudentCreateController(
    studentListController: Student_list_controller,
    studentList: Student_list
) : StudentFormController(studentListController,studentList) {

    constructor(studentListController: Student_list_controller): this(studentListController, Student_list(Student_list_DB()))

    override fun saveProcessedStudent(student: Student, id: Int?): String {
        val id = studentList.addStudent(student)
        if (id > 0) {
            studentListController.refresh_data()
            return "Студент добавлен!"
        } else {
            return "Ошибка при добавлении студента."
        }
    }

    override fun getAccessFields(): ArrayList<String> {
        return arrayListOf("Фамилия", "Имя", "Отчество", "Telegram", "GitHub", "Email")
    }
}