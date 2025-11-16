package com.rafagonp.weathertracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafagonp.weathertracker.domain.model.DaysListBO
import com.rafagonp.weathertracker.domain.model.Resource
import com.rafagonp.weathertracker.domain.usecase.GetPredictionPerDaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DaysListViewModel @Inject constructor(
    getPredictionPerDaysUseCase: GetPredictionPerDaysUseCase,
) : ViewModel() {

    val daysListFlow: SharedFlow<Resource<DaysListBO>> = getPredictionPerDaysUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Resource.loading(null)
    )

}
