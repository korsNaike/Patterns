package org.korsnaike.db

import org.korsnaike.config.Config
import org.korsnaike.db.migrationstore.JsonMigrationStore
import org.korsnaike.db.migrationstore.MigrationStoreInterface

/**
 * Базовый класс для миграций.
 */
abstract class Migration {

    protected lateinit var db: DbInterface;
    private lateinit var migrationStore: MigrationStoreInterface

    fun getDataBase(): DbInterface {
        return db
    }

    init {
        db = PostgreDb.getInstance()
        db.initConnectionsParams(Config)
        db.connect()

        migrationStore = JsonMigrationStore()
    }

    private fun migrateAction(action: String) {
        println("Begin \"${action}\" for ${this.javaClass.simpleName}...")
        db.setAutoCommit(false)
        try {
            if (action == "up") {
                up()
            } else {
                down()
            }

            if (action == "up") {
                migrationStore.storeUpMigrate(this.javaClass.simpleName)
            } else {
                migrationStore.removeMigrationByName(this.javaClass.simpleName)
            }

            db.commit()
            println("\"${action}\" for ${this.javaClass.simpleName} have success!")
        } catch (e: Exception) {
            println("Get some errors for \"${action}\" for ${this.javaClass.simpleName}, rollback...")
            db.rollback()
            e.printStackTrace()
        } finally {
            println("End \"${action}\" for ${this.javaClass.simpleName}\n\n")
        }
    }

    fun migrateUp() {
        migrateAction("up")
    }

    fun migrateDown() {
        migrateAction("down")
    }

    abstract protected fun up()

    abstract protected fun down()

}