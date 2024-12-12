package com.yanncer.fixconvnum.domain.repository

import androidx.paging.PagingData
import com.yanncer.fixconvnum.domain.models.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    fun getContactsPager(): Flow<PagingData<Contact>>
    suspend fun fetchContacts(limit: Int = Int.MAX_VALUE, offset: Int = 0,searchQuery: String): List<Contact>
    suspend fun updateContact(contact: Contact)
    suspend fun removeContact(contactId: Long, contacts: List<Contact>): List<Contact>
    suspend fun removeContacts(contactIds: List<Long>, contacts: List<Contact>): List<Contact>
}