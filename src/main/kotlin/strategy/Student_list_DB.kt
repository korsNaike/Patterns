package org.korsnaike.strategy

import org.korsnaike.adapter.StudentListInterface
import org.korsnaike.config.Config
import org.korsnaike.db.DbInterface
import org.korsnaike.db.PostgreDb
import org.korsnaike.dto.StudentFilter
import org.korsnaike.enums.SearchParam
import org.korsnaike.pattern.student.Data_list_student_short
import org.korsnaike.student.Student
import org.korsnaike.student.Student_short
import java.sql.SQLException
import java.sql.Statement


class Student_list_DB(private val db: DbInterface = PostgreDb.getInstance()): StudentListInterface {

    init {
        db.initConnectionsParams(config = Config)
    }

    override fun getStudentById(id: Int): Student? {
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

    override fun getKNStudentShortList(k: Int, n: Int):  Data_list_student_short {
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

    fun getFilteredStudentList(
        page: Int,
        pageSize: Int,
        studentFilter: StudentFilter
    ): List<Student> {
        val offset = (page - 1) * pageSize

        var query = "SELECT * FROM student "
        query += buildFilterQuery(studentFilter)
        query += " ORDER BY id LIMIT $pageSize OFFSET $offset"

        val studentList: MutableList<Student> = ArrayList()
        try {
            db.connect()
            val rs = db.executeQuery(query)
            while (rs.next()) {
                studentList.add(Student(rs))
            }
        } catch (e: SQLException) {
            println("Error fetching filtered students: " + e.message)
        } finally {
            db.closeConnection()
        }
        return studentList
    }

    fun getFilteredStudentCount(studentFilter: StudentFilter?): Int {
        var query = "SELECT COUNT(*) FROM student "
        query += buildFilterQuery(studentFilter!!) // Метод для генерации условий фильтрации

        try {
            db.connect()
            val rs = db.executeQuery(query)
            if (rs.next()) {
                return rs.getInt(1)
            }
        } catch (e: SQLException) {
            println("Error counting students: " + e.message)
        } finally {
            db.closeConnection()
        }
        return 0
    }

    private fun buildFilterQuery(studentFilter: StudentFilter): String {
        var query = "WHERE (TRUE"
        val nameFilter = studentFilter.nameFilter
        if (nameFilter.isNotEmpty()) query += " AND last_name || ' ' || first_name ILIKE '%$nameFilter%'"
        query = mutateQueryWithFilter(
            query,
            studentFilter.gitSearch,
            studentFilter.gitFilter,
            "git"
        )
        query = mutateQueryWithFilter(
            query,
            studentFilter.emailSearch,
            studentFilter.emailFilter,
            "email"
        )
        query = mutateQueryWithFilter(
            query,
            studentFilter.phoneSearch,
            studentFilter.phoneFilter,
            "phone"
        )
        query = mutateQueryWithFilter(
            query,
            studentFilter.telegramSearch,
            studentFilter.telegramFilter,
            "telegram"
        )

        return "$query)"
    }

    private fun mutateQueryWithFilter(
        query: String,
        search: SearchParam,
        value: String,
        column_name: String
    ): String {
        var new_query = query
        if (search == SearchParam.YES) {
            new_query += " AND $column_name IS NOT NULL AND $column_name!=''"
            if (value.isNotEmpty()) {
                new_query += " AND $column_name LIKE '%$value%'"
            }
        } else {
            if (search == SearchParam.NO) new_query += " AND ($column_name IS NULL OR $column_name='')"
        }

        return new_query
    }


    override fun addStudent(student: Student): Int {
        student.transformEmptyStringsToNull()
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

    override fun updateStudent(student: Student): Boolean {
        student.transformEmptyStringsToNull()
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

    override fun deleteStudent(id: Int): Boolean {
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

    override fun getStudentCount(): Int {
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