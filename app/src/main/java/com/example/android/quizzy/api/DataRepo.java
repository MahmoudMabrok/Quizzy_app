package com.example.android.quizzy.api;

import com.example.android.quizzy.model.AttemptedQuiz;
import com.example.android.quizzy.model.Award;
import com.example.android.quizzy.model.NotifactionItem;
import com.example.android.quizzy.model.Quiz;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class DataRepo {
    private FirebaseDataSource firebaseDataSource;

    public DataRepo() {
        firebaseDataSource = new FirebaseDataSource();
    }

    public void addQuiz(Quiz quiz) {
        firebaseDataSource.addQuiz(quiz.getTeacherKey(), quiz);
    }

    public DatabaseReference getSpecificQuizRef(String teacherKey, String quizzKey) {
        return firebaseDataSource.getSpecificQuizRef(teacherKey, quizzKey);
    }

    public String getUUID() {
        return firebaseDataSource.getCurrentUserUUID();
    }

    public DatabaseReference getCompleteListRef(String teacherID, String studentUUID) {
        return firebaseDataSource.getCompleteListRef(teacherID, studentUUID);
    }

    public void addQuizTOCompleteList(Quiz quiz, String sID) {
        firebaseDataSource.addQuizTOCompleteList(quiz, sID);
    }

    public DatabaseReference getStudentCompletedQuizz(String teacher, String sID, String quizeID) {
        return firebaseDataSource.getStudentCompletedQuizz(teacher, sID, quizeID);
    }

    public void addAttemted(AttemptedQuiz attemptedQuiz, String quizeID, String teacher) {
        firebaseDataSource.addAttemted(attemptedQuiz, quizeID, teacher);
    }

    public DatabaseReference getStudentName(String studentUUID, String teacherUUID) {
        return firebaseDataSource.getStudentName(studentUUID, teacherUUID);
    }

    public DatabaseReference getNotificationRef(String uuid) {
        return firebaseDataSource.getNotifcationRef(uuid).getRef();
    }

    public void addNotification(String teacherUUID, NotifactionItem notifactionItem) {
        firebaseDataSource.addNotification(teacherUUID, notifactionItem);
    }

    public void addNotification(NotifactionItem notifactionItem) {
    }

    public DatabaseReference getTeacherQuizz(String teacherKey) {
        return firebaseDataSource.getTeacherQuizz(teacherKey);
    }

    public DatabaseReference getStudentOfTeacherRef(String teacherKey) {
        return firebaseDataSource.getStudentOfTeacherRef(teacherKey);
    }

    public void addAward(Award award) {
        firebaseDataSource.addAward(award);
    }
}
