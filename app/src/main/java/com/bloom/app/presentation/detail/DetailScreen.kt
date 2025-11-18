package com.bloom.app.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.bloom.app.presentation.components.*
import com.bloom.app.presentation.theme.BloomColors
import java.io.File

/**
 * Écran de détail d'une découverte
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    discoveryId: String,
    viewModel: DetailViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Charger la découverte au démarrage
    LaunchedEffect(discoveryId) {
        viewModel.loadDiscovery(discoveryId)
    }

    // Navigation automatique après suppression
    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            DetailTopBar(
                onNavigateBack = onNavigateBack,
                onShare = { viewModel.shareDiscovery() },
                onDelete = { viewModel.showDeleteConfirmation(true) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    BloomFullScreenLoader(message = "Loading discovery...")
                }

                uiState.notFound -> {
                    NotFoundState(onNavigateBack = onNavigateBack)
                }

                uiState.discovery != null -> {
                    DetailContent(
                        discovery = uiState.discovery!!,
                        onDeleteClick = { viewModel.showDeleteConfirmation(true) }
                    )
                }
            }

            // Dialog de confirmation de suppression
            if (uiState.showDeleteConfirmation) {
                DeleteConfirmationDialog(
                    onConfirm = {
                        viewModel.showDeleteConfirmation(false)
                        viewModel.deleteDiscovery()
                    },
                    onDismiss = { viewModel.showDeleteConfirmation(false) }
                )
            }
        }
    }
}

/**
 * Barre supérieure
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTopBar(
    onNavigateBack: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    Column {
        TopAppBar(
            title = { },
            navigationIcon = {
                BloomIconButton(
                    icon = Icons.Default.ArrowBack,
                    onClick = onNavigateBack,
                    contentDescription = "Back"
                )
            },
            actions = {
                BloomIconButton(
                    icon = Icons.Default.Share,
                    onClick = onShare,
                    contentDescription = "Share"
                )
                BloomIconButton(
                    icon = Icons.Default.Delete,
                    onClick = onDelete,
                    contentDescription = "Delete"
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BloomColors.white
            )
        )
        BloomDivider()
    }
}

/**
 * Contenu du détail
 */
@Composable
private fun DetailContent(
    discovery: com.bloom.app.domain.model.Discovery,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(24.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BloomColors.neutral100)
        ) {
            Image(
                painter = rememberAsyncImagePainter(File(discovery.imagePath)),
                contentDescription = discovery.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Informations
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // Nom de la plante
            Text(
                text = discovery.name,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                color = BloomColors.neutral900
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = "Discovered: ${discovery.getFormattedDate()}",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = BloomColors.neutral600
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fait amusant
            Text(
                text = discovery.aiSummary,
                fontSize = 16.sp,
                lineHeight = 26.sp,
                color = BloomColors.neutral900
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Bouton Delete Entry
            BloomButton(
                text = "Delete Entry",
                onClick = onDeleteClick,
                style = BloomButtonStyle.DANGER
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Dialog de confirmation de suppression
 */
@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Discovery?",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("This action cannot be undone. The discovery and its image will be permanently deleted.")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = BloomColors.danger500
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * État "non trouvé"
 */
@Composable
private fun NotFoundState(
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier.padding(40.dp)
        ) {
            Text(
                text = "Discovery Not Found",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BloomColors.neutral900
            )

            Spacer(modifier = Modifier.height(16.dp))

            BloomButton(
                text = "Go Back",
                onClick = onNavigateBack,
                style = BloomButtonStyle.PRIMARY
            )
        }
    }
}