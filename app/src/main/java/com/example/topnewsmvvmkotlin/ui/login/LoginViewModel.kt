package com.example.topnewsmvvmkotlin.ui.login

import android.app.Activity
import android.util.Log
import android.util.Patterns
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.getCredentialFacebook
import com.example.topnewsmvvmkotlin.ui.MainActivity
import com.example.topnewsmvvmkotlin.util.*
import com.facebook.CallbackManager
import com.facebook.FacebookButtonBase
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class LoginViewModel(val loginRepository: LoginRepository) : ViewModel(), CoroutineScope {

    private lateinit var resultTwitter: Task<AuthResult>


    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    sealed class StateLiveData {
        object PreLogin : StateLiveData()
        class RefreshUi(val result: Result<Any>) : StateLiveData()
        object PostLogin : StateLiveData()
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm


    private val _loginResult = MutableLiveData<StateLiveData>()
    val loginResult: LiveData<StateLiveData>
        get() {
            return _loginResult
        }

    fun loginFireBase(
        username: String, password: String, action: LoginFragment.ActionFireBase,
        credential: AuthCredential?, activity: FragmentActivity?
    ) {

        launch {
            _loginResult.value = StateLiveData.PreLogin

            try {
                when (action) {

                    LoginFragment.ActionFireBase.LOGIN -> {
                        loginRepository.setLoginWithEmail(username, password)
                            .addOnCompleteListener {
                                _loginResult.value = it.AuthResult()
                                _loginResult.value = StateLiveData.PostLogin
                            }
                    }
                    LoginFragment.ActionFireBase.REGISTER -> {
                        loginRepository.setRegisterWithEmail(username, password)
                            .addOnCompleteListener {
                                _loginResult.value = it.AuthResult()
                                _loginResult.value = StateLiveData.PostLogin

                            }
                    }
                    LoginFragment.ActionFireBase.GOOGLE -> {

                        loginRepository.setLoginByGoogle(credential!!).addOnCompleteListener {
                            _loginResult.value = it.AuthResult()
                            _loginResult.value = StateLiveData.PostLogin
                        }.addOnFailureListener { Log.i("Carpul", "${it.localizedMessage}") }

                    }
                    LoginFragment.ActionFireBase.TWITTER -> {

                        resultTwitter =
                            loginRepository.setLoginByTwitter(activity).addOnCompleteListener {
                                _loginResult.value = resultTwitter.AuthResult()
                                _loginResult.value = StateLiveData.PostLogin
                            }.addOnFailureListener {
                                Log.i("Carpul", it.localizedMessage)
                                StateLiveData.RefreshUi(Result.GenericError(it.localizedMessage))
                            }
                    }

                    LoginFragment.ActionFireBase.FACEBOOK -> {
                        FirebaseAuth.getInstance().signInWithCredential(credential!!)
                            .addOnCompleteListener {
                                _loginResult.value = it.AuthResult()
                                _loginResult.value = StateLiveData.PostLogin
                            }.addOnFailureListener { Log.i("Carpul", "${it.localizedMessage}") }
                    }
                }
            } catch (e: Exception) {
                _loginResult.value =
                    StateLiveData.RefreshUi(Result.GenericError(e.localizedMessage))
                _loginResult.value = StateLiveData.PostLogin
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalidUsername)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalidPassword)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@') && username.isNotEmpty()) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
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