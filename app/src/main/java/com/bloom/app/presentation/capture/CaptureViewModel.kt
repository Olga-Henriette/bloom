package com.bloom.app.presentation.capture

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloom.app.data.remote.FirebaseAuthService
import com.bloom.app.domain.usecase.IdentifyPlantUseCase
import com.bloom.app.domain.usecase.SaveDiscoveryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * ViewModel pour l'écran de capture
 */
class CaptureViewModel(
    private val identifyPlantUseCase: IdentifyPlantUseCase,
    private val saveDiscoveryUseCase: SaveDiscoveryUseCase,
    private val authService: FirebaseAuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow(CaptureUiState())
    val uiState: StateFlow<CaptureUiState> = _uiState.asStateFlow()

    /**
     * Image capturée sélectionnée
     */
    fun onImageSelected(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            // Sauvegarder l'image localement
            val imagePath = saveImageLocally(bitmap, context)

            _uiState.value = _uiState.value.copy(
                capturedImage = bitmap,
                imagePath = imagePath,
                step = CaptureStep.IMAGE_CAPTURED
            )
        }
    }

    /**
     * Identifie la plante avec l'IA
     */
    fun identifyPlant() {
        val bitmap = _uiState.value.capturedImage ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                step = CaptureStep.ANALYZING,
                isAnalyzing = true
            )

            identifyPlantUseCase(bitmap).fold(
                onSuccess = { identification ->
                    _uiState.value = _uiState.value.copy(
                        plantName = identification.name,
                        funFact = identification.funFact,
                        isAnalyzing = false,
                        step = CaptureStep.IDENTIFIED
                    )

                    // Sauvegarder automatiquement
                    saveDiscovery()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        error = exception.message ?: "Failed to identify",
                        step = CaptureStep.ERROR
                    )
                }
            )
        }
    }

    /**
     * Sauvegarde la découverte
     */
    private fun saveDiscovery() {
        val userId = authService.getCurrentUserId() ?: return
        val imagePath = _uiState.value.imagePath ?: return
        val plantName = _uiState.value.plantName ?: return
        val funFact = _uiState.value.funFact ?: return

        viewModelScope.launch {
            saveDiscoveryUseCase(
                name = plantName,
                aiSummary = funFact,
                imagePath = imagePath,
                userId = userId
            ).fold(
                onSuccess = { discovery ->
                    _uiState.value = _uiState.value.copy(
                        step = CaptureStep.SAVED,
                        savedDiscoveryId = discovery.id
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to save",
                        step = CaptureStep.ERROR
                    )
                }
            )
        }
    }

    /**
     * Sauvegarde l'image localement
     */
    private fun saveImageLocally(bitmap: Bitmap, context: Context): String {
        val fileName = "discovery_${UUID.randomUUID()}.jpg"
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }

        return file.absolutePath
    }

    /**
     * Réinitialise pour une nouvelle capture
     */
    fun reset() {
        _uiState.value = CaptureUiState()
    }

    /**
     * Annule la capture
     */
    fun cancel() {
        _uiState.value = CaptureUiState()
    }

    /**
     * Efface l'erreur
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * État de l'UI de capture
 */
data class CaptureUiState(
    val capturedImage: Bitmap? = null,
    val imagePath: String? = null,
    val plantName: String? = null,
    val funFact: String? = null,
    val isAnalyzing: Boolean = false,
    val step: CaptureStep = CaptureStep.IDLE,
    val error: String? = null,
    val savedDiscoveryId: String? = null
)

/**
 * Étapes du processus de capture
 */
enum class CaptureStep {
    IDLE,              // Pas encore d'image
    IMAGE_CAPTURED,    // Image sélectionnée
    ANALYZING,         // En cours d'analyse IA
    IDENTIFIED,        // Identification réussie
    SAVED,             // Sauvegardée en base
    ERROR              // Erreur survenue
}