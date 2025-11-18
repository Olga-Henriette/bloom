package com.bloom.app.domain.usecase

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.bloom.app.data.remote.GeminiService
import com.bloom.app.data.remote.PlantIdentification
import com.bloom.app.data.repository.DiscoveryRepository
import com.bloom.app.domain.model.Discovery
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.util.UUID

/**
 * Use Case : Identifier une plante/insecte
 *
 * Utilise Gemini AI pour identifier l'objet dans l'image
 */
class IdentifyPlantUseCase(
    private val geminiService: GeminiService
) {
    suspend operator fun invoke(bitmap: Bitmap): Result<PlantIdentification> {
        return geminiService.identifyPlant(bitmap)
    }
}

/**
 * Use Case : Sauvegarder une découverte
 *
 * Crée et sauvegarde une nouvelle découverte dans la base de données
 */
class SaveDiscoveryUseCase(
    private val repository: DiscoveryRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        name: String,
        aiSummary: String,
        imagePath: String,
        userId: String
    ): Result<Discovery> {
        return try {
            val discovery = Discovery(
                id = UUID.randomUUID().toString(),
                name = name,
                aiSummary = aiSummary,
                imagePath = imagePath,
                timestamp = LocalDateTime.now(),
                userId = userId
            )

            repository.saveDiscovery(discovery).fold(
                onSuccess = { Result.success(discovery) },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Use Case : Récupérer toutes les découvertes
 *
 * Obtient la liste de toutes les découvertes d'un utilisateur
 */
class GetDiscoveriesUseCase(
    private val repository: DiscoveryRepository
) {
    operator fun invoke(userId: String): Flow<List<Discovery>> {
        return repository.getAllDiscoveries(userId)
    }
}

/**
 * Use Case : Récupérer une découverte par ID
 *
 * Obtient les détails d'une découverte spécifique
 */
class GetDiscoveryByIdUseCase(
    private val repository: DiscoveryRepository
) {
    operator fun invoke(id: String): Flow<Discovery?> {
        return repository.getDiscoveryById(id)
    }
}

/**
 * Use Case : Supprimer une découverte
 *
 * Supprime une découverte et son image associée
 */
class DeleteDiscoveryUseCase(
    private val repository: DiscoveryRepository
) {
    suspend operator fun invoke(discoveryId: String): Result<Unit> {
        return repository.deleteDiscoveryById(discoveryId)
    }
}

/**
 * Use Case : Rechercher des découvertes
 *
 * Recherche des découvertes par nom
 */
class SearchDiscoveriesUseCase(
    private val repository: DiscoveryRepository
) {
    operator fun invoke(userId: String, query: String): Flow<List<Discovery>> {
        return repository.searchDiscoveries(userId, query)
    }
}

/**
 * Use Case : Obtenir les statistiques
 *
 * Obtient le nombre total de découvertes
 */
class GetDiscoveryStatsUseCase(
    private val repository: DiscoveryRepository
) {
    suspend operator fun invoke(userId: String): Result<DiscoveryStats> {
        return try {
            val count = repository.getDiscoveryCount(userId)
            Result.success(DiscoveryStats(totalDiscoveries = count))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Statistiques des découvertes
 */
data class DiscoveryStats(
    val totalDiscoveries: Int
)