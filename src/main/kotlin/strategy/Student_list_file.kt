package org.korsnaike.strategy

import org.korsnaike.dto.StudentFilter
import org.korsnaike.enums.SearchParam
import org.korsnaike.pattern.Data_list
import org.korsnaike.pattern.student.Data_list_student_short
import org.korsnaike.strategy.studentfileprocessing.StudentFileProcessorInterface
import org.korsnaike.strategy.studentfileprocessing.StudentTxtFileProcessor
import org.korsnaike.student.Student
import org.korsnaike.student.Student_short

class Student_list_file(
    private var students: MutableList<Student>,
    var fileProcessor: StudentFileProcessorInterface = StudentTxtFileProcessor()
) {
    constructor(
        fileProcessor: StudentFileProcessorInterface = StudentTxtFileProcessor()
    ) : this(mutableListOf(), fileProcessor)

    constructor(
        filePath: String,
        fileProcessor: StudentFileProcessorInterface = StudentTxtFileProcessor()
    ) : this(mutableListOf(), fileProcessor) {
        read_from_file(filePath)
    }

    var studentFilter: StudentFilter? = null;

    /**
     * Считывает объекты из файла, используя объект StudentFileProcessor
     */
    fun read_from_file(filePath: String) {
        students = fileProcessor.read_from_file(filePath)
    }

    /**
     * Записать объекты в файл, использует объект StudentFileProcessor
     */
    fun write_to_file(directory: String, fileName: String) {
        fileProcessor.write_to_file(students, directory, fileName)
    }

    /**
     * Найти объект по id
     */
    fun findById(id: Int): Student {
        return students.first { it.id == id }
    }

    /**
     * Получить список k по счету n объектов класса Student_short
     *
     * @param n - страница (должен быть 0 или больше)
     * @param k - количество элементов для выборки (должно быть больше 0)
     *
     * @return Список объектов Student_short
     */
    fun get_k_n_student_short_list(n: Int, k: Int): Data_list<Student_short> {
        require(n >= 0) { "Индекс n должен быть больше или равен 0." }
        require(k > 0) { "Количество k должно быть больше 0." }

        if (studentFilter != null) {
            return Data_list_student_short(
                students
                    .drop((n - 1) * k)
                    .take(k)
                    .stream()
                    .filter { filterByStudentFilter(it) }
                    .map { Student_short(it) }
                    .toList()
            )
        }
        return Data_list_student_short(
            students
            .drop((n - 1) * k)
            .take(k)
            .map { Student_short(it) }
        )
    }

    fun filterByStudentFilter(student: Student): Boolean {
        if (studentFilter == null) {
            return true;
        }

        if (studentFilter!!.nameFilter.isNotEmpty() && !student.getFullName().contains(studentFilter!!.nameFilter)) {
            return false;
        } else if (
            !filterValueAndSearchParam(student.email, studentFilter!!.emailFilter, studentFilter!!.emailSearch) ||
            !filterValueAndSearchParam(student.git, studentFilter!!.gitFilter, studentFilter!!.gitSearch) ||
            !filterValueAndSearchParam(student.phone, studentFilter!!.phoneFilter, studentFilter!!.phoneSearch) ||
            !filterValueAndSearchParam(student.telegram, studentFilter!!.telegramFilter, studentFilter!!.telegramSearch)
            ) {
            return false
        }

        return true
    }

    fun filterValueAndSearchParam(value: String?, filterValue: String, searchParam: SearchParam): Boolean {
        if (
            searchParam == SearchParam.YES &&
            (
                    (
                            filterValue.isNotEmpty() && value?.contains(filterValue) != true
                    ) ||
                    (
                            filterValue.isEmpty() && value.isNullOrEmpty()
                    )
            )
        ) {
            return false
        } else if (
            searchParam == SearchParam.NO &&
            !value.isNullOrEmpty()
        ) {
            return false
        }
        return true
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