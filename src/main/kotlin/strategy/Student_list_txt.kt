package org.korsnaike.strategy

import org.korsnaike.pattern.Data_list
import org.korsnaike.pattern.student.Data_list_student_short
import org.korsnaike.student.Student
import org.korsnaike.student.Student_short
import java.io.File
import java.io.FileNotFoundException
import java.util.function.Predicate

/**
 * Класс для работы с коллекцией объектов Student и получения его из txt файла
 */
class Student_list_txt(private var students: MutableList<Student>) {

    constructor() : this(mutableListOf())

    constructor(filePath: String) : this(mutableListOf()) {
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
            }.toMutableList()
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
        return students.first { it.id == id }
    }

    /**
     * Получить список k по счету n объектов класса Student_short
     *
     * @param n - индекс с которого начинать выборку (должен быть 0 или больше)
     * @param k - количество элементов для выборки (должно быть больше 0)
     *
     * @return Список объектов Student_short
     */
    fun get_k_n_student_short_list(n: Int, k: Int): Data_list<Student_short> {
        require(n >= 0) { "Индекс n должен быть больше или равен 0." }
        require(k > 0) { "Количество k должно быть больше 0." }

        return Data_list_student_short(students
            .drop(n)
            .take(k)
            .map { Student_short(it) }
        )
    }

    /**
     * Сортировка списка по фамилии и инициалам
     */
    fun orderStudentsByLastNameInitials() {
        orderStudents(compareBy { it.getLastNameWithInitials() })
    }

    /**
     * Сортировка списка
     */
    fun orderStudents(comparator: Comparator<Student>) {
        students.sortedWith(comparator)
    }

    /**
     * Добавление объекта
     */
    fun add(student: Student) {
        val nextId = (students.maxByOrNull { it.id }?.id ?: 0) + 1
        student.id = nextId

        students.addLast(student)
    }

    /**
     * Заменить объект по ID
     */
    fun replaceById(student: Student, id: Int) {
        student.id = id
        students.replaceAll { if (it.id == id) student else it }
    }

    /**
     * Удалить по ID
     */
    fun removeById(id: Int) {
        students.removeIf { it.id == id }
    }

    /**
     * Получить количество объектов в списке
     */
    fun get_student_short_count(): Int = students.count()
}