package com.bloom.app.data.remote

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Service d'authentification Firebase
 *
 * Gère toutes les opérations d'authentification :
 * - Connexion Email/Password
 * - Connexion Google
 * - Inscription
 * - Déconnexion
 * - État de l'utilisateur
 */
class FirebaseAuthService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Utilisateur actuellement connecté
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Flow de l'état d'authentification
     * Émet l'utilisateur connecté ou null
     */
    val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)

        // Émission initiale
        trySend(auth.currentUser)

        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    /**
     * Connexion avec Email et Password
     *
     * @param email Adresse email
     * @param password Mot de passe
     * @return Result avec l'utilisateur ou une erreur
     */
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Authentication failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Inscription avec Email et Password
     *
     * @param email Adresse email
     * @param password Mot de passe
     * @return Result avec l'utilisateur ou une erreur
     */
    suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Connexion avec Google
     *
     * @param account Compte Google obtenu après le sign-in
     * @return Result avec l'utilisateur ou une erreur
     */
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user

            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Google sign-in failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Déconnexion
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Réinitialisation du mot de passe
     *
     * @param email Adresse email
     * @return Result.success si l'email est envoyé, Result.failure si erreur
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Suppression du compte utilisateur
     *
     * @return Result.success si supprimé, Result.failure si erreur
     */
    suspend fun deleteAccount(): Result<Unit> {
        return try {
            currentUser?.delete()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Vérifie si un utilisateur est connecté
     */
    fun isUserSignedIn(): Boolean {
        return currentUser != null
    }

    /**
     * Obtient l'ID de l'utilisateur connecté
     */
    fun getCurrentUserId(): String? {
        return currentUser?.uid
    }

    /**
     * Obtient l'email de l'utilisateur connecté
     */
    fun getCurrentUserEmail(): String? {
        return currentUser?.email
    }
}