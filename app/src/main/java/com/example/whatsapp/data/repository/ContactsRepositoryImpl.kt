package com.example.whatsapp.data.repository

import com.example.whatsapp.domain.model.User
import com.example.whatsapp.domain.repository.ContactsRepository
import com.example.whatsapp.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ContactsRepository {

    var whatsAppContacts: MutableList<User> = arrayListOf()

    override fun getAllUsers(deviceContacts: List<String>): Flow<Resource<List<User>>> = channelFlow {
        try {
            trySend(Resource.Loading)
            for (contact in deviceContacts) {
                val query = firestore.collection("users").whereEqualTo("userNumber", contact)
                query.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            whatsAppContacts.add(getUserFromDocument(document))
                            trySend(Resource.Success(whatsAppContacts))
                        }
                    }
                }
            }
            awaitClose()
        } catch (exception: Exception) {
            trySend(Resource.Error(exception.localizedMessage?:"An Error Occurred in ContactsRepositoryImpl::getAllUsers()"))
        }
    }

    private fun getUserFromDocument(document: QueryDocumentSnapshot): User {
        return User(
            userNumber = document.getString("userNumber"),
            userName = document.getString("userName"),
            userImage = document.getString("userImage"),
            userStatus = document.getString("userStatus")
        )
    }
}