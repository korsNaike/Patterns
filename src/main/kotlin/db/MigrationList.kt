package org.korsnaike.db

import java.util.Comparator

/**
 * Класс для работы со списком миграций
 */
class MigrationList(private var migrations: List<Migration>) {

    private var migrationsMap: Map<String, Migration>? = null;
    private var db: DbInterface? = null;

    init {
        db = migrations.first().getDataBase()
        migrationsMap = migrations.associateBy { it.javaClass.name }.toSortedMap()
    }

    fun allUp() {
        migrationsMap?.forEach { it.value.migrateUp() }
        db?.closeConnection()
    }

    fun allDown(count: Int? = null) {
        if (count != null) {
            require(count > 0)
        }

        if (count == null) {
            migrationsMap?.toSortedMap(Comparator.reverseOrder())
                ?.forEach { it.value.migrateDown() }
        } else {
            migrationsMap?.toSortedMap(Comparator.reverseOrder())
                ?.asSequence()
                ?.take(count)
                ?.forEach { it.value.migrateDown() }
        }

        db?.closeConnection()
    }
}