package com.yanncer.fixconvnum.domain.use_case

data class ContactUseCases(
    val getContacts: GetContacts,
    val fixOneContact: FixOneContact,
    val removeContacts: RemoveContact,
    val updateContacts: UpdateContacts
)
