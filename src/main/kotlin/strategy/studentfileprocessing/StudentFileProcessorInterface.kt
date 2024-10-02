package org.korsnaike.strategy.studentfileprocessing

import org.korsnaike.student.Student

/**
 * Интерфейс для классов, предназначенных для реализации работы с разным видом записи и чтения из файлов
 */
interface StudentFileProcessorInterface {
    fun read_from_file(filePath: String): MutableList<Student>

    fun write_to_file(students: MutableList<Student>, directory: String, fileName: String)

}