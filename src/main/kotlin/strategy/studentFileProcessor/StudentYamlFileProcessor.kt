package org.korsnaike.strategy.studentFileProcessor

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.serialization.encodeToString
import org.korsnaike.strategy.studentfileprocessor.StudentFileProcessor
import org.korsnaike.student.Student
import java.io.File

class StudentYamlFileProcessor: StudentFileProcessor {
    override fun read_from_file(filePath: String): MutableList<Student> {
        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            throw IllegalArgumentException("Некорректный адрес файла: $filePath")
        }

        try {
            return Yaml.default.decodeFromStream(file.inputStream())
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при чтении файла JSON: ${e.message}")
        }
    }

    override fun write_to_file(students: MutableList<Student>, directory: String, fileName: String) {
        val file = File(directory, fileName)

        try {
            file.parentFile?.mkdirs()
            file.writeText(Yaml.default.encodeToString(students))
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при записи в файл JSON: ${e.message}")
        }
    }
}