package org.korsnaike.db

import org.korsnaike.config.Config
import java.sql.Connection
import java.sql.ResultSet

interface DbInterface {

    /**
     * Провести инициализацию параметров для подключения к базе данных
     */
    fun initConnectionsParams(config: Config)

    /**
     * Выполнить подключение к базе данных
     */
    fun connect()

    /**
     * Установить значение авто-коммита
     */
    fun setAutoCommit(autoCommit: Boolean)

    /**
     * Коммит транзакции
     */
    fun commit()

    /**
     * Откат транзакции
     */
    fun rollback()

    /**
     * Выполнить запрос к базе данных и вернуть результат ResultSet
     */
    fun executeQuery(sql: String): ResultSet

    /**
     * Выполнить запрос на обновление и вернуть результат в количестве изменённых объектов
     */
    fun executeUpdate(sql: String): Int

    /**
     * Закрыть подключение к базе данных
     */
    fun closeConnection()

    fun getConn(): Connection?
}