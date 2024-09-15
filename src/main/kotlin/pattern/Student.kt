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
