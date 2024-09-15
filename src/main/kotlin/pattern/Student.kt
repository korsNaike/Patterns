package org.korsnaike.pattern

class Student(
    var id: Int = 0,
    var lastName: String,
    var firstName: String,
    var middleName: String,
    var phone: String? = null,
    var telegram: String? = null,
    var email: String? = null,
    var git: String? = null
) {
    override fun toString(): String {
        return "Student(id=$id, name='$firstName $middleName $lastName', " +
                "phone=$phone, telegram=$telegram, email=$email, git=$git)"
    }
}
