package com.madteam.sunset.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : DatabaseContract {

    // TODO: Tu mindk es 24 y esta funcion solo permite crear usuarios a partir de la 26
//    @RequiresApi(VERSION_CODES.O)
    override fun createUser(
        email: String,
        username: String,
        provider: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val userDocument = firebaseFirestore.collection("users").document(username)
        val documentSnapshot = userDocument.get().await()
        if (documentSnapshot.exists()) {
            emit(Resource.Error("El usuario ya existe en la base de datos"))
            return@flow
        }

        val currentDate = Calendar.getInstance().time.toString()
        val user = hashMapOf(
            "username" to username,
            "email" to email,
            "provider" to provider,
            "creation_date" to currentDate
        )

        // FIXME: No deberias tener strings hardcoded ("users") en mitad del codigo, es mala practica.
        // Si el dia de mñn quieres cambiar el nombre de la coleccion, vas a tener que cambiar este string en mil sitios.
        firebaseFirestore.collection("users").document(username).set(user).await()
        emit(Resource.Success(Unit))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }

    // Tambien queria dejar un ejemplo de como retornar información a través de una lambda, ya que muchas veces
    // no se puede hacer un return directamente porque a respuesta te la va a entregar firestore despues de realizar
    // la petición, por eso esta funcion, retorna el UserProfile en una lambda cuando se recibe en el `addOnSuccessListener`
    override fun getProfileByUsername(email: String, userProfileCallback: (UserProfile) -> Unit) {
        // FIXME: No necesitas recorrer las colecciones, el documento que buscas es el del username!
        firebaseFirestore.collection("users").document(email)
            .get()
            .addOnSuccessListener { userDocument ->
                userDocument.toObject(UserProfile::class.java)?.let {
                    userProfileCallback(it)
                }
            }
    }
}

interface DatabaseContract {
    fun createUser(email: String, username: String, provider: String): Flow<Resource<Unit>>
    fun getProfileByUsername(email: String, userProfileCallback: (UserProfile) -> Unit)
}
