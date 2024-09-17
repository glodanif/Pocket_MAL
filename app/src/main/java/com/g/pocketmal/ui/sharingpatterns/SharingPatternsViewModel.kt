package com.g.pocketmal.ui.sharingpatterns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharingPatternsViewModel @Inject constructor(): ViewModel() {

    init {
        loadPatterns()
    }

    private fun loadPatterns() {
        viewModelScope.launch {

        }
    }
}
