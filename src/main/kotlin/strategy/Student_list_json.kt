package org.korsnaike.strategy

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.korsnaike.pattern.Data_list
import org.korsnaike.pattern.student.Data_list_student_short
import org.korsnaike.student.Student
import org.korsnaike.student.Student_short
import java.io.File

class Student_list_json(students: MutableList<Student> = mutableListOf()) : Student_base_list(students) {

    constructor() : this(mutableListOf())

    constructor(filePath: String) : this() {
        read_from_file(filePath)
    }

    override fun read_from_file(filePath: String) {
        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            throw IllegalArgumentException("Некорректный адрес файла: $filePath")
        }

        try {
            students = Json.decodeFromString<MutableList<Student>>(file.readText()) ?: mutableListOf()
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при чтении файла JSON: ${e.message}")
        }
    }

    override fun write_to_file(directory: String, fileName: String) {
        val file = File(directory, fileName)

        try {
            file.parentFile?.mkdirs()
            file.writeText(Json.encodeToString(students))
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при записи в файл JSON: ${e.message}")
        }
    }
}