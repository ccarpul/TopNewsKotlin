package com.example.topnewsmvvmkotlin.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.util.Result
import com.example.topnewsmvvmkotlin.util.LoginFormState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel() : ViewModel(), CoroutineScope {

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

    fun loginFireBase(username: String, password: String, action: LoginFragment.ActionFireBase) {

        launch {
            _loginResult.value = StateLiveData.PreLogin

            try {

                if (action == LoginFragment.ActionFireBase.LOGIN) {
                    Log.i("Carpul", "loginFireBase: SignUp")
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener {

                            if (it.isSuccessful) {
                                _loginResult.value =
                                    StateLiveData.RefreshUi(Result.Success(username))
                            } else if (it.exception != null) {
                                _loginResult.value = StateLiveData
                                    .RefreshUi(Result.GenericError(it.exception.toString()))
                            }
                            _loginResult.value = StateLiveData.PostLogin

                        }
                } else {
                    Log.i("Carpul", "loginFireBase: login")
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener {

                            if (it.isSuccessful) {
                                _loginResult.value =
                                    StateLiveData.RefreshUi(Result.Success(username))
                            } else if (it.exception != null) {
                                _loginResult.value = StateLiveData
                                    .RefreshUi(Result.GenericError(it.exception.toString()))
                            }
                            _loginResult.value = StateLiveData.PostLogin

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
        return if (username.contains('@')) {
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