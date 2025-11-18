package com.bloom.app.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Modèle de domaine pour une découverte
 *
 * Représente une plante, fleur ou insecte identifié par l'utilisateur
 */
data class Discovery(
    val id: String,
    val name: String,              // Nom formel identifié par l'IA
    val aiSummary: String,         // Fait amusant en 2 phrases généré par l'IA
    val imagePath: String,         // Chemin local vers l'image capturée
    val timestamp: LocalDateTime,  // Date et heure de la découverte
    val userId: String             // ID de l'utilisateur qui a fait la découverte
) {
    /**
     * Formate la date pour l'affichage
     * Format: "November 13, 2023 at 02:45 PM"
     */
    fun getFormattedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")
        return timestamp.format(formatter)
    }

    /**
     * Formate la date courte pour les cartes
     * Format: "10/22/2023"
     */
    fun getShortDate(): String {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return timestamp.format(formatter)
    }

    /**
     * Vérifie si la découverte est récente (moins de 24h)
     */
    fun isRecent(): Boolean {
        val now = LocalDateTime.now()
        val hoursDiff = java.time.Duration.between(timestamp, now).toHours()
        return hoursDiff < 24
    }
}