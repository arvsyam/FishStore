package com.adl.fishstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adl.fishstore.repo.FishRepo
import com.adl.fishstore.repo.FishResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FishViewModel(val fishRepo: FishRepo): ViewModel() {
    val fishStateFlow = MutableStateFlow<FishResponse?>(null)

    init{
        viewModelScope.launch {
            fishRepo.getFishDetails().collect(){
                fishStateFlow.value = it
            }
        }
    }

    fun getBookInfo() = fishRepo.getFishDetails()
}