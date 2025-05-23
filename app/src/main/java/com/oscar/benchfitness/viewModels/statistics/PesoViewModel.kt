package com.oscar.benchfitness.viewModels.statistics


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.statistics.WeightProgress
import com.oscar.benchfitness.repository.StatisticsExercisesRepository
import com.oscar.benchfitness.repository.StatisticsWeightRepository
import kotlinx.coroutines.launch

class PesoViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var progreso by mutableStateOf<WeightProgress?>(WeightProgress())



    private val statisticsWeightRepository = StatisticsWeightRepository(auth, db)


    fun cargarHistorialPesos(){
        viewModelScope.launch {
            progreso = statisticsWeightRepository.getAllWeightProgress()
        }
    }






}