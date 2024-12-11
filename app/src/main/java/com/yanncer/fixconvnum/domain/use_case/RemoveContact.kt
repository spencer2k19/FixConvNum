package com.yanncer.fixconvnum.domain.use_case

import com.yanncer.fixconvnum.common.PrefSingleton
import com.yanncer.fixconvnum.common.Resource
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveContact @Inject constructor(
    private val repository: ContactsRepository
) {

     operator fun invoke(contact: Contact,allContacts: List<Contact>):Flow<Resource<List<Contact>>> {
        return flow {
            try {
                PrefSingleton.saveBool(contact.id.toString(),true)
                val contacts = repository.removeContact(contact.id, contacts = allContacts)
                emit(Resource.Success(contacts))
            }catch (e: Exception) {
                emit(Resource.Error(e.message ?: ""))
            }
        }

    }
}