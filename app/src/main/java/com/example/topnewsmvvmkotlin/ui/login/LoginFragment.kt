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
import com.example.topnewsmvvmkotlin.util.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    enum class ActionFireBase{
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
                     loginViewModel.loginFireBase(editTextEmail.text.toString()
                         , editTextPassword.text.toString(), ActionFireBase.LOGIN, null)
                }
                false
            }
        }

        loginButton.setOnClickListener { loginViewModel.loginFireBase(editTextEmail.text.toString(),
                editTextPassword.text.toString(), ActionFireBase.LOGIN, null)
        }
        signUpButton.setOnClickListener { loginViewModel.loginFireBase(editTextEmail.text.toString(),
                editTextPassword.text.toString(), ActionFireBase.REGISTER,null)
        }
        buttonGoogle.setOnClickListener{

            val googleConfig =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail().build()
            val googleClient = GoogleSignIn.getClient(requireContext(), googleConfig)

            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, 100)

        }
        buttonTwitter.setOnClickListener{
            loginViewModel.loginFireBase("", "", ActionFireBase.TWITTER, null)}
        buttonFacebook.setOnClickListener{
            loginViewModel.loginFireBase("", "", ActionFireBase.FACEBOOK, null)}
    }


    fun firebaseErrors(error: String?) {

        when (error) {
            getString(R.string.networkError) -> makeToast(context, "Please check your conection")
            getString(R.string.formatError) -> makeToast(context, "Please check your Email format")
            getString(R.string.emailInUse) -> makeToast(context,"Email in Use")
            getString(R.string.apiException)  -> makeToast(context,"Choice an option")
            else -> makeToast(context, resources.getString(R.string.loginFailed))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100){
            val task = GoogleSignIn
                .getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential= GoogleAuthProvider
                        .getCredential(account.idToken, null)
                        loginViewModel.loginFireBase("", "",
                            ActionFireBase.GOOGLE, credential = credential)
                }
            } catch (e: ApiException) {
                firebaseErrors(e.localizedMessage)
            }
        }
    }

}