package org.korsnaike.strategy.studentfileprocessing

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.korsnaike.student.Student
import java.io.File

class StudentJsonFileProcessor: StudentFileProcessorInterface {
    override fun read_from_file(filePath: String): MutableList<Student> {
        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            throw IllegalArgumentException("Некорректный адрес файла: $filePath")
        }

        try {
            return Json.decodeFromString<MutableList<Student>>(file.readText()) ?: mutableListOf()
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при чтении файла JSON: ${e.message}")
        }
    }

    override fun write_to_file(students: MutableList<Student>, directory: String, fileName: String) {
        val file = File(directory, fileName)

        try {
            file.parentFile?.mkdirs()
            file.writeText(Json.encodeToString(students))
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при записи в файл JSON: ${e.message}")
        }
    }
}