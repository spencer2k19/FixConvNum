package com.yanncer.fixconvnum.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncer.fixconvnum.common.Resource
import com.yanncer.fixconvnum.domain.use_case.ContactUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: ContactUseCases
): ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    fun onQueryChange(value: String) {
        _state.value = state.value.copy(query = value)
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
            }
        }.launchIn(viewModelScope)
    }
}