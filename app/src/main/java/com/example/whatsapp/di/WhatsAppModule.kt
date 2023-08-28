package com.example.whatsapp.di

import com.example.whatsapp.data.repository.AuthRepositoryImpl
import com.example.whatsapp.data.repository.ContactsRepositoryImpl
import com.example.whatsapp.domain.repository.AuthRepository
import com.example.whatsapp.domain.repository.ContactsRepository
import com.example.whatsapp.domain.use_case.AuthenticationUseCase
import com.example.whatsapp.domain.use_case.ContactsUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WhatsAppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) : AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideAuthenticationUseCase(authRepository: AuthRepository) : AuthenticationUseCase {
        return AuthenticationUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideContactRepository(firebaseFirestore: FirebaseFirestore): ContactsRepository {
        return ContactsRepositoryImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideContactsUseCase(contactsRepository: ContactsRepository) : ContactsUseCase {
        return ContactsUseCase(contactsRepository)
    }
}