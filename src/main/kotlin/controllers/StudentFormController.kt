package org.korsnaike.controllers

import org.korsnaike.exceptions.ValidateException
import org.korsnaike.student.Student

abstract class StudentFormController {

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

    abstract fun saveProcessedStudent(student: Student): String
}