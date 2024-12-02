package org.korsnaike.strategy

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.korsnaike.db.DbInterface
import org.korsnaike.dto.StudentFilter
import org.korsnaike.enums.SearchParam
import org.korsnaike.student.Student
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.sql.ResultSet
import java.sql.Statement

@ExtendWith(MockitoExtension::class)
class StudentListDBTest {

    @Mock
    private lateinit var dbMock: DbInterface

    private lateinit var studentListDB: Student_list_DB

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        studentListDB = Student_list_DB(db = dbMock)
    }

    @Test
    fun `getStudentById should return Student when student exists`() {
        val studentId = 1
        val resultSetMock = mock(ResultSet::class.java)

        `when`(dbMock.connect()).then { }
        `when`(dbMock.executeQuery("SELECT * FROM student WHERE id = $studentId")).thenReturn(resultSetMock)
        `when`(resultSetMock.next()).thenReturn(true)
        `when`(resultSetMock.getInt("id")).thenReturn(studentId)
        `when`(resultSetMock.getString("last_name")).thenReturn("LastName")
        `when`(resultSetMock.getString("first_name")).thenReturn("FirstName")
        `when`(resultSetMock.getString("middle_name")).thenReturn("MiddleName")

        val student = studentListDB.getStudentById(studentId)

        assertNotNull(student)
        assertEquals(studentId, student?.id)

        verify(dbMock).connect()
        verify(dbMock).executeQuery("SELECT * FROM student WHERE id = $studentId")
        verify(dbMock).closeConnection()
    }

    @Test
    fun `getStudentById should return null when student does not exist`() {
        val studentId = 1
        val resultSetMock = mock(ResultSet::class.java)

        `when`(dbMock.connect()).then { }
        `when`(dbMock.executeQuery("SELECT * FROM student WHERE id = $studentId")).thenReturn(resultSetMock)
        `when`(resultSetMock.next()).thenReturn(false)

        val student = studentListDB.getStudentById(studentId)

        assertNull(student)

        verify(dbMock).connect()
        verify(dbMock).executeQuery("SELECT * FROM student WHERE id = $studentId")
        verify(dbMock).closeConnection()
    }

    @Test
    fun `getKNStudentShortList should return filtered list when filter is set`() {
        val studentFilter = StudentFilter(
            nameFilter = "Test",
            gitFilter = "",
            emailFilter = "",
            phoneFilter = "",
            telegramFilter = "",
            gitSearch = SearchParam.YES,
            emailSearch = SearchParam.NO,
            phoneSearch = SearchParam.NO,
            telegramSearch = SearchParam.YES
        )
        studentListDB.initStudentFilter(studentFilter)

        val resultSetMock = mock(ResultSet::class.java)
        `when`(dbMock.connect()).then { }
        `when`(dbMock.executeQuery(anyString())).thenReturn(resultSetMock)
        `when`(resultSetMock.next()).thenReturn(true, false)
        `when`(resultSetMock.getInt("id")).thenReturn(1)
        `when`(resultSetMock.getString("last_name")).thenReturn("LastName")
        `when`(resultSetMock.getString("first_name")).thenReturn("FirstName")
        `when`(resultSetMock.getString("middle_name")).thenReturn("MiddleName")

        val result = studentListDB.getKNStudentShortList(5, 1)

        assertNotNull(result)
        assertEquals(1, result.getSize())

        verify(dbMock).connect()
        verify(dbMock).executeQuery(anyString())
        verify(dbMock).closeConnection()
    }

    @Test
    fun `addStudent should return new ID when insertion succeeds`() {
        val student = Student(id = 0, lastName = "Doe", firstName = "John", middleName = "Midede")
        val connectionMock = mock(java.sql.Connection::class.java)
        val preparedStatementMock = mock(java.sql.PreparedStatement::class.java)
        val resultSetMock = mock(ResultSet::class.java)

        // Настройка мока getConn() для возврата Connection
        `when`(dbMock.getConn()).thenReturn(connectionMock)

        // Настройка мока prepareStatement() для возврата PreparedStatement
        `when`(connectionMock.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
            .thenReturn(preparedStatementMock)

        // Настройка мока executeUpdate() и возвращение ResultSet с сгенерированным ID
        `when`(preparedStatementMock.executeUpdate()).thenReturn(1)
        `when`(preparedStatementMock.generatedKeys).thenReturn(resultSetMock)
        `when`(resultSetMock.next()).thenReturn(true)
        `when`(resultSetMock.getInt(1)).thenReturn(123)

        val newId = studentListDB.addStudent(student)

        assertEquals(123, newId)

        // Проверяем вызовы
        verify(dbMock).connect()
        verify(dbMock).closeConnection()
        verify(preparedStatementMock).setString(1, student.lastName)
        verify(preparedStatementMock).setString(2, student.firstName)
        verify(preparedStatementMock).executeUpdate()
    }


    @Test
    fun `deleteStudent should return true when deletion succeeds`() {
        val studentId = 1
        val connectionMock = mock(java.sql.Connection::class.java)
        val preparedStatementMock = mock(java.sql.PreparedStatement::class.java)

        // Настраиваем мок метода `getConn()` для возвращения соединения
        `when`(dbMock.getConn()).thenReturn(connectionMock)

        // Настраиваем мок `prepareStatement()` для возвращения PreparedStatement
        `when`(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock)

        // Настраиваем выполнение `executeUpdate()` для возвращения успешного результата
        `when`(preparedStatementMock.executeUpdate()).thenReturn(1)

        val result = studentListDB.deleteStudent(studentId)

        assertTrue(result)

        // Проверяем вызовы
        verify(dbMock).connect()
        verify(dbMock).closeConnection()
        verify(preparedStatementMock).setInt(1, studentId)
        verify(preparedStatementMock).executeUpdate()
    }

    @Test
    fun `deleteStudent should return false when deletion fails`() {
        val studentId = 1
        val connectionMock = mock(java.sql.Connection::class.java)
        val preparedStatementMock = mock(java.sql.PreparedStatement::class.java)

        // Настраиваем мок метода `getConn()` для возвращения соединения
        `when`(dbMock.getConn()).thenReturn(connectionMock)

        // Настраиваем мок `prepareStatement()` для возвращения PreparedStatement
        `when`(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock)

        // Настраиваем выполнение `executeUpdate()` для возвращения успешного результата
        `when`(preparedStatementMock.executeUpdate()).thenReturn(0)

        val result = studentListDB.deleteStudent(studentId)

        assertFalse(result)

        // Проверяем вызовы
        verify(dbMock).connect()
        verify(dbMock).closeConnection()
        verify(preparedStatementMock).setInt(1, studentId)
        verify(preparedStatementMock).executeUpdate()
    }
}
