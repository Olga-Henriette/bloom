package com.bloom.app.data.repository

import com.bloom.app.data.local.DiscoveryDao
import com.bloom.app.data.local.DiscoveryEntity
import com.bloom.app.domain.model.Discovery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository pour les découvertes
 *
 * Couche d'abstraction entre les ViewModels et les sources de données
 * Implémente le Repository Pattern pour une architecture propre
 */
class DiscoveryRepository(
    private val discoveryDao: DiscoveryDao
) {

    /**
     * Récupère toutes les découvertes d'un utilisateur
     *
     * @param userId ID de l'utilisateur connecté
     * @return Flow de la liste des découvertes (modèle domaine)
     */
    fun getAllDiscoveries(userId: String): Flow<List<Discovery>> {
        return discoveryDao.getAllDiscoveriesByUser(userId)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    /**
     * Récupère une découverte spécifique
     *
     * @param id ID de la découverte
     * @return Flow de la découverte ou null si non trouvée
     */
    fun getDiscoveryById(id: String): Flow<Discovery?> {
        return discoveryDao.getDiscoveryById(id)
            .map { it?.toDomain() }
    }

    /**
     * Sauvegarde une nouvelle découverte
     *
     * @param discovery La découverte à sauvegarder
     * @return Result.success si OK, Result.failure si erreur
     */
    suspend fun saveDiscovery(discovery: Discovery): Result<Unit> {
        return try {
            val entity = DiscoveryEntity.fromDomain(discovery)
            discoveryDao.insertDiscovery(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sauvegarde plusieurs découvertes
     *
     * @param discoveries Liste des découvertes à sauvegarder
     * @return Result.success si OK, Result.failure si erreur
     */
    suspend fun saveDiscoveries(discoveries: List<Discovery>): Result<Unit> {
        return try {
            val entities = discoveries.map { DiscoveryEntity.fromDomain(it) }
            discoveryDao.insertDiscoveries(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Met à jour une découverte existante
     *
     * @param discovery La découverte mise à jour
     * @return Result.success si OK, Result.failure si erreur
     */
    suspend fun updateDiscovery(discovery: Discovery): Result<Unit> {
        return try {
            val entity = DiscoveryEntity.fromDomain(discovery)
            discoveryDao.updateDiscovery(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Supprime une découverte
     *
     * @param discovery La découverte à supprimer
     * @return Result.success si OK, Result.failure si erreur
     */
    suspend fun deleteDiscovery(discovery: Discovery): Result<Unit> {
        return try {
            val entity = DiscoveryEntity.fromDomain(discovery)
            discoveryDao.deleteDiscovery(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Supprime une découverte par son ID
     *
     * @param id ID de la découverte à supprimer
     * @return Result.success si OK, Result.failure si erreur
     */
    suspend fun deleteDiscoveryById(id: String): Result<Unit> {
        return try {
            discoveryDao.deleteDiscoveryById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Supprime toutes les découvertes d'un utilisateur
     *
     * @param userId ID de l'utilisateur
     * @return Result.success si OK, Result.failure si erreur
     */
    suspend fun deleteAllDiscoveries(userId: String): Result<Unit> {
        return try {
            discoveryDao.deleteAllDiscoveriesByUser(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtient le nombre de découvertes
     *
     * @param userId ID de l'utilisateur
     * @return Nombre de découvertes ou 0 si erreur
     */
    suspend fun getDiscoveryCount(userId: String): Int {
        return try {
            discoveryDao.getDiscoveryCount(userId)
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Recherche des découvertes par nom
     *
     * @param userId ID de l'utilisateur
     * @param query Terme de recherche
     * @return Flow des découvertes correspondantes
     */
    fun searchDiscoveries(userId: String, query: String): Flow<List<Discovery>> {
        return discoveryDao.searchDiscoveries(userId, query)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }
}