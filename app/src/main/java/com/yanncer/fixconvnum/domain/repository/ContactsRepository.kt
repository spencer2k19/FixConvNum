package com.yanncer.fixconvnum.domain.repository

import com.yanncer.fixconvnum.domain.models.Contact

interface ContactsRepository {
    suspend fun fetchContacts(): List<Contact>
    suspend fun updateContact(contact: Contact)
}