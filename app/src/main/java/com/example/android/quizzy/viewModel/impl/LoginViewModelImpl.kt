package com.example.android.quizzy.viewModel.impl

import android.content.Context
import android.util.Log
import com.example.android.quizzy.R
import com.example.android.quizzy.api.LoginApi
import com.example.android.quizzy.model.Student
import com.example.android.quizzy.model.Teacher
import com.example.android.quizzy.model.User
import com.example.android.quizzy.util.Constants
import com.example.android.quizzy.viewModel.LoginViewModel
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

class LoginViewModelImpl : LoginViewModel {

    private val TAG = "LoginViewModelImpl"

    private val api : LoginApi
    private val context : Context

    constructor(context: Context, api: LoginApi) {
        this.api = api
        this.context = context
    }

    override fun register(body: HashMap<String, Any>): Completable {
        Log.d(TAG, "register executes")
        //check if the user is a teacher or a student
        val teacherUser = teacherOrStudent(body)

        if(teacherUser){
            //teacher
            return api.teacherExists(body[Constants.TELEPHONE_NUMBER_KEY] as String).flatMapCompletable {
                if(it){
                    Log.d(TAG, "teacher already exists")
                    Completable.error(Throwable(context.getString(R.string.teacher_with_same_number)))
                }
                else{
                    api.registerInFirebaseAuth(body.get(Constants.EMAIL_KEY) as String, body.get(Constants.PASSWORD_KEY) as String)
                            .flatMapCompletable { AuthResult ->
                                Log.d(TAG, "registered in fire-base auth")
                                val user : User = Teacher(AuthResult.user.uid)

                                addUserInfo(user, body)

                                api.registerInFirebaseDatabase(user)

                            }
                }
            }
        }
        else{
            //student
            return api.teacherExists(body[Constants.TEACHER_TELEPHONE_NUMBER_KEY] as String).flatMapCompletable {
                if(it){
                    api.registerInFirebaseAuth(body.get(Constants.EMAIL_KEY) as String, body.get(Constants.PASSWORD_KEY) as String)
                            .flatMapCompletable { AuthResult ->
                                Log.d(TAG, "registered in fire-base auth")
                                val user : User = Student(AuthResult.user.uid)

                                addUserInfo(user, body)

                                api.registerInFirebaseDatabase(user)

                            }
                }
                else {
                    Log.d(TAG, "teacher does not exist")
                    Completable.error(Throwable(context.getString(R.string.no_teacher_with_number)))
                }
            }
        }

    }

    private fun teacherOrStudent(body: HashMap<String, Any>) : Boolean {
        if (body.containsKey(Constants.TELEPHONE_NUMBER_KEY)) {
            Log.d(TAG, "register as teacher")
            return true
        }
        else if (body.containsKey(Constants.TEACHER_TELEPHONE_NUMBER_KEY)) {
            Log.d(TAG, "register as student")
            return false
        }
        else {
            Log.d(TAG, "not a teacher neither a student")
            throw Exception()
        }
    }

    override fun registerInDBOnly(body: HashMap<String, Any>): Completable {
        Log.d(TAG, "register only in DB executes")

        //check if the user is a teacher or a student
        val teacherUser = teacherOrStudent(body)

        if(teacherUser){
            //teacher
            return api.teacherExists(body[Constants.TELEPHONE_NUMBER_KEY] as String).flatMapCompletable {
                if(it){
                    Log.d(TAG, "teacher already exists")
                    Completable.error(Throwable(context.getString(R.string.teacher_with_same_number)))
                }
                else{
                    val user : User = Teacher(body[Constants.ID_KEY] as String)

                    addUserInfo(user, body)

                    api.registerInFirebaseDatabase(user)
                }
            }
        }
        else{
            //student
            return api.teacherExists(body[Constants.TEACHER_TELEPHONE_NUMBER_KEY] as String).flatMapCompletable {
                if(it){
                    val user : User = Student(body[Constants.ID_KEY] as String)

                    addUserInfo(user, body)

                    api.registerInFirebaseDatabase(user)
                }
                else {
                    Log.d(TAG, "teacher does not exist")
                    Completable.error(Throwable(context.getString(R.string.no_teacher_with_number)))
                }
            }
        }
    }

    override fun login(body: HashMap<String, String>): Maybe<User> {
        Log.d(TAG, "login executes")
        return api.login(body[Constants.EMAIL_KEY], body[Constants.PASSWORD_KEY]).flatMap {
            api.getUser(it.user.uid).flatMapMaybe {User ->
                Log.d(TAG, "Got user from api")
                Maybe.just(User)
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    private fun addUserInfo(user : User, body: HashMap<String, Any>){

        if(body.containsKey(Constants.FIRST_NAME_KEY)){
            user.firstName = body.get(Constants.FIRST_NAME_KEY) as String
        }
        else{
            Log.d(TAG, "no first name field")
            throw Exception()
        }

        if(body.containsKey(Constants.LAST_NAME_KEY)){
            user.lastName = body.get(Constants.LAST_NAME_KEY) as String
        }
        else{
            Log.d(TAG, "no last name field")
            throw Exception()
        }

        if(body.containsKey(Constants.CITY_KEY)){
            user.city = body.get(Constants.CITY_KEY) as String
        }


        if(user is Teacher){
            if(body.containsKey(Constants.TELEPHONE_NUMBER_KEY)){
                user.telephoneNumber = body.get(Constants.TELEPHONE_NUMBER_KEY) as String
            }
            else{
                Log.d(TAG, "no telephone number field")
                throw Exception()
            }

            if(body.containsKey(Constants.SUBJECT_KEY)){
                user.subject = body.get(Constants.SUBJECT_KEY) as String
            }
            else{
                Log.d(TAG, "no subject field")
                throw Exception()
            }
        }
        else if (user is Student){
            if(body.containsKey(Constants.ACADEMIC_YEAR_KEY)){
                user.academicYear = body.get(Constants.ACADEMIC_YEAR_KEY) as String
            }

            if(body.containsKey(Constants.TEACHER_TELEPHONE_NUMBER_KEY)){
                user.teacherTelephoneNumber = body.get(Constants.TEACHER_TELEPHONE_NUMBER_KEY) as String
            }
            else{
                Log.d(TAG, "no teacher telephone number field")
                throw Exception()
            }
        }

    }

    override fun getUser(id: String?): Single<User> {
        return api.getUser(id)
    }
}