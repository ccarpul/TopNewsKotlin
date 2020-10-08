package com.example.topnewsmvvmkotlin.ui.login

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class LoginRepository(private val instance: FirebaseAuth) {

    fun setLoginWithEmail(username: String, password: String ): Task<AuthResult> =
        instance.signInWithEmailAndPassword(username, password)

    fun setRegisterWithEmail(username: String, password: String ): Task<AuthResult> =
        instance.createUserWithEmailAndPassword(username, password)

    fun setLoginByGoogle(credential: AuthCredential): Task<AuthResult> =
        instance.signInWithCredential(credential)

    fun setLoginByTwitter(activity: FragmentActivity?): Task<AuthResult> {

        val pendingAuthResult = instance.pendingAuthResult
        Log.i("Carpul", "setLoginByTwitter: $pendingAuthResult")
        return if(pendingAuthResult != null){

            pendingAuthResult

        }else{
            val oAuthProvider = OAuthProvider.newBuilder("twitter.com")
            instance.startActivityForSignInWithProvider(activity!!, oAuthProvider.build())

        }
    }
}