package org.korsnaike.dto

import org.korsnaike.enums.SearchParam

data class StudentFilter(
    val nameFilter: String,
    val gitFilter: String,
    val emailFilter: String,
    val phoneFilter: String,
    val telegramFilter: String,

    val gitSearch: SearchParam,
    val phoneSearch: SearchParam,
    val telegramSearch: SearchParam,
    val emailSearch: SearchParam,
)
