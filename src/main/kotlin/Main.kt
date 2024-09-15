package org.korsnaike

import org.korsnaike.pattern.Student;

fun main() {
    val student1 = Student(
        1,
        "Ivanov",
        "Ivan",
        "Ivanovich",
        "+7 (123) 456-78-90",
        "@ivanivanov",
        "ivan.ivanov@example.com",
        "ivanivanov"
    )

    val student2 = Student(
        2,
        "Petrov",
        "Petr",
        "Petrovich",
        "+7 (987) 654-32-10",
        "@petrpetrov",
        "petr.petrov@example.com"
    )

    val student3 = Student(
        3,
        "Sidorova",
        "Svetlana",
        "Ivanovna"
    )

    val student4 = Student(
        mapOf(
            "id" to 4,
            "lastName" to "Doe",
            "firstName" to "Jane",
            "middleName" to "Olivia",
            "phone" to "+1 (555) 1234567",
            "email" to "jane.doe@example.com"
        )
    )

    println(student1)
    println(student2)
    println(student3)
    println(student4)

    student4.phone = "+72122233311"

    checkValidStudent(student1)
    checkValidStudent(student2)
    checkValidStudent(student3)

}

fun checkValidStudent(student: Student) {
    val studentName = student.lastName
    if (student.validate()) {
        println("Student $studentName is valid");
    } else {
        println("Student $studentName is not valid");
    }
}