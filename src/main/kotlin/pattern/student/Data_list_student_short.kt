package org.korsnaike.pattern.student

import org.korsnaike.pattern.Data_list
import org.korsnaike.pattern.Data_table
import org.korsnaike.student.Student_short

class Data_list_student_short(students: List<Student_short>) : Data_list<Student_short>(students) {
    override fun getNames(): List<String> {
        return listOf("№", "Имя", "Гит")
    }

    override fun getData(): Data_table<Any> {
        val data = elements.mapIndexed { index, student ->
            listOf(index + 1, student.lastNameInitials, student.git)
        }
        return Data_table.create(data) as Data_table<Any>
    }
}