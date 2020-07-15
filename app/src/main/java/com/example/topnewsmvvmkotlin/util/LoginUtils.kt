package com.example.topnewsmvvmkotlin.util


import android.content.Context
import android.util.Log
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.getCredentialFacebook
import com.example.topnewsmvvmkotlin.ui.login.LoginViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

fun firebaseErrors(error: String?, context: Context) {

    when (error) {
        context.getString(R.string.networkError) -> makeToast(context, "Please check your conection")
        context.getString(R.string.formatError) -> makeToast(context, "Please check your Email format")
        context.getString(R.string.emailInUse) -> makeToast(context, "Email in use try to login with Google account")
        context.getString(R.string.apiException) -> makeToast(context, "Choice an option")
        context.getString(R.string.cancelByUser) -> makeToast(context, "cancel by user")
        else -> makeToast(context, context.getString(R.string.loginFailed))
    }
}

fun Task<AuthResult>.AuthResult() =
    if (this.isSuccessful) {
        LoginViewModel.StateLiveData.RefreshUi(Result.Success("Success")) }
    else {
        if (this.exception != null) LoginViewModel.StateLiveData.RefreshUi(Result.GenericError(this.exception.toString()))
        else  LoginViewModel.StateLiveData.RefreshUi(Result.GenericError("Error"))
    }





