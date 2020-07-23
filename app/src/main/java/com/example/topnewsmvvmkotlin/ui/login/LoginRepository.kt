package com.example.topnewsmvvmkotlin.ui.login

import androidx.fragment.app.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

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
}