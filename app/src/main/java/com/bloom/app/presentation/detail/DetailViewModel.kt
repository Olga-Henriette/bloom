package com.bloom.app.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloom.app.domain.model.Discovery
import com.bloom.app.domain.usecase.DeleteDiscoveryUseCase
import com.bloom.app.domain.usecase.GetDiscoveryByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * ViewModel pour l'écran de détail d'une découverte
 */
class DetailViewModel(
    private val getDiscoveryByIdUseCase: GetDiscoveryByIdUseCase,
    private val deleteDiscoveryUseCase: DeleteDiscoveryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    /**
     * Charge les détails d'une découverte
     */
    fun loadDiscovery(discoveryId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getDiscoveryByIdUseCase(discoveryId).collect { discovery ->
                if (discovery != null) {
                    _uiState.value = _uiState.value.copy(
                        discovery = discovery,
                        isLoading = false,
                        notFound = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        notFound = true
                    )
                }
            }
        }
    }

    /**
     * Supprime la découverte
     */
    fun deleteDiscovery() {
        val discovery = _uiState.value.discovery ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeleting = true)

            deleteDiscoveryUseCase(discovery.id).fold(
                onSuccess = {
                    // Supprimer aussi le fichier image
                    try {
                        File(discovery.imagePath).delete()
                    } catch (e: Exception) {
                        // Ignorer si le fichier n'existe pas
                    }

                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        isDeleted = true
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        error = exception.message ?: "Failed to delete"
                    )
                }
            )
        }
    }

    /**
     * Partage la découverte (placeholder pour future implémentation)
     */
    fun shareDiscovery() {
        // TODO: Implémenter le partage
        // Peut être fait via Intent Android pour partager l'image + texte
    }

    /**
     * Efface l'erreur
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Affiche/masque la confirmation de suppression
     */
    fun showDeleteConfirmation(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDeleteConfirmation = show)
    }
}

/**
 * État de l'UI de détail
 */
data class DetailUiState(
    val discovery: Discovery? = null,
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false,
    val notFound: Boolean = false,
    val error: String? = null,
    val showDeleteConfirmation: Boolean = false
)