package org.korsnaike.db.migrationstore

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JsonMigrationStore : MigrationStoreInterface {

    private val pathToFile = "src/files/migrations.json"
    private val json = Json { prettyPrint = true }

    private fun readMigrations(): MutableList<MigrationData> {
        return if (File(pathToFile).exists()) {
            val jsonString = File(pathToFile).readText()
            json.decodeFromString<MutableList<MigrationData>>(jsonString)
        } else {
            mutableListOf()
        }
    }

    private fun writeMigrations(migrations: List<MigrationData>) {
        val jsonString = json.encodeToString(migrations)
        File(pathToFile).writeText(jsonString)
    }

    override fun storeUpMigrate(migrationName: String) {
        val migrations = readMigrations()
        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        migrations.add(MigrationData(migrationName, currentDate))
        writeMigrations(migrations)
    }

    override fun removeMigrationByName(migrationName: String) {
        val migrations = readMigrations()
        val filteredMigrations = migrations.filter { it.name != migrationName }
        writeMigrations(filteredMigrations)
    }

    override fun getAlreadyUpMigrationNames(): List<String> {
        val migrations = readMigrations()
        return migrations.map { it.name }
    }
}