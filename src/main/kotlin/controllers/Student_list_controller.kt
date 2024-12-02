package org.korsnaike.controllers

import org.korsnaike.adapter.StudentListInterface
import org.korsnaike.db.exceptions.DbConnectionException
import org.korsnaike.dto.StudentFilter
import org.korsnaike.pattern.student.Data_list_student_short
import org.korsnaike.strategy.Student_list
import view.MainWindowView

class Student_list_controller(studentSourceData: StudentListInterface, private var view: MainWindowView) {


    private var studentsList: Student_list = Student_list(studentSourceData);
    private var dataListStudentShort: Data_list_student_short? = null;

    fun setView(view: MainWindowView) {
        this.view = view
    }

    fun firstInitDataList() {
        val page = 1
        val pageSize = 20
        try {
            dataListStudentShort = studentsList.getKNStudentShortList(k = pageSize, n = page, studentFilter = null)
            dataListStudentShort?.pagination?.updatePagination(
                studentsList.getStudentCount(),
                page,
                pageSize
            )
            view.setDataList(dataListStudentShort)
        } catch (e: DbConnectionException) {
            throwErrorMessage(e.message)
        }
    }

    fun refresh_data(pageSize: Int, page: Int, studentFilter: StudentFilter?) {
        try {
            dataListStudentShort = studentsList.getKNStudentShortList(k = pageSize, n = page, studentFilter = studentFilter);
            dataListStudentShort?.pagination?.updatePagination(
                studentsList.getStudentCount(),
                page,
                pageSize
            )
            view.setDataList(dataListStudentShort)
            dataListStudentShort?.addObserver(view)
            dataListStudentShort?.notifyObservers()
        } catch (e: DbConnectionException) {
            throwErrorMessage(e.message)
        }
    }

    fun refresh_data() {
        val page = dataListStudentShort?.pagination?.currentPage
        val pageSize = dataListStudentShort?.pagination?.perPage
        val studentFilter = studentsList.studentFilter
        if (page == null || pageSize == null) {
            throwErrorMessage("Список не был инициализирован, ошибка")
            return
        }
        try {
            dataListStudentShort = studentsList.getKNStudentShortList(k = pageSize, n = page, studentFilter = studentFilter);
            dataListStudentShort?.pagination?.updatePagination(
                studentsList.getStudentCount(),
                page,
                pageSize
            )
            view.setDataList(dataListStudentShort)
            dataListStudentShort?.addObserver(view)
            dataListStudentShort?.notifyObservers()
        } catch (e: DbConnectionException) {
            throwErrorMessage(e.message)
        }
    }

    private fun throwErrorMessage(errorMessage: String?) {
        var error = ""
        if (errorMessage == null) {
            error = "Неизвестная ошибка"
        } else {
            error = errorMessage
        }
        val page = 1
        val pageSize = 20
        dataListStudentShort = Data_list_student_short(mutableListOf())
        dataListStudentShort?.pagination?.updatePagination(
            0,
            page,
            pageSize
        )
        view.setDataList(dataListStudentShort)
        view.showError("Error: $error")
    }

}