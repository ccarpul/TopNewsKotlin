package com.example.topnewsmvvmkotlin.ui.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.util.Constants
import com.example.topnewsmvvmkotlin.util.ResultWrapper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel(), CoroutineScope {

    private var job: Job = Job()
    var page = Constants.PAGE_INIT
    var queryFilters = listOf<String>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job //Sobreesribiendo la variable coroutineContext para usar la
    // interfaz CourutinaScope como ambito de las coroutinas del ViewModel
    // Especifico el dispacher para que se ejecute en el hilo principal y
    //le paso la referencia job para controlar el estado de la corrutina

    val uiDataArticles = MutableLiveData<ModelResponse>()

    val getDataArticles: LiveData<ModelResponse>
        get() {
            if (uiDataArticles.value == null) {
                getDataArticles(page)
            } else getDataArticles(page)
            return uiDataArticles
        }

    fun getDataArticles(page: Int) {

        launch {

            when (val result = homeRepository.getArticles(page, queryFilters)) {
                is ResultWrapper.Success -> {
                    uiDataArticles.value = result.value
                }
                is ResultWrapper.NetworkError -> {
                    Log.d("Test", result.throwable.message())
                }
                is ResultWrapper.GenericError -> {
                    Log.d("Test", result.error)
                }
            }
        }
    }

    init { job = SupervisorJob() }

    override fun onCleared() {  //Metodo de la clase View Model, al cerrarse el View Model
        // se cierran todas las corrutinas pertenecientes ael presente Scope
        job.cancel()
        super.onCleared()
    }
}