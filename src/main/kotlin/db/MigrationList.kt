package org.korsnaike.db

import java.util.Comparator

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

    fun allDown() {
        migrationsMap?.toSortedMap(Comparator.reverseOrder())
            ?.forEach { it.value.migrateDown() }
        db?.closeConnection()
    }
}