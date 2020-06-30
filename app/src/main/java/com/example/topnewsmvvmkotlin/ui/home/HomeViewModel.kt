package com.example.topnewsmvvmkotlin.ui.home


import android.content.ContentValues.TAG
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
        get() = Dispatchers.Main + job
    /**Sobreesribiendo la variable coroutineContext para usar la
    // interfaz CourutinaScope como ambito de las coroutinas del ViewModel
    // Especifico el dispacher para que se ejecute en el hilo principal y
    //le paso la referencia job para controlar el estado de la corrutina*/

    private val uiModelArticles = MutableLiveData<StateLiveData>()

    sealed class StateLiveData{
        object InitialStateUi: StateLiveData()
        object PreCall:        StateLiveData()
        class  RefreshStateUi(val response: ModelResponse) : StateLiveData()
        object PostCall:       StateLiveData()
    }

    val modelArticles: LiveData<StateLiveData>
        get() {
            if (uiModelArticles.value == null) setUi()
            return uiModelArticles
        }

    fun getDataArticles() {

        uiModelArticles.value = StateLiveData.PreCall

        launch {
            when (val result = homeRepository.getArticles(page, queryFilters)) {
                is ResultWrapper.Success      -> {
                    uiModelArticles.value = StateLiveData.RefreshStateUi(result.value)
                }
                is ResultWrapper.NetworkError -> { Log.d("Test", result.throwable.message()) }
                is ResultWrapper.GenericError -> { Log.d("Test", result.error) }
            }
        }
        uiModelArticles.value = StateLiveData.PostCall
    }

    init { job = SupervisorJob() }

    override fun onCleared() {  //Metodo de la clase View Model, al cerrarse el View Model
        // se cierran todas las corrutinas pertenecientes ael presente Scope
        job.cancel()
        super.onCleared()
    }

    fun setUi(){ uiModelArticles.value = StateLiveData.InitialStateUi }
}