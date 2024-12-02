package org.korsnaike.controllers

import org.korsnaike.strategy.Student_list
import org.korsnaike.strategy.Student_list_DB
import org.korsnaike.student.Student

class StudentUpdateController(
    studentListController: Student_list_controller,
    studentList: Student_list
) : StudentFormController(studentListController,studentList) {

    constructor(studentListController: Student_list_controller) : this(
        studentListController,
        Student_list(Student_list_DB())
    )

    override fun saveProcessedStudent(student: Student, id: Int?): String {
        if (id == null) {
            return "Ошибка, не найдено"
        }
        val oldStudentData = studentList.getStudentById(id)
        if (oldStudentData != null) {
            val success = studentList.updateStudent(student)
            if (success) {
                studentListController.refresh_data()
                return "Студент обновлён!"
            } else {
                return "Ошибка при обновлении студента"
            }
        } else {
            return "Ошибка, студент с данным ID не найден."
        }
    }

    override fun getAccessFields(): ArrayList<String> {
        return arrayListOf("Фамилия", "Имя", "Отчество")
    }
}