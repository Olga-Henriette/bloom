package com.bloom.app.presentation.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloom.app.data.remote.FirebaseAuthService
import com.bloom.app.domain.model.Discovery
import com.bloom.app.domain.usecase.GetDiscoveriesUseCase
import com.bloom.app.domain.usecase.SearchDiscoveriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel pour l'écran Journal (liste des découvertes)
 */
class JournalViewModel(
    private val getDiscoveriesUseCase: GetDiscoveriesUseCase,
    private val searchDiscoveriesUseCase: SearchDiscoveriesUseCase,
    private val authService: FirebaseAuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadDiscoveries()
    }

    /**
     * Charge toutes les découvertes de l'utilisateur
     */
    private fun loadDiscoveries() {
        val userId = authService.getCurrentUserId() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getDiscoveriesUseCase(userId).collect { discoveries ->
                _uiState.value = _uiState.value.copy(
                    discoveries = discoveries,
                    isLoading = false,
                    isEmpty = discoveries.isEmpty()
                )
            }
        }
    }

    /**
     * Met à jour la recherche
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            loadDiscoveries()
        } else {
            searchDiscoveries(query)
        }
    }

    /**
     * Recherche des découvertes
     */
    private fun searchDiscoveries(query: String) {
        val userId = authService.getCurrentUserId() ?: return

        viewModelScope.launch {
            searchDiscoveriesUseCase(userId, query).collect { discoveries ->
                _uiState.value = _uiState.value.copy(
                    discoveries = discoveries,
                    isEmpty = discoveries.isEmpty()
                )
            }
        }
    }

    /**
     * Rafraîchit la liste
     */
    fun refresh() {
        loadDiscoveries()
    }

    /**
     * Déconnexion
     */
    fun signOut() {
        authService.signOut()
    }
}

/**
 * État de l'UI du journal
 */
data class JournalUiState(
    val discoveries: List<Discovery> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null
)