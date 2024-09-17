package org.korsnaike.pattern.student

import org.korsnaike.pattern.Data_list
import org.korsnaike.pattern.Data_table
import org.korsnaike.student.Student_short

class Data_list_student_short(students: List<Student_short>) : Data_list<Student_short>(students) {
    override fun getEntityFields(): List<String> {
        return listOf("Имя", "Гит")
    }

    override fun getDataRow(entity: Student_short): List<Any> {
        return listOf(entity.lastNameInitials, entity.git) as List<Any>
    }

}