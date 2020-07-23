package com.example.topnewsmvvmkotlin.ui

import android.app.Application
import com.example.topnewsmvvmkotlin.data.di.DependenciesModuleHome
import com.example.topnewsmvvmkotlin.data.di.DependenciesModuleLogin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

//Clase para usar iniciar Koin para la inyección de dependencias

class TopNewsApp: Application() {
    companion object{
        lateinit var mApp: TopNewsApp
        private set
    }
    override fun onCreate(){
        super.onCreate()
        mApp = this
        startKoin {
            androidContext(this@TopNewsApp)
            modules(listOf(DependenciesModuleHome, DependenciesModuleLogin))
        }
    }

}