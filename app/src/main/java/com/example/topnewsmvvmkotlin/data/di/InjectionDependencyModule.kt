package com.example.topnewsmvvmkotlin.data.di

import com.example.topnewsmvvmkotlin.data.getApiService
import com.example.topnewsmvvmkotlin.ui.home.HomeRepository
import com.example.topnewsmvvmkotlin.ui.home.HomeViewModel
import com.example.topnewsmvvmkotlin.ui.login.LoginRepository
import com.example.topnewsmvvmkotlin.ui.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.sin

//modulo para inyeccion de dependencias con Koin
//este módulo permitirá agregar la dependencia de HomeViewModel en el MainActivity
val DependenciesModuleHome = module {
    viewModel { HomeViewModel(get()) }
    single { HomeRepository(get()) }
    single { getApiService()}
}

val DependenciesModuleLogin = module {

    viewModel { LoginViewModel(get()) }
    single { LoginRepository(get()) }
    single{FirebaseAuth.getInstance()}
}

