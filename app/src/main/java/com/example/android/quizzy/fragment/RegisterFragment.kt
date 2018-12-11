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
import com.example.android.quizzy.model.User
import com.example.android.quizzy.util.Constants
import com.example.android.quizzy.util.Utils
import com.example.android.quizzy.viewModel.LoginViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject

class RegisterFragment : Fragment() {

    private val TAG = "RegisterFragment"

    @Inject
    lateinit var loginViewModel : LoginViewModel

    private lateinit var callbackManager : CallbackManager

    private lateinit var transient: LoginFragment.LoginTransitionInterface

    private lateinit var auth : FirebaseAuth

    private lateinit var userInput : HashMap<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transient = activity as LoginFragment.LoginTransitionInterface
        (activity?.application as QuizzyApplication).component.inject(this)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNextButtonOnClickListener()
        setClickLoginOnClickListener()
        setGoogleRegisterButtonOnClickListener()
        setFacebookRegisterButtonOnClickListener()
    }

    private fun setGoogleRegisterButtonOnClickListener() {
        register_google_button.setOnClickListener{
            authUsingGoogle()
        }
    }

    private fun setNextButtonOnClickListener(){
        next_button.setOnClickListener {
            Log.d(TAG, "next button clicked")
            //Do not respond to user clicks for now
            it.isClickable = false

            //hide error text view
            hideError()

            //check internet connection
            if(!Utils.isNetworkConnected(context)){
                showErrorMessage(R.string.no_internet_connection)
                return@setOnClickListener
            }

            //get email and password entered by user
            userInput = getUserInput()

            //check empty email or password
            if(!Utils.checkEmptyInputs(userInput[Constants.EMAIL_KEY] as String, userInput[Constants.PASSWORD_KEY] as String)){
                showErrorMessage(R.string.forgot_email_password)
                return@setOnClickListener
            }

            //check email validity
            if(!Utils.isValidEmail(userInput[Constants.EMAIL_KEY] as String)){
                showErrorMessage(R.string.invalid_email)
                return@setOnClickListener
            }

            //check password validity
            if(!Utils.isValidPassword(userInput[Constants.PASSWORD_KEY] as String)){
                showErrorMessage(R.string.invalid_password)
                return@setOnClickListener
            }

            performNext(userInput)
        }
    }

    private fun setClickLoginOnClickListener(){
        click_login_text_view.setOnClickListener{
            transient.openFragment(LoginFragment())
        }
    }

    /**
     * extract user inputs in a HashMap
     */
    private fun getUserInput() : HashMap<String, Any>{
        val userInput : HashMap<String, Any> = HashMap()
        userInput[Constants.EMAIL_KEY] = register_email_edit_text.text.trim().toString()
        userInput[Constants.PASSWORD_KEY] = register_password_edit_text.text.trim().toString()
        return userInput
    }

    private fun authUsingGoogle() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)

        val registerIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(registerIntent, Constants.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "Got intent in onActivityResult")

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            Log.d(TAG, "Intent regards google login ")
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val result = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleGoogleRegisterResult(result)
        }
        else {     //result returning from facebook sign
            // Pass the activity result back to the Facebook SDK
            Log.d(TAG, "intent regards facebook login")
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleGoogleRegisterResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            registerWithGoogleCredential(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            e.printStackTrace()
            showErrorMessage(R.string.error_register)
        }
    }

    private fun registerWithGoogleCredential(account: GoogleSignInAccount) {
        Log.d(TAG, "Sign in with credential")
        //show loading progress bar
        register_loading_progress_bar.visibility = View.VISIBLE

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign in success
                        Log.d(TAG, "signInWithCredential:success")

                        val currentUser = auth.currentUser

                        checkUserExistence(currentUser)
                    } else {
                        // If sign in fails, display a message to the user.
                        //hide loading progress bar
                        hideLading()

                        showErrorMessage(R.string.error_login)
                        Log.w(TAG, "signInWithCredential:failure", it.exception)
                    }
                }
    }

    /**
     * checks if user exists in the database or not
     */
    private fun checkUserExistence(currentUser: FirebaseUser?) {
        loginViewModel.getUser(currentUser?.uid).subscribe({
            Log.d(TAG, "got user")

            //hide loading progress bar
            hideLading()

            navigateUser(it)
        }, {
            Log.d(TAG, "got no user with error: ${it.message}")

            //hide loading progress bar
            hideLading()

            val input: HashMap<String, Any> = HashMap()
            input[Constants.ID_KEY] = currentUser?.uid as String

            performNext(input)
        })
    }

    /**
     * checks gotten user whether is a student or a teacher and sends appropriate values in the intent
     */
    private fun navigateUser(user: User) {
        //Open Main Activity and attach teacher's number
        val intent = Intent(context, MainActivity::class.java)

        if (user is Teacher) {
            Log.d(TAG, "Got teacher with number : " + user.telephoneNumber)
            intent.putExtra(Constants.TELEPHONE_NUMBER_KEY, user.telephoneNumber)
        } else if (user is Student) {
            Log.d(TAG, "Got student with teacher's number : " + user.teacherTelephoneNumber)
            intent.putExtra(Constants.TEACHER_TELEPHONE_NUMBER_KEY, user.teacherTelephoneNumber)
            intent.putExtra(Constants.STUDENT_NAME_KEY, user.firstName + " " + user.lastName)
        } else {
            Log.d(TAG, "Neither a teacher nor a student")
            throw(Exception())
        }

        startActivity(intent)
        activity?.finish()
        Toast.makeText(activity, R.string.already_member, Toast.LENGTH_SHORT).show()
    }

    private fun performNext(inputs : HashMap<String, Any>){
        if(register_radio_student.isChecked){
            transient.openFragment(RegisterStudentFragment.newInstance(inputs))
        }
        else if(register_radio_teacher.isChecked){
            transient.openFragment(RegisterTeacherFragment.newInstance(inputs))
        }
        else{
            showErrorMessage(R.string.check_student_teacher)
        }
    }

    private fun setFacebookRegisterButtonOnClickListener() {
        register_facebook_button.setOnClickListener {
            authUsingFacebook()
        }
    }

    private fun authUsingFacebook() {
        Log.d(TAG, "auth using facebook executes")

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        register_facebook_button.fragment = this

        register_facebook_button.setReadPermissions(getString(R.string.email_permission), getString(R.string.profile_permission))

        register_facebook_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess: " + loginResult)
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                showErrorMessage(error.message)
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken){
        Log.d(TAG, "handleFacebookAccessToken:$token")
        //show loading progress bar
        showLoading()

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener{
                    Log.d(TAG, "got completed sign in with credential")
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")

                        //hide loading progress bar
                        hideLading()

                        val user = auth.currentUser
                        checkUserExistence(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", it.exception)

                        //hide loading progress bar
                        hideLading()

                        showErrorMessage(R.string.error_register)
                    }
                }
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId : Int){
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = getString(messageId)
        next_button.isClickable = true
    }

    private fun showErrorMessage(message : String?){
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = message
        next_button.isClickable = true
    }

    private fun showLoading() {
        register_loading_progress_bar.visibility = View.VISIBLE
    }

    private fun hideLading() {
        register_loading_progress_bar.visibility = View.GONE
    }

    private fun hideError() {
        register_error_text_view.visibility = View.GONE
    }

}