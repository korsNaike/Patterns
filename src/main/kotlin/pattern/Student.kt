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

    constructor(
        id: Int,
        lastName: String,
        firstName: String,
        middleName: String
    ) : this(id, lastName, firstName, middleName, null, null, null, null)

    constructor(
        lastName: String,
        firstName: String,
        middleName: String
    ) : this(0, lastName, firstName, middleName, null, null, null, null)

    override fun toString(): String {
        return "Student(id=$id, name='$firstName $middleName $lastName', " +
                "phone=$phone, telegram=$telegram, email=$email, git=$git)"
    }
}
