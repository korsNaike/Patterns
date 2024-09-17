package org.korsnaike.student

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class StudentTest {

    private lateinit var student: Student

    @BeforeEach
    fun setUp() {
        student = Student(
            id = 1,
            lastName = "Doe",
            firstName = "John",
            middleName = "Michael"
        )
    }

    @Test
    fun testPrimaryConstructor() {
        assertEquals(1, student.id)
        assertEquals("Doe", student.lastName)
        assertEquals("John", student.firstName)
        assertEquals("Michael", student.middleName)
        assertNull(student.telegram)
        assertNull(student.git)
        assertNull(student.phone)
        assertNull(student.email)
    }

    @Test
    fun testSecondaryConstructor() {
        val student = Student(
            id = 2,
            lastName = "Smith",
            firstName = "Jane",
            middleName = "Elizabeth",
            phone = "+1234567890",
            telegram = "@janesmith",
            email = "jane@example.com",
            git = "janesmith"
        )

        assertEquals(2, student.id)
        assertEquals("Smith", student.lastName)
        assertEquals("Jane", student.firstName)
        assertEquals("Elizabeth", student.middleName)
        assertEquals("+1234567890", student.phone)
        assertEquals("@janesmith", student.telegram)
        assertEquals("jane@example.com", student.email)
        assertEquals("janesmith", student.git)
    }

    @Test
    fun testMapConstructor() {
        val info = mapOf(
            "id" to 3,
            "lastName" to "Brown",
            "firstName" to "Bob",
            "middleName" to "James",
            "phone" to "+9876543210",
            "email" to "bob@example.com"
        )
        val student = Student(info)

        assertEquals(3, student.id)
        assertEquals("Brown", student.lastName)
        assertEquals("Bob", student.firstName)
        assertEquals("James", student.middleName)
        assertEquals("+9876543210", student.phone)
        assertEquals("bob@example.com", student.email)
        assertNull(student.telegram)
        assertNull(student.git)
    }

    @Test
    fun testValidPhoneNumber() {
        student.phone = "+1234567890"
        assertEquals("+1234567890", student.phone)
    }

    @Test
    fun testInvalidPhoneNumber() {
        assertThrows(IllegalArgumentException::class.java) {
            student.phone = "invalid"
        }
    }

    @Test
    fun testValidEmail() {
        student.email = "test@example.com"
        assertEquals("test@example.com", student.email)
    }

    @Test
    fun testInvalidEmail() {
        assertThrows(IllegalArgumentException::class.java) {
            student.email = "invalid"
        }
    }

    @Test
    fun testToString() {
        student.phone = "+1234567890"
        student.email = "john@example.com"
        student.telegram = "@johndoe"
        student.git = "johndoe"

        val expected = "Student(id=1, firstName=John, lastName=Doe, middleName=Michael, phone=+1234567890, telegram=@johndoe, email=john@example.com, git=johndoe)"
        assertEquals(expected, student.toString())
    }

    @Test
    fun testValidateValid() {
        student.git = "johndoe"
        student.email = "john@example.com"
        assertTrue(student.validate())
    }

    @Test
    fun testValidateInvalid() {
        assertFalse(student.validate())
    }

    @Test
    fun testValidateWithOnlyGit() {
        student.git = "johndoe"
        assertFalse(student.validate())
    }

    @Test
    fun testValidateWithOnlyContact() {
        student.phone = "+1234567890"
        assertFalse(student.validate())
    }

    @Test
    fun testSetContacts() {
        student.set_contacts("john@example.com", "@johndoe", "+1234567890")
        assertEquals("john@example.com", student.email)
        assertEquals("@johndoe", student.telegram)
        assertEquals("+1234567890", student.phone)
    }

    @Test
    fun testSetContactsPartial() {
        student.set_contacts("john@example.com", null, null)
        assertEquals("john@example.com", student.email)
        assertNull(student.telegram)
        assertNull(student.phone)
    }

    @Test
    fun testSetContactsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            student.set_contacts("invalid", null, null)
        }
    }
}