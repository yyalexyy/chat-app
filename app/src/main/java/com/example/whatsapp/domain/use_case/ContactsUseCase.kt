package com.example.whatsapp.domain.use_case

import com.example.whatsapp.domain.repository.ContactsRepository
import javax.inject.Inject

class ContactsUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) {

    fun getAllUsers(deviceContacts: List<String>) = contactsRepository.getAllUsers(deviceContacts)
}