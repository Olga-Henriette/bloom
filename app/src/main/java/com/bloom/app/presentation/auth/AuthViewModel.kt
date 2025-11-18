package com.bloom.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloom.app.data.remote.FirebaseAuthService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel pour l'écran d'authentification
 */
class AuthViewModel(
    private val authService: FirebaseAuthService
) : ViewModel() {

    // État de l'UI
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Mode actuel (Sign In ou Sign Up)
    private val _authMode = MutableStateFlow(AuthMode.SIGN_IN)
    val authMode: StateFlow<AuthMode> = _authMode.asStateFlow()

    init {
        // Observer l'état d'authentification
        viewModelScope.launch {
            authService.authStateFlow.collect { user ->
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = user != null
                )
            }
        }
    }

    /**
     * Met à jour l'email
     */
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null
        )
    }

    /**
     * Met à jour le mot de passe
     */
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null
        )
    }

    /**
     * Change le mode (Sign In ↔ Sign Up)
     */
    fun toggleAuthMode() {
        _authMode.value = if (_authMode.value == AuthMode.SIGN_IN) {
            AuthMode.SIGN_UP
        } else {
            AuthMode.SIGN_IN
        }
        clearErrors()
    }

    /**
     * Validation des champs
     */
    private fun validateFields(): Boolean {
        var isValid = true

        // Validation email
        if (_uiState.value.email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Email is required")
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
            _uiState.value = _uiState.value.copy(emailError = "Invalid email format")
            isValid = false
        }

        // Validation password
        if (_uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "Password is required")
            isValid = false
        } else if (_uiState.value.password.length < 6) {
            _uiState.value = _uiState.value.copy(passwordError = "Password must be at least 6 characters")
            isValid = false
        }

        return isValid
    }

    /**
     * Connexion avec Email/Password
     */
    fun signInWithEmail() {
        if (!validateFields()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            authService.signInWithEmail(
                email = _uiState.value.email,
                password = _uiState.value.password
            ).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Sign in failed"
                    )
                }
            )
        }
    }

    /**
     * Inscription avec Email/Password
     */
    fun signUpWithEmail() {
        if (!validateFields()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            authService.signUpWithEmail(
                email = _uiState.value.email,
                password = _uiState.value.password
            ).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Sign up failed"
                    )
                }
            )
        }
    }

    /**
     * Connexion avec Google
     */
    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            authService.signInWithGoogle(account).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Google sign in failed"
                    )
                }
            )
        }
    }

    /**
     * Efface les erreurs
     */
    private fun clearErrors() {
        _uiState.value = _uiState.value.copy(
            emailError = null,
            passwordError = null,
            error = null
        )
    }

    /**
     * Efface l'erreur générale
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * État de l'UI d'authentification
 */
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

/**
 * Mode d'authentification
 */
enum class AuthMode {
    SIGN_IN,
    SIGN_UP
}