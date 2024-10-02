package org.korsnaike

import org.korsnaike.db.MigrationList
import org.korsnaike.migrations.`02102024_2353_create_table_students`
import org.korsnaike.migrations.`03102024_0004_add_test_data_to_student_table`
import org.korsnaike.pattern.student.Data_list_student_short
import org.korsnaike.strategy.Student_list
import org.korsnaike.strategy.studentfileprocessing.StudentJsonFileProcessor
import org.korsnaike.strategy.studentfileprocessing.StudentYamlFileProcessor
import org.korsnaike.student.Student
import org.korsnaike.student.Student_short

fun main() {
    val migrationList = MigrationList(
        listOf(
            `02102024_2353_create_table_students`(),
            `03102024_0004_add_test_data_to_student_table`(),
        )
    )
    migrationList.allUp()
}

fun studentTest() {
    val filePath = "src/files/students.yaml"
    val studentList = Student_list(filePath, StudentYamlFileProcessor())

    studentList.add(Student(0, "NewJohn", "John", "JOGN", email = "jhon@hmail.ru"))
    studentList.fileProcessor = StudentJsonFileProcessor()
    studentList.write_to_file("src/files", "students.json")
}

fun checkValidStudent(student: Student) {
    val studentName = student.lastName
    if (student.validate()) {
        println("Student $studentName is valid");
    } else {
        println("Student $studentName is not valid");
    }
}

fun dataTableTest() {
    val students = listOf(
        Student_short(1, "Иванов И.И.", "https://github.com/ivanov/Patterns", "Telegram: @ivanich"),
        Student_short(2, "Петров П.П.", "https://github.com/petrov/Patterns", "Email: pudge@gmail.com"),
        Student_short(3, "Сидоров С.С.", "https://github.com/sidorov/Patterns", "Phone: +7 (999) 222-11-11")
    )

    val studentList = Data_list_student_short(students)

    println("Названия столбцов: ${studentList.getNames()}")

    studentList.select(0)
    studentList.select(2)
    println("Выбранные элементы: ${studentList.getSelected()}")

    val dataTable = studentList.getData()
    println("Количество строк: ${dataTable.getRowCount()}")
    println("Количество столбцов: ${dataTable.getColumnCount()}")

// Вывод данных таблицы
    for (i in 0 until dataTable.getRowCount()) {
        for (j in 0 until dataTable.getColumnCount()) {
            print("${dataTable.getElement(i, j)} ")
        }
        println()
    }
}