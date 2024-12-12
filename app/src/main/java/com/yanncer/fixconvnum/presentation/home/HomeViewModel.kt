package com.yanncer.fixconvnum.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.yanncer.fixconvnum.common.BeninPhoneValidator.hasPhoneNumberIssue
import com.yanncer.fixconvnum.common.PrefSingleton
import com.yanncer.fixconvnum.common.Resource
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.use_case.ContactUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: ContactUseCases
): ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state




    private val _showRemoveDialogState = mutableStateOf(false)
    val showRemoveDialogState: State<Boolean> = _showRemoveDialogState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onQueryChange(value: String) {
        _state.value = state.value.copy(query = value)
        viewModelScope.launch {
            delay(1000)
            fetchContacts()
        }

    }

    fun issueExistsInList(): Boolean {
        return _state.value.contacts.any {
            it.hasPhoneNumberIssue()
        }
    }

    fun showRemoveDialog(contact: Contact) {
        Log.e("contact","Contact with dialog: $contact")
        _state.value = state.value.copy(contact = contact)
        _showRemoveDialogState.value = true
    }

    fun dismissRemoveDialog() {
        _showRemoveDialogState.value = false
    }

     fun getDisplayContentOfContact(contact: Contact): String {
        return  if (contact.firstName.isNotEmpty() && contact.lastName.isNotEmpty()) {
            "${contact.firstName} ${contact.lastName}"
        } else contact.displayName
    }


    fun fixSomeContacts() {
        if (state.value.contactsSelected.isNotEmpty()) {

            useCases.fixContacts(state.value.contactsSelected).onEach {result ->
                when(result) {
                    is Resource.Loading -> {
                        _state.value =  state.value.copy(isLoading = true, selectionMode = false)
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(message = result.message?:"Une erreur s'est produite. Veuillez réessayer ultérieurement"))
                        _state.value = state.value.copy(isLoading = false, selectionMode = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoading = false, contacts = result.data ?: emptyList(), selectionMode = false, contactsSelected = emptyList())
                        _eventFlow.emit(UIEvent.ShowSnackbar(message ="Les contacts sélectionnés  ont été corrigé avec succès ✅"))
                    }
                    else -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    fun removeContacts() {
        if (state.value.contactsSelected.isNotEmpty()) {

                useCases.removeContacts(state.value.contactsSelected.map { it.id },state.value.contacts).onEach {result ->
                    when(result) {

                        is Resource.Loading -> {
                            _state.value =  state.value.copy(isLoading = true, selectionMode = false)
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(isLoading = false, selectionMode = true)
                            _eventFlow.emit(UIEvent.ShowSnackbar(message = result.message?:"Une erreur s'est produite. Veuillez réessayer ultérieurement"))
                        }

                        is Resource.Success -> {
                            _state.value = state.value.copy(isLoading = false, contacts = result.data ?: emptyList(), selectionMode = false, contactsSelected = emptyList())
                            _eventFlow.emit(UIEvent.ShowSnackbar(message ="Les contacts sélectionnés  ont été retiré avec succès ✅"))
                        }

                        else -> {

                        }
                    }

                }.launchIn(viewModelScope)


        }

    }




    fun fixOneContact(contact: Contact) {
        useCases.fixContacts(listOf(contact)).onEach {result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value =  state.value.copy(isLoading = true)
                }
                is Resource.Error -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(message = result.message?:"Une erreur s'est produite. Veuillez réessayer ultérieurement"))
                    _state.value = state.value.copy(isLoading = false)
                }
                is Resource.Success -> {
                    _state.value = state.value.copy(isLoading = false, contacts = result.data ?: emptyList())
                    contact.let {

                    }

                    _eventFlow.emit(UIEvent.ShowSnackbar(message ="Le contact ${getDisplayContentOfContact(contact)} a été corrigé avec succès ✅"))
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun toggleSelectMode() {
        _state.value = state.value.copy(
            selectionMode = !state.value.selectionMode
        )
    }

    fun isContactSelected(contactId: Long): Boolean {
        return state.value.contactsSelected.any { it.id == contactId }
    }

    fun toggleSelectionOfContact(contact: Contact, isChecked: Boolean) {
        val updatedContacts = state.value.contactsSelected.toMutableList()
        if (isChecked) {
            updatedContacts.add(contact)
        } else {
            updatedContacts.remove(contact)
        }
        _state.value = state.value.copy(contactsSelected = updatedContacts)
    }





    fun removeContact() {
        state.value.contact?.let {
            Log.e("contact","Contact to be removed: $it")

            useCases.removeContact(it,state.value.contacts).onEach {result ->
                when(result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(contact = null)
                        _eventFlow.emit(UIEvent.ShowSnackbar(message = result.message?:"Une erreur s'est produite. Veuillez réessayer ultérieurement"))
                    }

                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoading = false, contacts = result.data ?: emptyList(), contact = null)

                        _eventFlow.emit(UIEvent.ShowSnackbar(message ="Le contact ${getDisplayContentOfContact(it)} été retiré ✅"))
                    }

                    else -> {

                    }
                }

            }.launchIn(viewModelScope)
        }

    }



    fun updateContacts() {
        useCases.updateContacts().onEach {result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value =  state.value.copy(isLoading = true)
                }
                is Resource.Error -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(message = result.message?:"Une erreur s'est produite. Veuillez réessayer ultérieurement"))
                    _state.value = state.value.copy(isLoading = false)
                }
                is Resource.Success -> {
                    _state.value = state.value.copy(isLoading = false, contacts = result.data ?: emptyList())
                    _eventFlow.emit(UIEvent.ShowSnackbar(message ="Vos contacts ont été corrigé avec succès"))
                }
                else -> {}
            }

        }.launchIn(viewModelScope)
    }




     fun fetchContacts(searchQuery: String = "") {
        useCases.getContacts(limit = 50, offset = 0,state.value.query).onEach {result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = _state.value.copy(contacts = result.data ?: emptyList(), isLoading = false)
                    if (state.value.query.isEmpty()) {
                        fetchRemainingContacts(searchQuery)
                    }

                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(error = result.message ?: "", isLoading = false)
                }
                else -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchRemainingContacts(searchQuery: String) {
        useCases.getContacts(limit = Int.MAX_VALUE, offset = 50,"").onEach {result ->
            when(result) {

                is Resource.Success -> {
                    val currentContacts = _state.value.contacts.toMutableList()
                    result.data?.let { newContacts ->
                        currentContacts.addAll(newContacts)
                    }
                    _state.value = _state.value.copy(
                        contacts = currentContacts
                    )
                }

                is Resource.Error -> {
                    _state.value = HomeState(error = result.message ?: "")
                }
                else -> {

                }
            }
        }.launchIn(viewModelScope)
    }


    sealed class UIEvent {
        data class ShowSnackbar(val message: String): UIEvent()

    }


}