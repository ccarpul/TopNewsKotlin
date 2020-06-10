package com.example.topnewsmvvmkotlin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job //Sobreesribiendo la variable coroutineContext para usar la
    // interfaz CourutinaScope como ambito de las coroutinas del ViewModel
    // Especifico el dispacher para que se ejecute en el hilo principal y
    //le paso la referencia job para controlar el estado de la corrutina

    private val uiDataArticles = MutableLiveData<ModelResponse>()

    val getDataArticles: LiveData<ModelResponse>
        get() {
            if (uiDataArticles.value == null) getDataArticles()
            return uiDataArticles
        }
    fun getDataArticles() {
        launch {
            uiDataArticles.value = homeRepository.getArticles()
        }
    }
    init {
        job = SupervisorJob()
    }
    override fun onCleared() {  //Metodo de la clase View Model, al cerrarse el View Model
        // se cierran todas las corrutinas pertenecientes ael presente Scope
        job.cancel()
        super.onCleared()
    }
}