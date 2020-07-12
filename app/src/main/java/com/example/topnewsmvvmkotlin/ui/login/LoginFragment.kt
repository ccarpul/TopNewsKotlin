package com.example.topnewsmvvmkotlin.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.addCallback
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    enum class ActionFireBase{
        LOGIN,
        REGISTER
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer
            loginButton.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                editTextEmail.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                editTextPassword.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this, Observer {

            when (it) {

                is LoginViewModel.StateLiveData.PreLogin -> progressbarLayout.show()
                is LoginViewModel.StateLiveData.RefreshUi -> {
                    when (it.result) {
                        is Result.Success -> {
                            findNavController().apply {
                                popBackStack()
                                navigate(R.id.homeFragment)
                            }
                        }
                        is Result.GenericError -> {
                            firebaseErrors(it.result.error.toString())
                        }
                    }
                }
                is LoginViewModel.StateLiveData.PostLogin -> progressbarLayout.hide()

            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextEmail.afterTextChanged {
            loginViewModel.loginDataChanged(
                editTextEmail.text.toString(),
                editTextPassword.text.toString()
            )
        }

        editTextPassword.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    editTextPassword.text.toString(),
                    editTextPassword.text.toString()
                )


            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.loginFireBase(
                            editTextEmail.text.toString(),
                            editTextPassword.text.toString(),
                            ActionFireBase.LOGIN
                        )
                }
                false
            }
        }

        loginButton.setOnClickListener { loginViewModel.loginFireBase(editTextEmail.text.toString(),
                editTextPassword.text.toString(), ActionFireBase.LOGIN)
        }


        signUpButton.setOnClickListener { loginViewModel.loginFireBase(editTextEmail.text.toString(),
                editTextPassword.text.toString(), ActionFireBase.REGISTER)
        }
    }


    fun firebaseErrors(error: String) {
        Log.i("Carpul", "error $error")
        Log.i("Carpul", "R.string.formatError ${resources.getString(R.string.formatError)}")
        when (error) {
            resources.getString(R.string.networkError) -> makeToast(
                context,
                "Please check your conection"
            )
            resources.getString(R.string.formatError) -> makeToast(
                context,
                "Please check your Email format"
            )
            resources.getString(R.string.emailInUse) -> makeToast(context,"Email in Use")
            else -> makeToast(context, resources.getString(R.string.loginFailed))
        }
    }

}