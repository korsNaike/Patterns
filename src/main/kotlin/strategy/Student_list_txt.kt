package org.korsnaike.strategy

import org.korsnaike.student.Student
import java.io.File
import java.io.FileNotFoundException

class Student_list_txt(private var students: List<Student>) {

    constructor(): this(emptyList())

    constructor(filePath: String): this(emptyList()) {
        read_from_file(filePath)
    }

    /**
     * Метод для получения списка студентов из файла
     */
    fun read_from_file(filePath: String) {
        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            throw IllegalArgumentException("Некорректный адрес файла: $filePath")
        }

         try {
            students = file.readLines().mapNotNull { line ->
                try {
                    Student(line)
                } catch (e: IllegalArgumentException) {
                    println("Ошибка при разборе строки: $line")
                    println("Причина: ${e.message}")
                    null
                }
            }
        } catch (e: FileNotFoundException) {
            throw IllegalArgumentException("Файл не найден: $filePath")
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при чтении файла: ${e.message}")
        }
    }

    /**
     * Запись списка студентов в файл
     */
    fun write_to_file(directory: String, fileName: String) {
        val file = File(directory, fileName)

        try {
            file.parentFile?.mkdirs() // Создаем директорию, если она не существует
            file.bufferedWriter().use { writer ->
                students.forEach { student ->
                    writer.write(student.toString())
                    writer.newLine()
                }
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при записи в файл: ${e.message}")
        }
    }

    fun findById(id: Int): Student {
        return students.first { it.id == id}
    }
}