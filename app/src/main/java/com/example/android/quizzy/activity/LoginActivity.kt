package com.example.android.quizzy.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Window
import com.example.android.quizzy.R
import com.example.android.quizzy.fragment.LoginFragment

class LoginActivity : AppCompatActivity() , LoginFragment.LoginTransitionInterface{

    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        openFragment(this, LoginFragment())
    }

    override fun openFragment(caller: Any, fragment: Fragment) {
        //when opening the LoginFragment from MainActivity, do not add to BackStack
        if(fragment is LoginFragment && caller is LoginActivity){
            Log.d(TAG, "open LoginFragment from LoginActivity")
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.login_frame_layout, fragment)
                    .commit()
        }
        else {
            Log.d(TAG, "open fragment other than LoginFragment")
            supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.login_frame_layout, fragment)
                    .commit()
        }
    }
}
