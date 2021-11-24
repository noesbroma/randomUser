package com.example.randomuser.ui.brouchers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.randomuser.data.brochures.BrouchersRepository
import com.example.randomuser.data.brochures.GetBrouchersResult
import com.example.randomuser.domain.Broucher


class BrouchersViewModel(private val repository: BrouchersRepository): ViewModel() {
    val onLoadBrouchersEvent = MutableLiveData<ArrayList<Broucher>>()
    val onLoadMoreEvent = MutableLiveData<ArrayList<Broucher>>()
    var catalogsList = ArrayList<Broucher>()


    fun getBrouchersFromAPI() {
        viewModelScope.launch {
            when (val result = repository.getBrouchers("25", "20", "0", "41.38864", "2.161126")) {
                is GetBrouchersResult.Ok -> {
                    onLoadBrouchersEvent.value = result.getBrouchersResponse.brochuresList
                    catalogsList = result.getBrouchersResponse.brochuresList
                }

                is GetBrouchersResult.Error -> {
                }
            }
        }
    }


   fun loadMore(offset: Int) {
        viewModelScope.launch {
            when (val result = repository.getBrouchers("25", "20", offset.toString(), "41.38864", "2.161126")) {
                is GetBrouchersResult.Ok -> {
                    catalogsList.addAll(result.getBrouchersResponse.brochuresList)
                    onLoadMoreEvent.value = catalogsList
                }

                is GetBrouchersResult.Error -> {
                }
            }
        }
    }
}