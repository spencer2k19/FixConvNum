package com.yanncer.fixconvnum.presentation.home

import com.yanncer.fixconvnum.domain.models.Contact

data class HomeState(var query: String = "",
                     var isLoading: Boolean = false,
                     var originalContacs: List<Contact> = emptyList(),
                     var contacts: List<Contact> = emptyList(),
                     var error: String = "",
                    var contact: Contact? = null,
                    var selectionMode: Boolean = false,
                    var contactsSelected : List<Contact> = emptyList(),
                    var onlyContactsIssues: Boolean = false,
                    var hasAlreadyGettingStarted: Boolean = true
    )
