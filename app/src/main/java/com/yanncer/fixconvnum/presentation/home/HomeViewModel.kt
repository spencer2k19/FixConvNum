package com.yanncer.fixconvnum.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncer.fixconvnum.common.BeninPhoneValidator.hasPhoneNumberIssue
import com.yanncer.fixconvnum.common.Resource
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.use_case.ContactUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    fun removeContact() {
        state.value.contact?.let {
            Log.e("contact","Contact to be removed: $it")
            useCases.removeContacts(it,state.value.contacts).onEach {result ->
                when(result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(contact = null)
                        _eventFlow.emit(UIEvent.ShowSnackbar(message = result.message?:"Une erreur s'est produite. Veuillez réessayer ultérieurement"))
                    }

                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoading = false, contacts = result.data ?: emptyList(), contact = null)
                        val displayName = if (it.firstName.isNotEmpty() && it.lastName.isNotEmpty()) {
                            "${it.firstName} ${it.lastName}"
                        } else it.displayName
                        _eventFlow.emit(UIEvent.ShowSnackbar(message ="Le contact $displayName été retiré ✅"))
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
            }

        }.launchIn(viewModelScope)
    }

     fun fetchContacts() {
        useCases.getContacts().onEach {result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = HomeState(contacts = result.data ?: emptyList())
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