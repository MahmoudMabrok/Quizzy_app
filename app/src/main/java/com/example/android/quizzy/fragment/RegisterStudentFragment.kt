package com.example.android.quizzy.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.quizzy.QuizzyApplication
import com.example.android.quizzy.R
import com.example.android.quizzy.activity.MainActivity
import com.example.android.quizzy.util.Constants
import com.example.android.quizzy.util.Utils
import com.example.android.quizzy.viewModel.LoginViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_register_student.*
import javax.inject.Inject

class RegisterStudentFragment : Fragment() {

    private val TAG = "RegisterStudentFragment"

    @Inject
    lateinit var loginViewModel : LoginViewModel

    private lateinit var disposable: Disposable

    private lateinit var transient: LoginFragment.LoginTransitionInterface

    companion object {
        fun newInstance(map : HashMap<String, Any>) : RegisterStudentFragment{
            val registerStudentFragment = RegisterStudentFragment()
            val args = Bundle()
            args.putSerializable(Constants.INPUTS_KEY, map)
            registerStudentFragment.arguments = args
            return registerStudentFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as QuizzyApplication).component.inject(this)
        transient = activity as LoginFragment.LoginTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get inputs received from Register fragment as arguments
        val userInputs = arguments?.getSerializable(Constants.INPUTS_KEY) as HashMap<String, Any>

        setRegisterButtonClickListener(userInputs)
    }

    private fun setRegisterButtonClickListener(userInputs : HashMap<String, Any>){
        register_student_button.setOnClickListener{
            Log.d(TAG, "register button clicked")
            if(!extractUserInputs(userInputs))
                return@setOnClickListener

            if(!validateUserInputs())
                return@setOnClickListener

            //hide error text
            register_student_error_text_view.visibility = View.GONE

            //show loading progress bar
            register_student_loading_progress_bar.visibility = View.VISIBLE

            if(userInputs.containsKey(Constants.ID_KEY)) {
                //Register using google
                Log.d(TAG, "got id from login fragment")

                disposable = loginViewModel.registerInDBOnly(userInputs).subscribe({
                    successfulRegister(userInputs)
                }, {
                    failureRegister(it)
                })
            }
            else {
                //Normal register
                Log.d(TAG, "got e-mail and password from register fragment")

                disposable = loginViewModel.register(userInputs).subscribe({
                    successfulRegister(userInputs)
                }, {
                    failureRegister(it)
                })
            }
        }
    }

    private fun failureRegister(it: Throwable) {
        Log.d(TAG, "error registering : " + it.message)
        //hide loading progress bar
        register_student_loading_progress_bar.visibility = View.GONE

        showErrorMessage(it.message)
    }

    private fun successfulRegister(userInputs: HashMap<String, Any>) {
        Log.d(TAG, "registered successfully")
        //hide loading progress bar
        register_student_loading_progress_bar.visibility = View.GONE

        //Open Main Activity and attach student's teacher's phone with the intent
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra(Constants.TEACHER_TELEPHONE_NUMBER_KEY, userInputs[Constants.TEACHER_TELEPHONE_NUMBER_KEY] as String)
        intent.putExtra(Constants.STUDENT_NAME_KEY, userInputs[Constants.FIRST_NAME_KEY] as String + " " + userInputs[Constants.LAST_NAME_KEY] as String)
        startActivity(intent)
    }

    private fun extractUserInputs(userInputs: HashMap<String, Any>) : Boolean{
        val firstName = register_student_first_name_edit_text.text.toString().trim()
        if(firstName.isEmpty()) {
            showErrorMessage(R.string.required_student_fields)
            return false
        }
        else{
            userInputs[Constants.FIRST_NAME_KEY] = firstName
        }

        val lastName = register_student_last_name_edit_text.text.toString().trim()
        if(lastName.isEmpty()) {
            showErrorMessage(R.string.required_student_fields)
            return false
        }
        else{
            userInputs[Constants.LAST_NAME_KEY] = lastName
        }

        val teacherTelephoneNumber = register_student_teacher_telephone_number_edit_text.text.toString().trim()
        if(teacherTelephoneNumber.isEmpty()) {
            showErrorMessage(R.string.required_student_fields)
            return false
        }
        else{
            userInputs[Constants.TEACHER_TELEPHONE_NUMBER_KEY] = teacherTelephoneNumber
        }

        return true
    }

    /**
     * validate user inputs
     */
    private fun validateUserInputs() : Boolean{
        //validate first name property
        val firstName = register_student_first_name_edit_text.text.toString().trim()
        if(!firstName.isEmpty()) {
            if(!Utils.isValidName(firstName)) {
                showErrorMessage(R.string.invalid_name)
                return false
            }
        }

        //validate first name property
        val lastName = register_student_last_name_edit_text.text.toString().trim()
        if(!lastName.isEmpty()) {
            if(!Utils.isValidName(lastName)) {
                showErrorMessage(R.string.invalid_name)
                return false
            }
        }

        //validate teacher telephone number
        val teacherTelephoneNumber = register_student_teacher_telephone_number_edit_text.text.toString().trim()
        if(!teacherTelephoneNumber.isEmpty()){
            if(!Utils.isValidTelephoneNumber(teacherTelephoneNumber)) {
                showErrorMessage(R.string.invalid_telephone_number)
                return false
            }
        }

        return true
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId : Int){
        register_student_error_text_view.visibility = View.VISIBLE
        register_student_error_text_view.text = getString(messageId)
    }

    private fun showErrorMessage(message : String?){
        register_student_error_text_view.visibility = View.VISIBLE
        register_student_error_text_view.text = message
    }

}