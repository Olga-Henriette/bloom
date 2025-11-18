package com.bloom.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bloom.app.domain.model.Discovery
import java.time.LocalDateTime

/**
 * Entité Room pour la table des découvertes
 *
 * Cette classe représente la structure de la table dans SQLite
 */
@Entity(tableName = "discoveries")
data class DiscoveryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val aiSummary: String,
    val imagePath: String,
    val timestamp: Long,  // Stocké en millisecondes (epoch time)
    val userId: String
) {
    /**
     * Convertit l'entité en modèle de domaine
     */
    fun toDomain(): Discovery {
        return Discovery(
            id = id,
            name = name,
            aiSummary = aiSummary,
            imagePath = imagePath,
            timestamp = LocalDateTime.ofEpochSecond(
                timestamp / 1000,
                ((timestamp % 1000) * 1000000).toInt(),
                java.time.ZoneOffset.UTC
            ),
            userId = userId
        )
    }

    companion object {
        /**
         * Crée une entité depuis un modèle de domaine
         */
        fun fromDomain(discovery: Discovery): DiscoveryEntity {
            return DiscoveryEntity(
                id = discovery.id,
                name = discovery.name,
                aiSummary = discovery.aiSummary,
                imagePath = discovery.imagePath,
                timestamp = discovery.timestamp.atZone(java.time.ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli(),
                userId = discovery.userId
            )
        }
    }
}