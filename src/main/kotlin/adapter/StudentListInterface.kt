package org.korsnaike.adapter

import org.korsnaike.dto.StudentFilter
import org.korsnaike.pattern.student.Data_list_student_short
import org.korsnaike.student.Student

interface StudentListInterface {
    var studentFilter: StudentFilter?

    fun getStudentById(id: Int): Student?

    fun getKNStudentShortList(k: Int, n: Int): Data_list_student_short

    fun setStudentFilter(filter: StudentFilter?) {
        this.studentFilter = filter
    }

    fun addStudent(student: Student): Int

    fun updateStudent(student: Student): Boolean

    fun deleteStudent(id: Int): Boolean

    fun getStudentCount(): Int
}