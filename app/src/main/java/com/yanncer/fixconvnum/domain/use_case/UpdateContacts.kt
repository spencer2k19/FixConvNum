package com.yanncer.fixconvnum.domain.use_case

import com.yanncer.fixconvnum.domain.repository.ContactsRepository
import javax.inject.Inject

class UpdateContacts @Inject constructor(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke() {

    }
}