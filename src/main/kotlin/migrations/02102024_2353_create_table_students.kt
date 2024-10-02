package org.korsnaike.migrations

import org.korsnaike.db.Migration

class `02102024_2353_create_table_students` : Migration() {
    override fun up() {
        db.executeUpdate(
            "CREATE TABLE student (\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    first_name VARCHAR(50) NOT NULL,\n" +
                    "    last_name VARCHAR(50) NOT NULL,\n" +
                    "    middle_name VARCHAR(50) NOT NULL,\n" +
                    "    email VARCHAR(255) UNIQUE NULL,\n" +
                    "    telegram VARCHAR(255) UNIQUE NULL,\n" +
                    "    phone VARCHAR(255) UNIQUE NULL,\n" +
                    "    git VARCHAR(255) UNIQUE NULL,\n" +
                    "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                    ")"
        )
    }

    override fun down() {
        db.executeUpdate("DROP TABLE student")
    }
}