package com.yanncer.fixconvnum.presentation.home

import com.yanncer.fixconvnum.domain.models.Contact

data class HomeState(var query: String = "",
                     var isLoading: Boolean = false,
                     var contacts: List<Contact> = emptyList(),
                     var error: String = "")
