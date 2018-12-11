package com.example.android.quizzy.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.quizzy.QuizzyApplication
import com.example.android.quizzy.R
import com.example.android.quizzy.activity.MainActivity
import com.example.android.quizzy.model.Student
import com.example.android.quizzy.model.Teacher
import com.example.android.quizzy.util.Constants
import com.example.android.quizzy.util.Utils
import com.example.android.quizzy.viewModel.LoginViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment() {

    private val TAG = "LoginFragment"

    @Inject
    lateinit var loginViewModel: LoginViewModel
    private lateinit var disposable: Disposable
    private lateinit var transient: LoginTransitionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as QuizzyApplication).component.inject(this)
        transient = activity as LoginTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoginButtonOnClickListener()
        setClickRegisterOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.login)
    }

    private fun setLoginButtonOnClickListener() {
        login_button.setOnClickListener {
            Log.d(TAG, "login button clicked")
            //Do not respond to user clicks for now
            it.isClickable = false

            //hide error text view
            login_error_text_view.visibility = View.GONE

            //check internet connection
            if (!Utils.isNetworkConnected(context)) {
                showErrorMessage(R.string.no_internet_connection)
                return@setOnClickListener
            }

            //get email and password entered by user
            val userInput: HashMap<String, String> = getUserInput()

            //check empty email or password
            if (!Utils.checkEmptyInputs(userInput[Constants.EMAIL_KEY], userInput[Constants.PASSWORD_KEY])) {
                showErrorMessage(R.string.forgot_email_password)
                return@setOnClickListener
            }

            //check email validity
            if (!Utils.isValidEmail(userInput[Constants.EMAIL_KEY])) {
                showErrorMessage(R.string.invalid_email)
                return@setOnClickListener
            }

            //check password validity
            if (!Utils.isValidPassword(userInput[Constants.PASSWORD_KEY])) {
                showErrorMessage(R.string.invalid_password)
                return@setOnClickListener
            }

            //show loading progress bar
            login_loading_progress_bar.visibility = View.VISIBLE

            //Call web service
            callLoginApi(userInput)
        }
    }

    private fun callLoginApi(body: HashMap<String, String>) {
        disposable = loginViewModel.login(body).subscribe({
            Log.d(TAG, "logged in")
            //hide loading progress bar
            login_loading_progress_bar.visibility = View.GONE

            //Open Main Activity and attach teacher's number
            val intent = Intent(context, MainActivity::class.java)

            if (it is Teacher) {
                Log.d(TAG, "Got teacher with number : " + it.telephoneNumber)
                intent.putExtra(Constants.TELEPHONE_NUMBER_KEY, it.telephoneNumber)
            } else if (it is Student) {
                Log.d(TAG, "Got student with teacher's number : " + it.teacherTelephoneNumber)
                intent.putExtra(Constants.TEACHER_TELEPHONE_NUMBER_KEY, it.teacherTelephoneNumber)
                intent.putExtra(Constants.STUDENT_NAME_KEY, it.firstName + " " + it.lastName)
            } else {
                Log.d(TAG, "Neither a teacher nor a student")
                throw(Exception())
            }

            startActivity(intent)
            activity?.finish()
            Toast.makeText(activity, R.string.signed_in, Toast.LENGTH_SHORT).show()
        }, {
            Log.d(TAG, "Error logging : " + it.message)
            //hide loading progress bar
            login_loading_progress_bar.visibility = View.GONE

            //show error message
            showErrorMessage(it.message)
        })
    }

    fun setClickRegisterOnClickListener() {
        click_register_text_view.setOnClickListener {
            transient.openFragment(RegisterFragment())
        }
    }

    /**
     * extract email and password of user in a HashMap
     */
    private fun getUserInput(): HashMap<String, String> {
        val userInput: HashMap<String, String> = HashMap()
        userInput[Constants.EMAIL_KEY] = login_email_edit_text.text.trim().toString()
        userInput[Constants.PASSWORD_KEY] = login_password_edit_text.text.trim().toString()
        return userInput
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId: Int) {
        login_error_text_view.visibility = View.VISIBLE
        login_error_text_view.text = getString(messageId)
        login_button.isClickable = true
    }

    private fun showErrorMessage(message: String?) {
        login_error_text_view.visibility = View.VISIBLE
        login_error_text_view.text = message
        login_button.isClickable = true
    }

    interface LoginTransitionInterface {
        fun openFragment(fragment: Fragment)
    }

}