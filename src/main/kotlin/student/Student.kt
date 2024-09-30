package org.korsnaike.student

import java.io.File
import java.io.FileNotFoundException

class Student(
    override var id: Int = 0,
    var lastName: String,
    var firstName: String,
    var middleName: String,
    var telegram: String? = null,
    override var git: String? = null
): StudentBase(id, git) {

    var phone: String? = null
        get() {
            return field
        }
        set(value) {
            validatePhone(value)
            field = value
        }

    var email: String? = null
        get() {
            return field
        }
        set(value) {
            validateEmail(value)
            field = value
        }

    companion object {
        private val PHONE_REGEX = Regex("(?:\\+|\\d)[\\d\\-\\(\\) ]{9,}\\d")
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

        /**
         * Проверить верность номера
         */
        fun validatePhone(phoneNumber: String?) {
            if (!(phoneNumber == null || PHONE_REGEX.matches(phoneNumber))) {
                throw IllegalArgumentException("Invalid phone number format: $phoneNumber")
            }
        }

        /**
         * Проверить верность почты
         */
        fun validateEmail(email: String?) {
            if (!(email == null || EMAIL_REGEX.matches(email))) {
                throw IllegalArgumentException("Invalid email format: $email")
            }
        }
    }

    /**
     * Конструктор с номером телефона
     */
    constructor(
        id: Int = 0,
        lastName: String,
        firstName: String,
        middleName: String,
        phone: String? = null,
        telegram: String? = null,
        email: String? = null,
        git: String? = null
    ) : this(id, lastName, firstName, middleName, telegram, git) {
        this.email = email
        this.phone = phone
    }

    constructor(
        info: Map<String, Any?>
    ) : this(
        info.getOrDefault("id", 0) as Int,
        info["lastName"].toString(),
        info["firstName"].toString(),
        info["middleName"].toString(),
        info.getOrDefault("phone", null) as String?,
        info.getOrDefault("telegram", null) as String?,
        info.getOrDefault("email", null) as String?,
        info.getOrDefault("git", null) as String?
    )

    constructor(
        serializedString: String
    ) : this(0, "", "", "") {
        val regex =
            Regex("Student\\(id=([^,]+), firstName=([^,]+), lastName=([^,]+), middleName=([^,]+), phone=([^,]+), telegram=([^,]+), email=([^,]+), git=([^)]*)\\)")
        val matchResult = regex.find(serializedString)

        if (matchResult != null) {
            this.id = matchResult.groups[1]?.value?.toInt() ?: 0
            this.firstName = matchResult.groups[2]?.value ?: ""
            this.lastName = matchResult.groups[3]?.value ?: ""
            this.middleName = matchResult.groups[4]?.value ?: ""
            this.phone = matchResult.groups[5]?.value.let { if (it == null || it == "null") null else it }
            this.telegram = matchResult.groups[6]?.value.let { if (it == null || it == "null") null else it }
            this.email = matchResult.groups[7]?.value.let { if (it == null || it == "null") null else it }
            this.git = matchResult.groups[8]?.value.let { if (it == null || it == "null") null else it }

            if (firstName.isEmpty()) {
                throw IllegalArgumentException("Invalid student string format: firstName is empty!")
            }
            if (lastName.isEmpty()) {
                throw IllegalArgumentException("Invalid student string format: lastName is empty!")
            }
            if (middleName.isEmpty()) {
                throw IllegalArgumentException("Invalid student string format: middleName is empty!")
            }

            if (!validate()) {
                throw IllegalArgumentException("Invalid student string format: git or some contact is empty")
            }
        } else {
            throw IllegalArgumentException("Invalid student string format: $serializedString")
        }
    }

    override fun toString(): String {
        return "Student(id=$id, firstName=$firstName, lastName=$lastName, middleName=$middleName, phone=$phone, telegram=$telegram, email=$email, git=$git)"
    }

    /**
     * Провести валидацию наличия гита и одного из контактов
     */
    fun validate(): Boolean {
        return this.git?.isNotEmpty() ?: false &&
                (
                        this.email?.isNotEmpty() ?: false ||
                                this.telegram?.isNotEmpty() ?: false ||
                                this.phone?.isNotEmpty() ?: false
                        )
    }

    /**
     * Установить контакты
     */
    fun set_contacts(email: String?, telegram: String?, phone: String?) {

        if (email != null) {
            this.email = email;
        }

        if (telegram != null) {
            this.telegram = telegram;
        }

        if (phone != null) {
            this.phone = phone;
        }
    }

    /**
     * Метод для получения информации о способе связи
     */
    override fun getContactInfo(): String {
        val telegramContact = if (telegram != null) "Telegram: $telegram;" else ""
        val phoneContact = if (phone != null) "Phone: $phone;" else ""
        val emailContact = if (email != null) "Email: $email;" else ""

        return listOf(telegramContact, phoneContact, emailContact).first { it.isNotEmpty() }
    }

    override fun getLastNameWithInitials(): String = "$lastName ${firstName.first()}. ${middleName.first()}."
}
