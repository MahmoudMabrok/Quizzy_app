package com.example.android.quizzy.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.android.quizzy.R
import com.example.android.quizzy.util.Constants
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check if user logged or still needs
        if(FirebaseAuth.getInstance().currentUser == null){
            val intent = Intent(applicationContext, WalkThroughActivty::class.java)
            startActivity(intent)
        }
        else {
            var gotTeacherNumber: String?
            var gotStudentName: String?

            //check if shared preferences has values
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            if(sharedPref.contains(Constants.TELEPHONE_NUMBER_KEY)){ //teacher logged before
                gotTeacherNumber = sharedPref.getString(Constants.TELEPHONE_NUMBER_KEY, null)
                //Navigate to Teacher activity
                moveToTeacherActivity(gotTeacherNumber)
            }

            else if(sharedPref.contains(Constants.TEACHER_TELEPHONE_NUMBER_KEY)){ //student logged before
                gotTeacherNumber = sharedPref.getString(Constants.TEACHER_TELEPHONE_NUMBER_KEY, null)
                gotStudentName = sharedPref.getString(Constants.STUDENT_NAME_KEY, null)
                //Navigate to Student activity
                moveToStudentActivity(gotTeacherNumber, gotStudentName)
            }

            else{
                //Check getting number from login/register
                if (intent.extras.containsKey(Constants.TELEPHONE_NUMBER_KEY)) { //Teacher logged
                    //isTeacher = true
                    gotTeacherNumber = intent.extras[Constants.TELEPHONE_NUMBER_KEY] as String
                    //store at shared preferences
                    getPreferences(Context.MODE_PRIVATE).edit().putString(Constants.TELEPHONE_NUMBER_KEY, gotTeacherNumber).apply()
                    //Navigate to Teacher activity
                    moveToTeacherActivity(gotTeacherNumber)
                }

                if (intent.extras.containsKey(Constants.TEACHER_TELEPHONE_NUMBER_KEY)) { //Student logged
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

    private fun moveToTeacherActivity(telephoneNumber : String){
        val intent = Intent(this, TeacherHome::class.java)
        intent.putExtra(Constants.TELEPHONE_NUMBER_KEY, telephoneNumber)
        startActivity(intent)
    }

    private fun moveToStudentActivity(teacherTelephoneNumber : String, studentName : String){
        val intent = Intent(this, StudentActivity::class.java)
        intent.putExtra(Constants.TEACHER_TELEPHONE_NUMBER_KEY, teacherTelephoneNumber)
        intent.putExtra(Constants.STUDENT_NAME_KEY, studentName)
        startActivity(intent)
    }
}
