package com.yanncer.fixconvnum.domain.use_case

import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.repository.ContactsRepository
import javax.inject.Inject

class FixOneContact @Inject constructor(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(contact: Contact) {

    }
}