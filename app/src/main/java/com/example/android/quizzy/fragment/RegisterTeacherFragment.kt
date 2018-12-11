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
import kotlinx.android.synthetic.main.fragment_register_teacher.*
import javax.inject.Inject

class RegisterTeacherFragment : Fragment() {

    private val TAG = "RegisterTeacherFragment"

    @Inject
    lateinit var loginViewModel : LoginViewModel

    private lateinit var disposable: Disposable

    private lateinit var transient: LoginFragment.LoginTransitionInterface

    companion object {
        fun newInstance(map : HashMap<String, Any>) : RegisterTeacherFragment{
            val registerTeacherFragment = RegisterTeacherFragment()
            val args = Bundle()
            args.putSerializable(Constants.INPUTS_KEY, map)
            registerTeacherFragment.arguments = args
            return registerTeacherFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as QuizzyApplication).component.inject(this)
        transient = activity as LoginFragment.LoginTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_teacher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get inputs received from Register fragment as arguments
        val userInputs = arguments?.getSerializable(Constants.INPUTS_KEY) as HashMap<String, Any>

        setRegisterButtonClickListener(userInputs)
    }

    private fun setRegisterButtonClickListener(userInputs : HashMap<String, Any>){
        register_teacher_button.setOnClickListener {
            if(!extractUserInputs(userInputs))
                return@setOnClickListener

            if(!validateUserInputs())
                return@setOnClickListener

            //hide error text
            register_teacher_error_text_view.visibility = View.GONE

            //show loading progress bar
            register_teacher_loading_progress_bar.visibility = View.VISIBLE

            if(userInputs.containsKey(Constants.ID_KEY)) {
                //Register using Google
                Log.d(TAG, "got id from login fragment")

                disposable = loginViewModel.registerInDBOnly(userInputs).subscribe({
                    successfulRegister(userInputs)
                }, {
                    failureRegister(it)
                })
            }
            else {
                //Register normally
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
        register_teacher_loading_progress_bar.visibility = View.GONE

        showErrorMessage(it.message)
    }

    private fun successfulRegister(userInputs: HashMap<String, Any>) {
        Log.d(TAG, "registered successfully")
        //hide loading progress bar
        register_teacher_loading_progress_bar.visibility = View.GONE

        //Open Main Acivity and attach teacher's telephone number
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra(Constants.TELEPHONE_NUMBER_KEY, userInputs[Constants.TELEPHONE_NUMBER_KEY] as String)
        startActivity(intent)
    }

    private fun extractUserInputs(userInputs: HashMap<String, Any>) : Boolean{
        val firstName = register_teacher_first_name_edit_text.text.toString().trim()
        if(firstName.isEmpty()) {
            showErrorMessage(R.string.required_teacher_fields)
            return false
        }
        else{
            userInputs[Constants.FIRST_NAME_KEY] = firstName
        }

        val lastName = register_teacher_last_name_edit_text.text.toString().trim()
        if(lastName.isEmpty()) {
            showErrorMessage(R.string.required_teacher_fields)
            return false
        }
        else{
            userInputs[Constants.LAST_NAME_KEY] = lastName
        }

        val telephoneNumber = register_teacher_telephone_number_edit_text.text.toString().trim()
        if(telephoneNumber.isEmpty()) {
            showErrorMessage(R.string.required_teacher_fields)
            return false
        }
        else{
            userInputs[Constants.TELEPHONE_NUMBER_KEY] = telephoneNumber
        }

        val subject = register_teacher_subject_edit_text.text.toString().trim()
        if(subject.isEmpty()) {
            showErrorMessage(R.string.required_teacher_fields)
            return false
        }
        else{
            userInputs[Constants.SUBJECT_KEY] = subject
        }

        return true
    }

    /**
     * validate user inputs
     */
    private fun validateUserInputs() : Boolean{
        //validate first name property
        val firstName = register_teacher_first_name_edit_text.text.toString().trim()
        if(!firstName.isEmpty()) {
            if(!Utils.isValidName(firstName)) {
                showErrorMessage(R.string.invalid_name)
                return false
            }
        }

        //validate first name property
        val lastName = register_teacher_last_name_edit_text.text.toString().trim()
        if(!lastName.isEmpty()) {
            if(!Utils.isValidName(lastName)) {
                showErrorMessage(R.string.invalid_name)
                return false
            }
        }

        //validate telephone number
        val telephoneNumber = register_teacher_telephone_number_edit_text.text.toString().trim()
        if(!telephoneNumber.isEmpty()){
            if(!Utils.isValidTelephoneNumber(telephoneNumber)) {
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
        register_teacher_error_text_view.visibility = View.VISIBLE
        register_teacher_error_text_view.text = getString(messageId)
    }

    private fun showErrorMessage(message : String?){
        register_teacher_error_text_view.visibility = View.VISIBLE
        register_teacher_error_text_view.text = message
    }

}