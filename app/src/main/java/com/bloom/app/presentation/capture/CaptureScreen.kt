package com.bloom.app.presentation.capture

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloom.app.presentation.components.*
import com.bloom.app.presentation.theme.BloomColors

/**
 * Écran de capture de découverte
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureScreen(
    viewModel: CaptureViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Launcher pour la caméra
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            viewModel.onImageSelected(it, context)
            viewModel.identifyPlant()
        }
    }

    // Launcher pour la galerie
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            // Convertir URI en Bitmap
            val bitmap = android.provider.MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
            viewModel.onImageSelected(bitmap, context)
            viewModel.identifyPlant()
        }
    }

    // Navigation automatique après sauvegarde
    LaunchedEffect(uiState.step) {
        if (uiState.step == CaptureStep.SAVED && uiState.savedDiscoveryId != null) {
            onNavigateToDetail(uiState.savedDiscoveryId!!)
        }
    }

    Scaffold(
        topBar = {
            CaptureTopBar(
                onNavigateBack = {
                    viewModel.cancel()
                    onNavigateBack()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Zone d'aperçu de l'image
                ImagePreviewArea(
                    bitmap = uiState.capturedImage,
                    isAnalyzing = uiState.isAnalyzing
                )

                Spacer(modifier = Modifier.weight(1f))

                // Boutons de capture
                if (uiState.step == CaptureStep.IDLE || uiState.step == CaptureStep.ERROR) {
                    CaptureButtons(
                        onCapturePhoto = { cameraLauncher.launch() },
                        onSelectFromGallery = { galleryLauncher.launch("image/*") }
                    )
                }
            }

            // Overlay de chargement pendant l'analyse
            if (uiState.isAnalyzing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BloomColors.white.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    BloomLoadingIndicator(message = "Analyzing image...")
                }
            }

            // Message d'erreur
            if (uiState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = BloomColors.danger500
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = BloomColors.white
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error!!,
                                fontSize = 14.sp,
                                color = BloomColors.white
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            BloomButton(
                                text = "Try Again",
                                onClick = {
                                    viewModel.clearError()
                                    viewModel.reset()
                                },
                                style = BloomButtonStyle.OUTLINED
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Barre supérieure
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CaptureTopBar(
    onNavigateBack: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "New Discovery",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BloomColors.neutral900
                )
            },
            navigationIcon = {
                BloomIconButton(
                    icon = Icons.Default.ArrowBack,
                    onClick = onNavigateBack,
                    contentDescription = "Back"
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
 * Zone d'aperçu de l'image
 */
@Composable
private fun ImagePreviewArea(
    bitmap: Bitmap?,
    isAnalyzing: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(BloomColors.neutral200),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = "No image selected",
                fontSize = 18.sp,
                color = BloomColors.neutral600
            )
        }
    }
}

/**
 * Boutons de capture
 */
@Composable
private fun CaptureButtons(
    onCapturePhoto: () -> Unit,
    onSelectFromGallery: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bouton Capture Photo
        BloomButton(
            text = "Capture Photo",
            onClick = onCapturePhoto,
            style = BloomButtonStyle.PRIMARY,
            icon = Icons.Default.CameraAlt
        )

        // Bouton Select from Gallery
        BloomButton(
            text = "Select from Gallery",
            onClick = onSelectFromGallery,
            style = BloomButtonStyle.SECONDARY,
            icon = Icons.Default.PhotoLibrary
        )
    }
}