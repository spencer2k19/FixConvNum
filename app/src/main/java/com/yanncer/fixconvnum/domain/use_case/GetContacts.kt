package com.yanncer.fixconvnum.domain.use_case

import android.util.Log
import com.yanncer.fixconvnum.common.Resource
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetContacts @Inject constructor(
    private val repository: ContactsRepository
) {
     operator fun invoke(limit: Int, offset: Int,searchQuery: String): Flow<Resource<List<Contact>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val contacts = repository.fetchContacts(limit, offset,searchQuery)
                emit(Resource.Success(contacts))
            }catch (e: Exception) {
                emit(Resource.Error(e.message ?: ""))
            }
        }
    }
}