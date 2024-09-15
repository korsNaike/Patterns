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

    println(student1)
    println(student2)
    println(student3)
}