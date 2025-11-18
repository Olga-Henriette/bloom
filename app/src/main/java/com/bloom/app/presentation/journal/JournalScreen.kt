package com.bloom.app.presentation.journal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloom.app.presentation.components.*
import com.bloom.app.presentation.theme.BloomColors

/**
 * Écran Journal - Liste des découvertes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    viewModel: JournalViewModel,
    onNavigateToCapture: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            JournalTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.onSearchQueryChange(it) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCapture,
                containerColor = BloomColors.primary500,
                contentColor = BloomColors.primary800
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Discovery",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    // Chargement
                    BloomFullScreenLoader(message = "Loading discoveries...")
                }

                uiState.isEmpty -> {
                    // Liste vide
                    EmptyJournalState(onAddClick = onNavigateToCapture)
                }

                else -> {
                    // Liste des découvertes
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 24.dp,
                            end = 24.dp,
                            top = 16.dp,
                            bottom = 88.dp // Espace pour le FAB
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = uiState.discoveries,
                            key = { it.id }
                        ) { discovery ->
                            DiscoveryCard(
                                plantName = discovery.name,
                                imagePath = discovery.imagePath,
                                date = discovery.getShortDate(),
                                onClick = { onNavigateToDetail(discovery.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Barre supérieure avec recherche
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JournalTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Discovery Journal",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BloomColors.neutral900
                )
            },
            actions = {
                BloomIconButton(
                    icon = Icons.Default.Search,
                    onClick = { /* TODO: Toggle search bar */ },
                    contentDescription = "Search"
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
 * État vide du journal
 */
@Composable
private fun EmptyJournalState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(40.dp)
        ) {
            BloomLeafIcon(
                withBackground = true,
                tint = BloomColors.neutral600
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "No discoveries yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BloomColors.neutral900
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap the + button to start discovering plants and insects!",
                fontSize = 14.sp,
                color = BloomColors.neutral600,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            BloomButton(
                text = "Start Discovering",
                onClick = onAddClick,
                style = BloomButtonStyle.PRIMARY,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }
    }
}