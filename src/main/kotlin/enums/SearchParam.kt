package org.korsnaike.enums

/**
 * Enum для фильтрации, отвечающий за то, нужно ли проверять наличие/отсутствие параметра у сущности или это не важно
 */
enum class SearchParam {
    YES,
    NO,
    DONT_MATTER;

    companion object {
        @JvmStatic
        fun create(searchParam: String): SearchParam {
            return when (searchParam) {
                "Да" -> YES
                "Нет" -> NO
                else -> DONT_MATTER
            }
        }
    }
}