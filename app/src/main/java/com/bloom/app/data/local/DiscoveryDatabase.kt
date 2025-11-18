package com.bloom.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de données Room principale de Bloom
 *
 * Gère la persistance locale de toutes les découvertes
 */
@Database(
    entities = [DiscoveryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DiscoveryDatabase : RoomDatabase() {

    /**
     * Fournit l'accès au DAO des découvertes
     */
    abstract fun discoveryDao(): DiscoveryDao

    companion object {
        // Nom de la base de données
        private const val DATABASE_NAME = "bloom_database"

        // Instance singleton (Volatile pour thread-safety)
        @Volatile
        private var INSTANCE: DiscoveryDatabase? = null

        /**
         * Récupère l'instance de la base de données (Singleton Pattern)
         * Thread-safe avec double-checked locking
         *
         * @param context Context de l'application
         * @return Instance unique de la base de données
         */
        fun getInstance(context: Context): DiscoveryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiscoveryDatabase::class.java,
                    DATABASE_NAME
                )
                    // En production, on ne détruit PAS la DB lors des migrations
                    // .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }

        /**
         * Réinitialise l'instance (utile pour les tests)
         */
        fun clearInstance() {
            INSTANCE = null
        }
    }
}