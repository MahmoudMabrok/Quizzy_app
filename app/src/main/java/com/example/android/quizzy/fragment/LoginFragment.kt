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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class LoginFragment : Fragment() {

    private val TAG = "LoginFragment"

    @Inject
    lateinit var loginViewModel : LoginViewModel

    private lateinit var callbackManager : CallbackManager

    private lateinit var auth : FirebaseAuth

    private lateinit var disposable : Disposable
    lateinit var transient: LoginTransitionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as QuizzyApplication).component.inject(this)
        transient = activity as LoginTransitionInterface
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoginButtonOnClickListener()
        setClickRegisterOnClickListener()
        setGoogleLoginButtonOnClickListener()
        setFacebookLoginButtonOnClickListener()
    }

    private fun setLoginButtonOnClickListener(){
        login_button.setOnClickListener {
            Log.d(TAG, "login button clicked")
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
            val userInput = getUserInput()

            //check empty email or password
            if(!Utils.checkEmptyInputs(userInput[Constants.EMAIL_KEY], userInput[Constants.PASSWORD_KEY])){
                showErrorMessage(R.string.forgot_email_password)
                return@setOnClickListener
            }

            //check email validity
            if(!Utils.isValidEmail(userInput[Constants.EMAIL_KEY])){
                showErrorMessage(R.string.invalid_email)
                return@setOnClickListener
            }

            //check password validity
            if(!Utils.isValidPassword(userInput[Constants.PASSWORD_KEY])){
                showErrorMessage(R.string.invalid_password)
                return@setOnClickListener
            }

            //show loading progress bar
            showLoading()

            //Call web service
            callLoginApi(userInput)
        }
    }

    private fun callLoginApi(body : HashMap<String, String>) {
        disposable = loginViewModel.login(body).subscribe({
            Log.d(TAG, "logged in")
            //hide loading progress bar
            hideLoading()

            navigateUser(it)
        }, {
            Log.d(TAG, "Error logging : " + it.message)
            //hide loading progress bar
            hideLoading()

            //show error message
            showErrorMessage(it.message)
        })
    }

    private fun navigateUser(it: User?) {
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
    }

    fun setClickRegisterOnClickListener(){
        click_register_text_view.setOnClickListener{
            transient.openFragment(RegisterFragment())
        }
    }

    /**
     * extract email and password of user in a HashMap
     */
    private fun getUserInput() : HashMap<String, String>{
        val userInput : HashMap<String, String> = HashMap()
        userInput[Constants.EMAIL_KEY] = login_email_edit_text.text.trim().toString()
        userInput[Constants.PASSWORD_KEY] = login_password_edit_text.text.trim().toString()
        return userInput
    }

    interface LoginTransitionInterface{
        fun openFragment(fragment : Fragment)
    }

    private fun setGoogleLoginButtonOnClickListener() {
        login_google_button.setOnClickListener {
            authUsingGoogle()
        }
    }

    private fun authUsingGoogle() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
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

            handleGoogleSignInResult(result)
        }
        else {     //result returning from facebook sign
            // Pass the activity result back to the Facebook SDK
            Log.d(TAG, "intent regards facebook login")
            callbackManager.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            signInWithGoogleCredential(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            e.printStackTrace()
            showErrorMessage(e.message)
        }
    }

    private fun signInWithGoogleCredential(account: GoogleSignInAccount) {
        Log.d(TAG, "Sign in with credential")
        //show loading progress bar
        showLoading()

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
                        hideLoading()

                        showErrorMessage(R.string.error_login)
                        Log.w(TAG, "signInWithCredential:failure", it.exception)
                    }
                }
    }

    private fun checkUserExistence(currentUser: FirebaseUser?) {
        loginViewModel.getUser(currentUser?.uid).subscribe({
            Log.d(TAG, "got user")

            //hide loading progress bar
            hideLoading()

            navigateUser(it)
        }, {
            Log.d(TAG, "got no user with error: ${it.message}")

            //hide loading progress bar
            hideLoading()

            //show dialog to ask user whether he is a teacher or a student
            val input: HashMap<String, Any> = HashMap()
            input[Constants.ID_KEY] = currentUser?.uid as String

            val dialog = Student_TeacherDialog(context!!, this, input)
            //if user dismisses the dialog, sign him out
            dialog.setOnDismissListener{
                auth.signOut()
                LoginManager.getInstance().logOut()
            }
            dialog.show()
        })
    }

    private fun setFacebookLoginButtonOnClickListener() {
        login_facebook_button.setOnClickListener {
            authUsingFacebook()
        }
    }

    private fun authUsingFacebook() {
        Log.d(TAG, "auth using facebook executes")

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        login_facebook_button.fragment = this

        login_facebook_button.setReadPermissions(getString(R.string.email_permission), getString(R.string.profile_permission))

        login_facebook_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess: " + loginResult)
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError ${error.message}")
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
                        hideLoading()

                        val user = auth.currentUser
                        checkUserExistence(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", it.exception)

                        //hide loading progress bar
                        hideLoading()

                        showErrorMessage(R.string.error_login)
                    }
                }
    }

    private fun showLoading() {
        login_loading_progress_bar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        login_loading_progress_bar.visibility = View.GONE
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId : Int){
        login_error_text_view.visibility = View.VISIBLE
        login_error_text_view.text = getString(messageId)
        login_button.isClickable = true
    }

    private fun showErrorMessage(message : String?){
        login_error_text_view.visibility = View.VISIBLE
        login_error_text_view.text = message
        login_button.isClickable = true
    }

    private fun hideError() {
        login_error_text_view.visibility = View.GONE
    }

}