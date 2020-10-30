package com.lenakurasheva.notes.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.lenakurasheva.notes.data.errors.NoAuthException
import com.lenakurasheva.notes.R

abstract class BaseActivity<T, S: BaseViewState<T>> : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN = 3737
    }
    abstract val viewModel : BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }
        viewModel.getViewState().observe(this, { value ->
            value ?: return@observe
            value.error?.let{
                renderError(it)
                return@observe
            }
            renderData(value.data)
        })
    }

    protected fun renderError(error: Throwable){
        when(error){
            is NoAuthException -> startLogin()
            else -> error?.message?.let{
                showError(it)
            }
        }
    }

    protected fun showError(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    abstract fun renderData(data: T)

    fun startLogin(){
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.drawable.android_robot)
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .build()

        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK){
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}