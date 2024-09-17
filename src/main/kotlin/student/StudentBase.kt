package org.korsnaike.student

/**
 * Базовый класс для студентов
 */
abstract class StudentBase(
    open val id: Int,
    open var git: String? = null
) {

    /**
     * Получить контактную информацию о пользователе
     */
    abstract fun getContactInfo(): String

    /**
     * Получить фамилию с инициалами
     */
    abstract fun getLastNameWithInitials(): String

    /**
     * Метод для получения краткой информации о студенте
     */
    fun getInfo(): String {
        val lastNameWithInitials = getLastNameWithInitials()
        val contactInfo = getContactInfo()
        return "$lastNameWithInitials; $git; $contactInfo"
    }
}