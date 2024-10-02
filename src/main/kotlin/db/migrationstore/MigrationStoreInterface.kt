package org.korsnaike.db.migrationstore

/**
 * Интерфейс для работы с хранилищем миграций.
 *
 * Место, где сохраняется информация о пройденных миграциях.
 */
interface MigrationStoreInterface {

    /**
     * Записать информацию о прохождении миграции
     */
    fun storeUpMigrate(migrationName: String)

    /**
     * Удалить прохождение миграции (при откате)
     */
    fun removeMigrationByName(migrationName: String)

    /**
     * Получить актуальный список выполненных миграций
     */
    fun getAlreadyUpMigrationNames(): List<String>
}