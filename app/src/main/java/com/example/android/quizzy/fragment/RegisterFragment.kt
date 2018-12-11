package com.example.android.quizzy.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.quizzy.R
import com.example.android.quizzy.util.Constants
import com.example.android.quizzy.util.Utils
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val TAG = "RegisterFragment"

    private lateinit var transient: LoginFragment.LoginTransitionInterface

    private lateinit var userInput: HashMap<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transient = activity as LoginFragment.LoginTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNextButtonOnClickListener()
        setClickLoginOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.register)
    }

    private fun setNextButtonOnClickListener() {
        next_button.setOnClickListener {
            Log.d(TAG, "next button clicked")
            //Do not respond to user clicks for now
            it.isClickable = false

            //hide error text view
            register_error_text_view.visibility = View.GONE

            //check internet connection
            if (!Utils.isNetworkConnected(context)) {
                showErrorMessage(R.string.no_internet_connection)
                return@setOnClickListener
            }

            //get email and password entered by user
            userInput = getUserInput()

            //check empty email or password
            if (!Utils.checkEmptyInputs(userInput[Constants.EMAIL_KEY] as String, userInput[Constants.PASSWORD_KEY] as String)) {
                showErrorMessage(R.string.forgot_email_password)
                return@setOnClickListener
            }

            //check email validity
            if (!Utils.isValidEmail(userInput[Constants.EMAIL_KEY] as String)) {
                showErrorMessage(R.string.invalid_email)
                return@setOnClickListener
            }

            //check password validity
            if (!Utils.isValidPassword(userInput[Constants.PASSWORD_KEY] as String)) {
                showErrorMessage(R.string.invalid_password)
                return@setOnClickListener
            }

            performNext(userInput)
        }
    }

    private fun performNext(inputs: HashMap<String, Any>) {
        if (register_radio_student.isChecked) {
            transient.openFragment(RegisterStudentFragment.newInstance(inputs))
        } else if (register_radio_teacher.isChecked) {
            transient.openFragment(RegisterTeacherFragment.newInstance(inputs))
        } else {
            showErrorMessage(R.string.check_student_teacher)
        }
    }

    private fun setClickLoginOnClickListener() {
        click_login_text_view.setOnClickListener {
            transient.openFragment(LoginFragment())
        }
    }

    /**
     * extract user inputs in a HashMap
     */
    private fun getUserInput(): HashMap<String, Any> {
        val userInput: HashMap<String, Any> = HashMap()
        userInput[Constants.EMAIL_KEY] = register_email_edit_text.text.trim().toString()
        userInput[Constants.PASSWORD_KEY] = register_password_edit_text.text.trim().toString()
        return userInput
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId: Int) {
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = getString(messageId)
        next_button.isClickable = true
    }

}