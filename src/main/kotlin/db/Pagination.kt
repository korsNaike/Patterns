package org.korsnaike.db

import view.StudentApp
import kotlin.math.ceil

class Pagination {
    var totalSize: Int = 0
        private set

    var perPage: Int = 20

    var currentPage: Int = 1

    var totalPages: Int = 1
        private set // Закрытый сеттер

    fun updatePagination(totalSize: Int, currentPage: Int, perPage: Int) {
        this.totalSize = totalSize
        this.perPage = perPage
        this.currentPage = currentPage
        this.totalPages = calculateLastPage()
    }

    fun updatePagination(totalSize: Int) {
        this.totalSize = totalSize
        this.totalPages = calculateLastPage()
    }

    private fun calculateLastPage(): Int {
        val page = ceil(totalSize.toDouble() / perPage).toInt()
        return if (page == 0) 1 else page
    }
}