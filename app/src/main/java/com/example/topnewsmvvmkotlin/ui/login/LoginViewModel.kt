package com.example.topnewsmvvmkotlin.ui.login

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.utils.AuthResult
import com.example.topnewsmvvmkotlin.utils.LoginFormState
import com.example.topnewsmvvmkotlin.utils.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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
        username: String = "", password: String = "", action: LoginFragment.ActionFireBase,
        credential: AuthCredential? = null, activity: FragmentActivity? = null
    ) {

        Log.i("Carpul", "loginFireBase: ")
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
                            loginRepository.setLoginByTwitter(activity!!).addOnCompleteListener {
                                _loginResult.value = it.AuthResult()
                                _loginResult.value = StateLiveData.PostLogin
                            }.addOnFailureListener {
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

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@') && username.isNotEmpty()) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    init {
        job = SupervisorJob()
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}