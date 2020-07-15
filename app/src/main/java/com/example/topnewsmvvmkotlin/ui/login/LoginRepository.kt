package com.example.topnewsmvvmkotlin.ui.login

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.util.Log
import androidx.fragment.app.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginRepository(private val instance: FirebaseAuth) {

    fun setLoginWithEmail(username: String, password: String ): Task<AuthResult> =
        instance.signInWithEmailAndPassword(username, password)

    fun setRegisterWithEmail(username: String, password: String ): Task<AuthResult> =
        instance.createUserWithEmailAndPassword(username, password)

    fun setLoginByGoogle(credential: AuthCredential): Task<AuthResult> =
        instance.signInWithCredential(credential)

    fun setLoginByTwitter(activity: FragmentActivity?): Task<AuthResult> {

        val pendingAuthResult = instance.pendingAuthResult
        return if(pendingAuthResult != null){
            pendingAuthResult
        }else{
            val oAuthProvider = OAuthProvider.newBuilder("twitter.com")
            instance.startActivityForSignInWithProvider(activity!!, oAuthProvider.build())
        }
    }

    fun setLoginByFacebook(): LoginManager {
        return LoginManager.getInstance()
    }
}