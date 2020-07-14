package com.example.topnewsmvvmkotlin.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.getApiService
import com.example.topnewsmvvmkotlin.data.getClientGoogle
import com.example.topnewsmvvmkotlin.data.getCredentialGoogle
import com.example.topnewsmvvmkotlin.ui.MainActivity
import com.example.topnewsmvvmkotlin.util.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.TwitterAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    enum class ActionFireBase {
        LOGIN,
        REGISTER,
        GOOGLE,
        TWITTER,
        FACEBOOK
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
                            firebaseErrors(it.result.error.toString(), requireContext())
                        }
                    }
                }
                is LoginViewModel.StateLiveData.PostLogin -> progressbarLayout.hide()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).toolBar.visibility = View.INVISIBLE
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
                        loginViewModel.loginFireBase(editTextEmail.text.toString(),
                            editTextPassword.text.toString(), ActionFireBase.LOGIN, null, null)
                }
                false
            }
        }
        loginButton.setOnClickListener {
            loginViewModel.loginFireBase(
                editTextEmail.text.toString(),
                editTextPassword.text.toString(), ActionFireBase.LOGIN, null, null
            )
        }
        signUpButton.setOnClickListener {
            loginViewModel.loginFireBase(
                editTextEmail.text.toString(),
                editTextPassword.text.toString(), ActionFireBase.REGISTER, null, null
            )
        }
        buttonGoogle.setOnClickListener {

            val googleClient = getClientGoogle(requireContext())
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, Constants.GOOGLE_LOGIN)

        }
        buttonTwitter.setOnClickListener {
            Firebase.auth.signOut()
            loginViewModel.loginFireBase("", "", ActionFireBase.TWITTER, null, activity)

        }
        buttonFacebook.setOnClickListener {
            loginViewModel.loginFireBase("", "", ActionFireBase.FACEBOOK, null, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.GOOGLE_LOGIN -> try { val credential = getCredentialGoogle(data)
                loginViewModel.loginFireBase("", "",
                    ActionFireBase.GOOGLE, credential, null)
            } catch (e: ApiException) { firebaseErrors(e.localizedMessage, requireContext()) }


            Constants.TWITTER_LOGIN -> {

                Log.i("Carpul", "onActivityResult: twitter")
            }
            Constants.FACEBOOK_LOGIN -> Log.i("Carpul", "onActivityResult: facebook")

        }
    }
}