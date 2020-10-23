package com.example.topnewsmvvmkotlin.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.getClientGoogle
import com.example.topnewsmvvmkotlin.data.getCredentialFacebook
import com.example.topnewsmvvmkotlin.data.getCredentialGoogle
import com.example.topnewsmvvmkotlin.ui.MainActivity
import com.example.topnewsmvvmkotlin.utils.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.log


class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()
    private val callbackManager = CallbackManager.Factory.create()

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
                            Log.i("Carpul", "onAttach: ${it.result.error}")
                            firebaseErrors(it.result.error.toString(), requireContext())
                        }
                    }
                }
                is LoginViewModel.StateLiveData.PostLogin -> progressbarLayout.visibility = View.INVISIBLE
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).apply {
            toolBar.visibility = View.INVISIBLE
            navBottomNavigation.visibility = View.INVISIBLE
        }

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
                Log.i("Carpul", "setOnEditorActionListener ")
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.loginFireBase(
                           username =  editTextEmail.text.toString(),
                            password = editTextPassword.text.toString(),
                            action = ActionFireBase.LOGIN
                        )
                }
                false
            }
        }

        loginButton.setOnClickListener {
            loginViewModel.loginFireBase(
                username = editTextEmail.text.toString(),
                password = editTextPassword.text.toString(), action = ActionFireBase.LOGIN
            )
        }
        signUpButton.setOnClickListener {
            loginViewModel.loginFireBase(
                username = editTextEmail.text.toString(),
                password = editTextPassword.text.toString(), action = ActionFireBase.REGISTER
            )
        }
        buttonGoogle.setOnClickListener {
            val googleClient = getClientGoogle(requireContext())
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, Constants.GOOGLE_LOGIN)

        }
        buttonTwitter.setOnClickListener {
            Firebase.auth.signOut()
            loginViewModel.loginFireBase( action = ActionFireBase.TWITTER,  activity = activity)

        }
        buttonFacebook.setOnClickListener {

            Firebase.auth.signOut()
            buttonFacebook.setReadPermissions("email")
            buttonFacebook.fragment = this
            buttonFacebook.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {

                    override fun onSuccess(result: LoginResult) {
                        val token = result.accessToken
                        val credential = getCredentialFacebook(token.token)
                        loginViewModel.loginFireBase(
                            "",
                            "",
                            ActionFireBase.FACEBOOK,
                            credential,
                            activity
                        )
                    }

                    override fun onCancel() {}

                    override fun onError(error: FacebookException?) {
                        LoginViewModel.StateLiveData.RefreshUi(Result.GenericError(error?.message))
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.GOOGLE_LOGIN -> try {
                val credential = getCredentialGoogle(data)
                loginViewModel.loginFireBase(
                    "", "",
                    ActionFireBase.GOOGLE, credential, null
                )
            } catch (e: ApiException) {
                firebaseErrors(e.localizedMessage, requireContext())
            }
        }
    }
}