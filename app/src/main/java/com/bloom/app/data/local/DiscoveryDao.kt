package com.bloom.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) pour les découvertes
 *
 * Interface pour toutes les opérations de base de données
 */
@Dao
interface DiscoveryDao {

    /**
     * Récupère toutes les découvertes d'un utilisateur
     * Triées par date (plus récentes en premier)
     *
     * @param userId ID de l'utilisateur
     * @return Flow qui émet la liste mise à jour automatiquement
     */
    @Query("SELECT * FROM discoveries WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllDiscoveriesByUser(userId: String): Flow<List<DiscoveryEntity>>

    /**
     * Récupère une découverte spécifique par son ID
     *
     * @param id ID de la découverte
     * @return Flow qui émet la découverte ou null
     */
    @Query("SELECT * FROM discoveries WHERE id = :id")
    fun getDiscoveryById(id: String): Flow<DiscoveryEntity?>

    /**
     * Insère une nouvelle découverte
     * Si une découverte avec le même ID existe, elle sera remplacée
     *
     * @param discovery La découverte à insérer
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscovery(discovery: DiscoveryEntity)

    /**
     * Insère plusieurs découvertes en une seule transaction
     *
     * @param discoveries Liste des découvertes à insérer
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscoveries(discoveries: List<DiscoveryEntity>)

    /**
     * Met à jour une découverte existante
     *
     * @param discovery La découverte à mettre à jour
     */
    @Update
    suspend fun updateDiscovery(discovery: DiscoveryEntity)

    /**
     * Supprime une découverte
     *
     * @param discovery La découverte à supprimer
     */
    @Delete
    suspend fun deleteDiscovery(discovery: DiscoveryEntity)

    /**
     * Supprime une découverte par son ID
     *
     * @param id ID de la découverte à supprimer
     */
    @Query("DELETE FROM discoveries WHERE id = :id")
    suspend fun deleteDiscoveryById(id: String)

    /**
     * Supprime toutes les découvertes d'un utilisateur
     *
     * @param userId ID de l'utilisateur
     */
    @Query("DELETE FROM discoveries WHERE userId = :userId")
    suspend fun deleteAllDiscoveriesByUser(userId: String)

    /**
     * Compte le nombre de découvertes d'un utilisateur
     *
     * @param userId ID de l'utilisateur
     * @return Nombre de découvertes
     */
    @Query("SELECT COUNT(*) FROM discoveries WHERE userId = :userId")
    suspend fun getDiscoveryCount(userId: String): Int

    /**
     * Recherche des découvertes par nom
     *
     * @param userId ID de l'utilisateur
     * @param searchQuery Terme de recherche
     * @return Flow avec les résultats filtrés
     */
    @Query("SELECT * FROM discoveries WHERE userId = :userId AND name LIKE '%' || :searchQuery || '%' ORDER BY timestamp DESC")
    fun searchDiscoveries(userId: String, searchQuery: String): Flow<List<DiscoveryEntity>>
}