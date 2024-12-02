package org.korsnaike.pattern.student

import org.korsnaike.observer.ObserveSubject
import org.korsnaike.observer.Observer
import org.korsnaike.pattern.Data_list
import org.korsnaike.student.Student
import org.korsnaike.student.Student_short

class Data_list_student_short(students: List<Student_short>) : Data_list<Student_short>(students), ObserveSubject {

    constructor(full_students: List<Student>) : this(students = full_students.map { Student_short(it) })

    override val observers: MutableList<Observer> = mutableListOf()

    override fun getEntityFields(): List<String> {
        return listOf("ID", "Имя", "Гит", "Контакт")
    }

    override fun getDataRow(entity: Student_short): List<Any> {
        return listOf(entity.id, entity.lastNameInitials, entity.git, entity.contact) as List<Any>
    }

}