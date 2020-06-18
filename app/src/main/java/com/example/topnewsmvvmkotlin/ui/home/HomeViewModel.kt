package com.example.topnewsmvvmkotlin.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.util.Constants
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.log

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel(), CoroutineScope {

    private var job: Job = Job()
    var page = 1
    var queryFilters = arrayOf<String>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job //Sobreesribiendo la variable coroutineContext para usar la
    // interfaz CourutinaScope como ambito de las coroutinas del ViewModel
    // Especifico el dispacher para que se ejecute en el hilo principal y
    //le paso la referencia job para controlar el estado de la corrutina

    private val uiDataArticles = MutableLiveData<ModelResponse>()

    val getDataArticles: LiveData<ModelResponse>
        get() {
             if (uiDataArticles.value == null) getDataArticles(page)
            else getDataArticles(page)
            page++
            return uiDataArticles
        }

    fun getDataArticles(page: Int) {
        launch {
            uiDataArticles.value = homeRepository.getArticles(page, queryFilters)
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