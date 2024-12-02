package org.korsnaike.controllers

import org.korsnaike.exceptions.ValidateException
import org.korsnaike.strategy.Student_list
import org.korsnaike.strategy.Student_list_DB
import org.korsnaike.student.Student

abstract class StudentFormController(
    val studentListController: Student_list_controller,
    val studentList: Student_list
) {
    constructor(studentListController: Student_list_controller) : this(
        studentListController,
        Student_list(Student_list_DB())
    )

    fun processForm(
        existingStudent: Student?,
        lastName: String,
        firstName: String,
        middleName: String,
        telegram: String,
        git: String,
        email: String
    ): Student {
        if (lastName.isEmpty() || firstName.isEmpty() || middleName.isEmpty()) {
            throw ValidateException("Фамилия, имя и отчество обязательны для заполнения!")
        }
        val student = if (existingStudent != null) existingStudent else Student()
        student.lastName = lastName
        student.firstName = firstName
        student.middleName = middleName
        student.telegram = telegram
        student.git = git
        student.email = email
        student.validate()

        return student
    }

    fun getStudentById(id: Int): Student? {
        return studentList.getStudentById(id)
    }

    abstract fun saveProcessedStudent(student: Student, id: Int?): String

    abstract fun getAccessFields(): ArrayList<String>
}