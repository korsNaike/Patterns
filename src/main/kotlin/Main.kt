package org.korsnaike

import org.korsnaike.pattern.Student;
import org.korsnaike.pattern.Student_short;

fun main() {
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