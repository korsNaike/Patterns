package org.korsnaike.strategy

import org.korsnaike.config.Config
import org.korsnaike.db.PostgreDb
import org.korsnaike.db.DbInterface
import org.korsnaike.pattern.Data_list
import org.korsnaike.pattern.student.Data_list_student_short
import org.korsnaike.student.Student
import org.korsnaike.student.Student_short
import java.sql.SQLException
import java.sql.Statement

class Student_list_DB(private val db: DbInterface = PostgreDb.getInstance()) {

    init {
        db.initConnectionsParams(config = Config)
    }

    fun getStudentById(id: Int): Student? {
        val query = "SELECT * FROM student WHERE id = $id"
        try {
            db.connect()
            val resultSet = db.executeQuery(query)
            return if (resultSet.next()) {
                Student(resultSet)
            } else {
                null
            }
        } catch (e: SQLException) {
            println("Error getting student: ${e.message}")
            return null
        } finally {
            db.closeConnection()
        }
    }

    fun getKNStudentShortList(k: Int, n: Int):  Data_list_student_short {
        val offset = (k - 1) * n
        val query = "SELECT * FROM student ORDER BY id LIMIT $n OFFSET $offset"
        val studentShortList = mutableListOf<Student_short>()

        try {
            db.connect()
            val resultSet = db.executeQuery(query)
            while (resultSet.next()) {
                val student = Student(resultSet)
                studentShortList.add(Student_short(student))
            }
        } catch (e: SQLException) {
            println("Error getting student list: ${e.message}")
        } finally {
            db.closeConnection()
        }

        return Data_list_student_short(studentShortList)
    }

    fun addStudent(student: Student): Int {
        val insertSQL = """
            INSERT INTO student (last_name, first_name, middle_name, telegram, git, phone, email)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        try {
            db.connect()
            db.setAutoCommit(false)
            val preparedStatement = db.getConn()?.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)
            preparedStatement?.apply {
                setString(1, student.lastName)
                setString(2, student.firstName)
                setString(3, student.middleName)
                setString(4, student.telegram)
                setString(5, student.git)
                setString(6, student.phone)
                setString(7, student.email)
                executeUpdate()

                val generatedKeys = generatedKeys
                if (generatedKeys.next()) {
                    val id = generatedKeys.getInt(1)
                    db.commit()
                    return id
                }
            }
            db.rollback()
        } catch (e: SQLException) {
            db.rollback()
            println("Error adding student: ${e.message}")
        } finally {
            db.setAutoCommit(true)
            db.closeConnection()
        }
        return -1
    }

    fun updateStudent(student: Student): Boolean {
        val updateSQL = """
            UPDATE student 
            SET last_name = ?, first_name = ?, middle_name = ?, telegram = ?, git = ?, phone = ?, email = ?
            WHERE id = ?
        """.trimIndent()

        try {
            db.connect()
            db.setAutoCommit(false)
            val preparedStatement = db.getConn()?.prepareStatement(updateSQL)
            preparedStatement?.apply {
                setString(1, student.lastName)
                setString(2, student.firstName)
                setString(3, student.middleName)
                setString(4, student.telegram)
                setString(5, student.git)
                setString(6, student.phone)
                setString(7, student.email)
                setInt(8, student.id)
                val rowsAffected = executeUpdate()
                if (rowsAffected > 0) {
                    db.commit()
                    return true
                }
            }
            db.rollback()
        } catch (e: SQLException) {
            db.rollback()
            println("Error updating student: ${e.message}")
        } finally {
            db.setAutoCommit(true)
            db.closeConnection()
        }
        return false
    }

    fun deleteStudent(id: Int): Boolean {
        val deleteSQL = "DELETE FROM student WHERE id = ?"

        try {
            db.connect()
            db.setAutoCommit(false)
            val preparedStatement = db.getConn()?.prepareStatement(deleteSQL)
            preparedStatement?.apply {
                setInt(1, id)
                val rowsAffected = executeUpdate()
                if (rowsAffected > 0) {
                    db.commit()
                    return true
                }
            }
            db.rollback()
        } catch (e: SQLException) {
            db.rollback()
            println("Error deleting student: ${e.message}")
        } finally {
            db.setAutoCommit(true)
            db.closeConnection()
        }
        return false
    }

    fun getStudentCount(): Int {
        val query = "SELECT COUNT(*) FROM student"
        try {
            db.connect()
            val resultSet = db.executeQuery(query)
            if (resultSet.next()) {
                return resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            println("Error getting student count: ${e.message}")
        } finally {
            db.closeConnection()
        }
        return 0
    }
}