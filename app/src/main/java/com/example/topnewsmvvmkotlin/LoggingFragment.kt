package com.example.topnewsmvvmkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.topnewsmvvmkotlin.util.makeToast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_logging.*


class LoggingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLoggin()
    }

    fun setupLoggin(){
        signUpButton.setOnClickListener{
            if(editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(editTextEmail.text.toString(),
                        editTextPassword.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            findNavController().navigate(LoggingFragmentDirections.actionLoggingFragmentToHomeFragment())
                        }else{
                            makeToast(context,"Intente de Nuevo")
                        }
                    }

            }
        }
        loggingButtom.setOnClickListener{
            if(editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(editTextEmail.text.toString(),
                        editTextPassword.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            findNavController().navigate(LoggingFragmentDirections.actionLoggingFragmentToHomeFragment())
                        }else{
                            makeToast(context,"Intente de Nuevo")
                        }
                    }

            }
        }
    }
}