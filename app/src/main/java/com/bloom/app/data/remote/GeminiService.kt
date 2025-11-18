package com.bloom.app.data.remote

import android.graphics.Bitmap
import com.bloom.app.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

/**
 * Service Gemini AI pour l'identification
 *
 * Utilise l'API Gemini pour identifier les plantes, fleurs et insectes
 * et générer des faits amusants
 */
class GeminiService {

    // Modèle Gemini Pro Vision (pour les images)
    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    /**
     * Identifie une plante, fleur ou insecte depuis une image
     *
     * @param bitmap Image capturée à analyser
     * @return Result avec PlantIdentification ou erreur
     */
    suspend fun identifyPlant(bitmap: Bitmap): Result<PlantIdentification> {
        return try {
            val prompt = """
                Identify this object in the image. It could be a plant, flower, or insect.
                
                Provide your response in the following format:
                NAME: [The formal name of the object]
                FACT: [A fun, interesting two-sentence fact about it]
                
                Be concise and engaging. The fact should be educational but entertaining.
            """.trimIndent()

            val content = content {
                image(bitmap)
                text(prompt)
            }

            val response = model.generateContent(content)
            val responseText = response.text ?: throw Exception("Empty response from AI")

            // Parse la réponse
            val identification = parseIdentificationResponse(responseText)

            Result.success(identification)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to identify: ${e.message}", e))
        }
    }

    /**
     * Parse la réponse de l'IA
     *
     * Format attendu :
     * NAME: Monstera Deliciosa
     * FACT: This is a fun fact about the plant. It spans two sentences.
     */
    private fun parseIdentificationResponse(response: String): PlantIdentification {
        val lines = response.trim().lines()

        var name = ""
        var fact = ""

        for (line in lines) {
            when {
                line.startsWith("NAME:", ignoreCase = true) -> {
                    name = line.substringAfter("NAME:", "").trim()
                }
                line.startsWith("FACT:", ignoreCase = true) -> {
                    fact = line.substringAfter("FACT:", "").trim()
                }
            }
        }

        // Validation
        if (name.isEmpty()) {
            throw Exception("Failed to parse name from AI response")
        }

        if (fact.isEmpty()) {
            throw Exception("Failed to parse fact from AI response")
        }

        return PlantIdentification(
            name = name,
            funFact = fact
        )
    }

    /**
     * Génère un fait amusant alternatif pour une plante connue
     *
     * @param plantName Nom de la plante
     * @return Result avec un nouveau fait ou erreur
     */
    suspend fun generateAlternativeFact(plantName: String): Result<String> {
        return try {
            val prompt = """
                Generate a fun, interesting two-sentence fact about $plantName.
                Make it educational but entertaining. Different from common facts.
            """.trimIndent()

            val response = model.generateContent(prompt)
            val fact = response.text ?: throw Exception("Empty response from AI")

            Result.success(fact.trim())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Résultat de l'identification
 *
 * @property name Nom formel de la plante/insecte
 * @property funFact Fait amusant en deux phrases
 */
data class PlantIdentification(
    val name: String,
    val funFact: String
)