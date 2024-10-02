package org.korsnaike.db

import org.korsnaike.config.Config

abstract class Migration {

    protected lateinit var db: DbInterface;
    fun getDataBase(): DbInterface {
        return db
    }

    init {
        db = PostgreDb.getInstance()
        db.initConnectionsParams(Config)
        db.connect()
    }

    private fun migrateAction(action: String) {
        println("Begin \"${action}\" for ${this.javaClass.name}...")
        db.setAutoCommit(false)
        try {
            if (action == "up") {
                up()
            } else {
                down()
            }

            db.commit()
            println("\"${action}\" for ${this.javaClass.name} have success!")
        } catch (e: Exception) {
            println("Get some errors for \"${action}\" for ${this.javaClass.name}, rollback...")
            db.rollback()
            e.printStackTrace()
        } finally {
            println("End \"${action}\" for ${this.javaClass.name}\n\n")
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