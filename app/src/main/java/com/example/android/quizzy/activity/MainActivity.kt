package com.example.android.quizzy.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.android.quizzy.R
import com.example.android.quizzy.util.Constants
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check if user logged or still needs
        if (FirebaseAuth.getInstance().currentUser == null) {
            Log.d(TAG, "there is no firebase user")
            val intent = Intent(applicationContext, WalkThroughActivty::class.java)
            startActivity(intent)
        } else {
            Log.d(TAG, "there is firebase user")
            var gotTeacherNumber: String?
            var gotTeacherName: String?
            var gotStudentName: String?

            //check if shared preferences has values
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            if (sharedPref.contains(Constants.TELEPHONE_NUMBER_KEY)) { //teacher logged before
                Log.d(TAG, "shared pref contains telephone number key")
                gotTeacherNumber = sharedPref.getString(Constants.TELEPHONE_NUMBER_KEY, null)
                gotTeacherName = sharedPref.getString(Constants.Teacher_NAME, null)
                //Navigate to Teacher activity
                moveToTeacherActivity(gotTeacherNumber, gotTeacherName)
            } else if (sharedPref.contains(Constants.TEACHER_TELEPHONE_NUMBER_KEY)) { //student logged before
                Log.d(TAG, "shared pref contains teacher telephone number key")
                gotTeacherNumber = sharedPref.getString(Constants.TEACHER_TELEPHONE_NUMBER_KEY, null)
                gotStudentName = sharedPref.getString(Constants.STUDENT_NAME_KEY, null)
                //Navigate to Student activity
                moveToStudentActivity(gotTeacherNumber, gotStudentName)
            } else {
                Log.d(TAG, "there is no shared pref")
                //Check getting number from login/register
                if (intent.extras.containsKey(Constants.TELEPHONE_NUMBER_KEY)) { //Teacher logged
                    Log.d(TAG, "got telephone number")
                    //isTeacher = true
                    gotTeacherNumber = intent.extras[Constants.TELEPHONE_NUMBER_KEY] as String
                    gotTeacherName = intent.extras[Constants.Teacher_NAME] as String
                    //store at shared preferences
                    getPreferences(Context.MODE_PRIVATE).edit().putString(Constants.TELEPHONE_NUMBER_KEY, gotTeacherNumber).apply()
                    getPreferences(Context.MODE_PRIVATE).edit().putString(Constants.Teacher_NAME, gotTeacherNumber).apply()
                    //Navigate to Teacher activity
                    moveToTeacherActivity(gotTeacherNumber, gotTeacherName)
                }

                if (intent.extras.containsKey(Constants.TEACHER_TELEPHONE_NUMBER_KEY)) { //Student logged
                    Log.d(TAG, "got teacher telephone number")
                    //isTeacher = false
                    gotTeacherNumber = intent.extras[Constants.TEACHER_TELEPHONE_NUMBER_KEY] as String
                    gotStudentName = intent.extras[Constants.STUDENT_NAME_KEY] as String
                    //store at shared preferences
                    getPreferences(Context.MODE_PRIVATE).edit().putString(Constants.TEACHER_TELEPHONE_NUMBER_KEY, gotTeacherNumber).apply()
                    getPreferences(Context.MODE_PRIVATE).edit().putString(Constants.STUDENT_NAME_KEY, gotStudentName).apply()
                    //Navigate to Student activity
                    moveToStudentActivity(gotTeacherNumber, gotStudentName)
                }
            }
        }
    }

    private fun moveToTeacherActivity(telephoneNumber: String, name: String) {
        val intent = Intent(this, TeacherHome::class.java)
        intent.putExtra(Constants.TELEPHONE_NUMBER_KEY, telephoneNumber)
        intent.putExtra(Constants.Teacher_NAME, name)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun moveToStudentActivity(teacherTelephoneNumber: String, studentName: String) {
        val intent = Intent(this, StudentActivity::class.java)
        intent.putExtra(Constants.TEACHER_TELEPHONE_NUMBER_KEY, teacherTelephoneNumber)
        intent.putExtra(Constants.STUDENT_NAME_KEY, studentName)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
