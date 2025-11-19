package com.bloom.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bloom.app.data.local.DiscoveryDatabase
import com.bloom.app.data.remote.FirebaseAuthService
import com.bloom.app.data.remote.GeminiService
import com.bloom.app.data.repository.DiscoveryRepository
import com.bloom.app.domain.usecase.*
import com.bloom.app.presentation.auth.AuthScreen
import com.bloom.app.presentation.auth.AuthViewModel
import com.bloom.app.presentation.capture.CaptureScreen
import com.bloom.app.presentation.capture.CaptureViewModel
import com.bloom.app.presentation.detail.DetailScreen
import com.bloom.app.presentation.detail.DetailViewModel
import com.bloom.app.presentation.journal.JournalScreen
import com.bloom.app.presentation.journal.JournalViewModel

/**
 * Routes de navigation
 */
sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Journal : Screen("journal")
    object Capture : Screen("capture")
    object Detail : Screen("detail/{discoveryId}") {
        fun createRoute(discoveryId: String) = "detail/$discoveryId"
    }
}

/**
 * Navigation principale de Bloom
 */
@Composable
fun BloomNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Auth.route
) {
    val context = LocalContext.current

    // Initialisation des services et use cases
    val authService = remember { FirebaseAuthService() }
    val geminiService = remember { GeminiService() }

    val database = remember { DiscoveryDatabase.getInstance(context) }
    val repository = remember { DiscoveryRepository(database.discoveryDao()) }

    // Use Cases
    val getDiscoveriesUseCase = remember { GetDiscoveriesUseCase(repository) }
    val getDiscoveryByIdUseCase = remember { GetDiscoveryByIdUseCase(repository) }
    val identifyPlantUseCase = remember { IdentifyPlantUseCase(geminiService) }
    val saveDiscoveryUseCase = remember { SaveDiscoveryUseCase(repository) }
    val deleteDiscoveryUseCase = remember { DeleteDiscoveryUseCase(repository) }
    val searchDiscoveriesUseCase = remember { SearchDiscoveriesUseCase(repository) }

    // ViewModels
    val authViewModel = remember {
        AuthViewModel(authService)
    }

    val journalViewModel = remember {
        JournalViewModel(
            getDiscoveriesUseCase = getDiscoveriesUseCase,
            searchDiscoveriesUseCase = searchDiscoveriesUseCase,
            authService = authService
        )
    }

    val captureViewModel = remember {
        CaptureViewModel(
            identifyPlantUseCase = identifyPlantUseCase,
            saveDiscoveryUseCase = saveDiscoveryUseCase,
            authService = authService
        )
    }

    val detailViewModel = remember {
        DetailViewModel(
            getDiscoveryByIdUseCase = getDiscoveryByIdUseCase,
            deleteDiscoveryUseCase = deleteDiscoveryUseCase
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Écran d'authentification
        composable(Screen.Auth.route) {
            AuthScreen(
                viewModel = authViewModel,
                onNavigateToJournal = {
                    navController.navigate(Screen.Journal.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {
                    // TODO: Implémenter Google Sign-In
                }
            )
        }

        // Écran Journal (liste)
        composable(Screen.Journal.route) {
            JournalScreen(
                viewModel = journalViewModel,
                onNavigateToCapture = {
                    navController.navigate(Screen.Capture.route)
                },
                onNavigateToDetail = { discoveryId ->
                    navController.navigate(Screen.Detail.createRoute(discoveryId))
                }
            )
        }

        // Écran de capture
        composable(Screen.Capture.route) {
            CaptureScreen(
                viewModel = captureViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDetail = { discoveryId ->
                    navController.navigate(Screen.Detail.createRoute(discoveryId)) {
                        popUpTo(Screen.Journal.route)
                    }
                }
            )
        }

        // Écran de détail
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("discoveryId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val discoveryId = backStackEntry.arguments?.getString("discoveryId") ?: return@composable

            DetailScreen(
                discoveryId = discoveryId,
                viewModel = detailViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}