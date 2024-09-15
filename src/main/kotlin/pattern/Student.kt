package org.korsnaike.pattern

class Student(
    var id: Int = 0,
    var lastName: String,
    var firstName: String,
    var middleName: String,
    var telegram: String? = null,
    var email: String? = null,
    var git: String? = null
) {

    var phone: String? = null
        get() {
            return field
        }
        set(value) {
            validatePhone(value)
            field = value
        }

    private fun validatePhone(phoneNumber: String?) {
        if (!isValidPhoneNumber(phoneNumber)) {
            throw IllegalArgumentException("Invalid phone number format: $phoneNumber")
        }
    }

    companion object {
        private val PHONE_REGEX = Regex("(?:\\+|\\d)[\\d\\-\\(\\) ]{9,}\\d")

        fun isValidPhoneNumber(phoneNumber: String?): Boolean {
            return phoneNumber == null || PHONE_REGEX.matches(phoneNumber)
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
    ) : this(id, lastName, firstName, middleName, telegram, email, git) {
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

    override fun toString(): String {
        return "Student(id=$id, name='$firstName $middleName $lastName', " +
                "phone=$phone, telegram=$telegram, email=$email, git=$git)"
    }
}
