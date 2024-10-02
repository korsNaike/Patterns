package org.korsnaike.db

import org.korsnaike.config.Config

abstract class Migration {

    protected lateinit var db: DbInterface;

    init {
        db = PostgreDb.getInstance()
        db.initConnectionsParams(Config)
        db.connect()
    }

    private fun migrateAction(action: String) {
        db.setAutoCommit(false)
        try {
            if (action == "up") {
                up()
            } else {
                down()
            }

            db.commit()
        } catch (e: Exception) {
            db.rollback()
            print(e.message)
            e.printStackTrace()
        } finally {
            db.closeConnection()
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