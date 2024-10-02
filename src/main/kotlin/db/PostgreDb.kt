package org.korsnaike.db

import org.korsnaike.config.Config
import org.korsnaike.db.exceptions.DbConnectionException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

/**
 * Реализация класс для работы с базой данных
 */
class PostgreDb private constructor(): DbInterface {

    private var host: String? = null;
    private var port: String? = null;
    private var dbName: String? = null;
    private var user: String? = null;
    private var password: String? = null;

    private var connection: Connection? = null;

    companion object {
        @Volatile
        private var instance: PostgreDb? = null

        /**
         * Получить экземпляр класса
         */
        fun getInstance(): PostgreDb {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = PostgreDb()
                    }
                }
            }
            return instance!!
        }
    }

    /**
     * Провести инициализацию параметров для подключения к базе данных
     */
    override fun initConnectionsParams(
        config: Config
    ) {
        this.host = config.db_host
        this.port = config.db_port
        this.dbName = config.db_name
        this.user = config.db_user
        this.password = config.db_password
    }

    override fun connect() {
        if (connection != null) {
            throw DbConnectionException("Подключение уже было выполнено")
        }
        val url = "jdbc:postgresql://${host}:${port}/${dbName}"
        val user = this.user
        val password = this.password

        connection = DriverManager.getConnection(url, user, password)
    }

    override fun setAutoCommit(autoCommit: Boolean) {
        if (connection != null) {
            connection!!.autoCommit = autoCommit
        }
    }

    override fun commit() {
        if (connection != null) {
            connection!!.commit()
        }
    }

    override fun rollback() {
        if (connection != null) {
            connection!!.rollback()
        }
    }

    private fun checkConnection() {
        if (connection == null || connection?.isClosed() == true) {
            throw DbConnectionException("Подключение к базе данных закрыто или отсутствует")
        }
    }

    override fun executeQuery(sql: String): ResultSet {
        checkConnection()
        val statement: Statement = connection?.createStatement()!!
        return statement.executeQuery(sql)
    }

    override fun executeUpdate(sql: String): Int {
        checkConnection()
        val statement = connection?.createStatement()!!
        return statement.executeUpdate(sql)
    }

    override fun closeConnection() {
        checkConnection()
        connection?.close()
    }
}