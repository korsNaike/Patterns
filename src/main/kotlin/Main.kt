package org.korsnaike

import org.korsnaike.pattern.student.Data_list_student_short
import org.korsnaike.student.Student;
import org.korsnaike.student.Student_short

fun main() {
    dataTableTest();
}

fun studentTest() {
    val student1 = Student(
        1,
        "Ivanov",
        "Ivan",
        "Ivanovich",
        email = "ivan.ivanov@example.com",
        git = "ivanivanov"
    )

    val studentFromString = Student("Student(id=2, firstName=Petr, lastName=Ivanov, middleName=Ivanovich, phone=+7 (123) 456-78-90, telegram=null, email=ivan.ivanov@example.com, git=ivanivanov)");
    println(studentFromString.getInfo())

    val studentForShort = Student(
        id = 3,
        lastName = "Иванов",
        firstName = "Василий",
        middleName = "Иванович",
        telegram = "@vaska",
        git = "ivanov-va-git"
    )


    Student.write_to_txt("src/files", "students.txt", listOf(student1, studentForShort, studentFromString))
    val students = Student.read_from_txt("src/files/students.txt")
    students.forEach { println(it.getInfo()) }
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