package com.example.topnewsmvvmkotlin.data

import android.content.Context
import android.content.Intent
import com.example.topnewsmvvmkotlin.util.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getApiService(): NewsApiClient
    = Retrofit.Builder()
        .baseUrl(Constants.BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClient())
        .build().run {
            create(NewsApiClient::class.java)
        }


fun getOkHttpClient(): OkHttpClient {

    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder().addInterceptor(logging).build()
}

/** Login Google*/

val googleConfig =

    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(Constants.DEFAUL_WEB_CLIENT_ID)
        .requestEmail().build()

fun getClientGoogle(context: Context): GoogleSignInClient {
    return GoogleSignIn.getClient(context, googleConfig)
}

fun getCredentialGoogle(data: Intent?): AuthCredential {
    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
    val account = task.getResult(ApiException::class.java)
    return GoogleAuthProvider.getCredential(account?.idToken, null)
}

/** Login Facebook */
fun getCredentialFacebook(token: String): AuthCredential {
    return FacebookAuthProvider.getCredential(token)
}

