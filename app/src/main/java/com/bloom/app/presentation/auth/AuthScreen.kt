package com.bloom.app.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bloom.app.presentation.components.*
import com.bloom.app.presentation.theme.BloomColors

/**
 * Écran d'authentification
 */
@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateToJournal: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val authMode by viewModel.authMode.collectAsState()

    // Navigation automatique si authentifié
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onNavigateToJournal()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(76.dp))

            // Logo Bloom
            BloomLeafIcon(
                withBackground = true,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Boutons Sign In / Sign Up
            AuthModeSwitcher(
                currentMode = authMode,
                onModeChange = { viewModel.toggleAuthMode() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Champ Email
            BloomEmailField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = "Email Address",
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError,
                enabled = !uiState.isLoading,
                imeAction = androidx.compose.ui.text.input.ImeAction.Next
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Champ Password
            BloomPasswordField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = "Password",
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError,
                enabled = !uiState.isLoading,
                imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                onImeAction = {
                    if (authMode == AuthMode.SIGN_IN) {
                        viewModel.signInWithEmail()
                    } else {
                        viewModel.signUpWithEmail()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Message d'erreur
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = BloomColors.danger500,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Bouton principal Sign In / Sign Up
            BloomButton(
                text = if (authMode == AuthMode.SIGN_IN) "Sign In" else "Sign Up",
                onClick = {
                    if (authMode == AuthMode.SIGN_IN) {
                        viewModel.signInWithEmail()
                    } else {
                        viewModel.signUpWithEmail()
                    }
                },
                style = BloomButtonStyle.PRIMARY,
                size = BloomButtonSize.LARGE,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Divider "or"
            BloomOrDivider()

            Spacer(modifier = Modifier.height(24.dp))

            // Bouton Google Sign In
            BloomButton(
                text = "Continue with Google",
                onClick = onGoogleSignInClick,
                style = BloomButtonStyle.OUTLINED,
                size = BloomButtonSize.MEDIUM,
                icon = Icons.Default.Email, // Remplacer par icône Google si disponible
                enabled = !uiState.isLoading
            )
        }

        // Indicateur de chargement
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                BloomLoadingIndicator(
                    message = if (authMode == AuthMode.SIGN_IN) "Signing in..." else "Creating account..."
                )
            }
        }
    }
}

/**
 * Composant de switch entre Sign In et Sign Up
 */
@Composable
private fun AuthModeSwitcher(
    currentMode: AuthMode,
    onModeChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
        color = BloomColors.neutral200,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            // Bouton Sign In
            BloomButton(
                text = "Sign In",
                onClick = { if (currentMode == AuthMode.SIGN_UP) onModeChange() },
                style = if (currentMode == AuthMode.SIGN_IN) BloomButtonStyle.PRIMARY else BloomButtonStyle.TEXT,
                size = BloomButtonSize.SMALL,
                modifier = Modifier.weight(1f)
            )

            // Bouton Sign Up
            BloomButton(
                text = "Sign Up",
                onClick = { if (currentMode == AuthMode.SIGN_IN) onModeChange() },
                style = if (currentMode == AuthMode.SIGN_UP) BloomButtonStyle.PRIMARY else BloomButtonStyle.TEXT,
                size = BloomButtonSize.SMALL,
                modifier = Modifier.weight(1f)
            )
        }
    }
}