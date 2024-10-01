package org.korsnaike.student

import kotlinx.serialization.Serializable

@Serializable
class Student_short(
    val id: Int,
    val lastNameInitials: String,
    var git: String? = null,
    val contact: String? = null
): StudentBase(), Comparable<Student_short> {
    // Конструктор, который принимает объект класса Student
    constructor(student: Student) : this(
        id = student.id,
        info = student.getInfo()
    )

    // Конструктор, который принимает ID и строку с остальной информацией
    constructor(id: Int, info: String) : this(
        id = id,
        lastNameInitials = info.split(";")[0].trim(),
        git = info.split(";")[1].trim(),
        contact = info.split(";")[2].trim()
    )

    override fun getContactInfo(): String = this.contact ?: "Нет контактной информации"

    override fun getLastNameWithInitials(): String = this.lastNameInitials
    override fun getGitInfo(): String? = git

    override fun compareTo(other: Student_short): Int {
        return if (this.lastNameInitials > other.lastNameInitials) {
            1
        } else if (this.lastNameInitials == other.lastNameInitials) {
            0
        } else {
            -1
        }
    }

    override fun toString(): String = getInfo()
}