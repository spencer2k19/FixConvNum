package com.yanncer.fixconvnum.domain.use_case

data class ContactUseCases(
    val getContacts: GetContacts,
    val fixContacts: FixContacts,
    val removeContact: RemoveContact,
    val removeContacts: RemoveContacts,
    val updateContacts: UpdateContacts,
    val getPagingContacts: GetPagingContacts
)
