package com.example.topnewsmvvmkotlin.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.example.topnewsmvvmkotlin.utils.Constants
import com.example.topnewsmvvmkotlin.utils.ResultWrapper
import kotlinx.coroutines.*

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel()/*, CoroutineScope*/ {

    //lateinit var job: Job
    var page = Constants.PAGE_INIT
    var queryFilters = listOf<String>()
    private val restoreData = arrayListOf<ModelResponse>()
    var pos = 0
    private var totalResult: Int? = Constants.TOTAL_RESULTS_INIT

    //override val coroutineContext: CoroutineContext
      //  get() = Dispatchers.Main + job
    
    sealed class StateLiveData{
        object InitialStateUi: StateLiveData()
        object PreCall:        StateLiveData()
        class  RefreshStateUi(val response: ModelResponse) : StateLiveData()
        object PostCall:        StateLiveData()
        class AdapterRecycler(val dataRecyclerView: ArrayList<ModelResponse>): StateLiveData()
    }

    private val _modelArticles = MutableLiveData<StateLiveData>()
    val modelArticles: LiveData<StateLiveData>
        get() {
            if (_modelArticles.value == null) _modelArticles.value = StateLiveData.InitialStateUi
            else  _modelArticles.value = StateLiveData.AdapterRecycler(restoreData)
            return _modelArticles
        }

    fun getDataArticles() {

        //viewModelScope, se cancela automaticamente al terminarse el ViewModel
        viewModelScope.launch {
            //launch { //Anteriormente usado con la extencion de la clase de la interfaz ViewModelScope
            _modelArticles.value = StateLiveData.PreCall
            when (val result = homeRepository.getArticles(page, queryFilters)) {
                is ResultWrapper.Success -> {

                    totalResult = result.value.totalResults
                    _modelArticles.value = StateLiveData.RefreshStateUi(result.value)
                    restoreData.add(result.value)
                }
                is ResultWrapper.NetworkError -> { Log.d("Test", result.throwable.message()) }
                is ResultWrapper.GenericError -> { Log.d("Test", result.error) }
            }
            _modelArticles.value = StateLiveData.PostCall
        }

    }

    fun setArticle(){
        Log.i("Carpul", "setArticle: ")
        //TODO
    }

    //init { job = SupervisorJob() }

    override fun onCleared() {
        super.onCleared()
    }

    fun getTotalResults(): Int{
        return totalResult ?: 0
    }
}