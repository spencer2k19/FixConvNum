package com.yanncer.fixconvnum.domain.repository

import com.yanncer.fixconvnum.domain.models.Contact

interface ContactsRepository {
    suspend fun fetchContacts(): List<Contact>
    suspend fun updateContact(contact: Contact)
    suspend fun removeContact(contactId: Long, contacts: List<Contact>): List<Contact>
}