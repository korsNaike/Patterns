package org.korsnaike.db.migrationstore

import kotlinx.serialization.Serializable

@Serializable
data class MigrationData(val name: String, val date: String)
