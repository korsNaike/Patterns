package org.korsnaike.pattern

class Student(
    private var id: Int = 0,
    private var lastName: String,
    private var firstName: String,
    private var middleName: String,
    private var phone: String? = null,
    private var telegram: String? = null,
    private var email: String? = null,
    private var git: String? = null
) {
    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getLastName(): String {
        return lastName
    }

    fun setLastName(lastName: String) {
        this.lastName = lastName
    }

    fun getFirstName(): String {
        return firstName
    }

    fun setFirstName(firstName: String) {
        this.firstName = firstName
    }

    fun getMiddleName(): String {
        return middleName
    }

    fun setMiddleName(middleName: String) {
        this.middleName = middleName
    }

    fun getPhone(): String? {
        return phone
    }

    fun setPhone(phone: String?) {
        this.phone = phone
    }

    fun getTelegram(): String? {
        return telegram
    }

    fun setTelegram(telegram: String?) {
        this.telegram = telegram
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getGit(): String? {
        return git
    }

    fun setGit(git: String?) {
        this.git = git
    }

    override fun toString(): String {
        return "Student(id=$id, name='$firstName $middleName $lastName', " +
                "phone=$phone, telegram=$telegram, email=$email, git=$git)"
    }
}
