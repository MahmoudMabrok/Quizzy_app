package com.example.android.quizzy.api;

import com.example.android.quizzy.model.AttemptedQuiz;
import com.example.android.quizzy.model.Award;
import com.example.android.quizzy.model.NotifactionItem;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class FirebaseDataSource {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    DatabaseReference teacherRef;
    DatabaseReference studentRef;
    FirebaseAuth auth;

    public FirebaseDataSource() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        userRef = firebaseDatabase.getReference(Constants.USERS_KEY);
        teacherRef = userRef.child(Constants.TEACHERS_KEY);
        studentRef = userRef.child(Constants.STUDENTS_KEY);
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public String getCurrentUserUUID() {
        return auth.getCurrentUser().getUid();
    }


    public void addQuiz(String teacherKey, Quiz quiz) {
        if (quiz.getKey() == null) {
            String key = teacherRef.child(teacherKey).child(Constants.QUIZZ_CHILD).push().getKey();
            quiz.setKey(key);
        }
        teacherRef.child(teacherKey).child(Constants.QUIZZ_CHILD).child(quiz.getKey()).setValue(quiz);
    }

    public void updateQuiz(String teacherKey, Quiz quiz) {
        teacherRef.child(teacherKey).child(Constants.QUIZZ_CHILD).child(quiz.getKey()).setValue(quiz);
    }

    public DatabaseReference getSpecificQuizRef(String teacherKey, String quizzKey) {
        return teacherRef.child(teacherKey).child(Constants.QUIZZ_CHILD).child(quizzKey).getRef();
    }

    public DatabaseReference getCompleteListRef(String teacherID, String studentUUID) {
        return teacherRef.child(teacherID).child(Constants.STUDENTS_KEY).child(studentUUID).child(Constants.COMPLETED_QUIZZ).getRef();
    }

    public void addQuizTOCompleteList(Quiz quiz, String sID) {
        teacherRef.child(quiz.getTeacherKey())
                .child(Constants.STUDENTS_KEY)
                .child(sID)
                .child(Constants.COMPLETED_QUIZZ)
                .child(quiz.getKey())
                .setValue(quiz);
    }

    public DatabaseReference getStudentCompletedQuizz(String teacher, String sID, String quizeID) {
        return teacherRef
                .child(teacher)
                .child(Constants.STUDENTS_KEY)
                .child(sID).child(Constants.COMPLETED_QUIZZ)
                .child(quizeID)
                .getRef();

    }

    public void addAttemted(AttemptedQuiz attemptedQuiz, String quizeID, String teacher) {
        teacherRef.child(teacher).child(Constants.QUIZZ_CHILD).child(quizeID).child(Constants.AttemptedList).push().setValue(attemptedQuiz);
    }


    public DatabaseReference getStudentName(String studentUUID, String teacherUUID) {
        return teacherRef.child(teacherUUID).child(Constants.STUDENTS_KEY).child(studentUUID).child("name").getRef();
    }


    public void addNotification(String teacherUUID, NotifactionItem item) {
        teacherRef.child(teacherUUID).child(Constants.NOTIFICATION).push().setValue(item);
    }

    public DatabaseReference getNotifcationRef(String teacherUUID) {
        return teacherRef.child(teacherUUID).child(Constants.NOTIFICATION).getRef();
    }

    public DatabaseReference getTeacherQuizz(String teacherKey) {
        return teacherRef.child(teacherKey).child(Constants.QUIZZ_CHILD).getRef();
    }

    public DatabaseReference getStudentOfTeacherRef(String teacherKey) {
        return teacherRef.child(teacherKey).child(Constants.STUDENTS_KEY).getRef();
    }

    public void addAward(Award award) {
        String key = teacherRef.child(award.getTeacherUUID()).child(Constants.AWERD_KEY).child(award.getQuizzID()).push().getKey();
        teacherRef.child(award.getTeacherUUID()).child(Constants.AWERD_KEY).child(award.getQuizzID()).child(key).setValue(award);
    }

    public DatabaseReference getAwradRef(String teacherUUID) {
        return teacherRef.child(teacherUUID).child(Constants.AWERD_KEY);
    }

    public void addQuizzAwards(String teacherUUID, String quizzID, List<Award> awards) {
        teacherRef.child(teacherUUID).child(Constants.AWERD_KEY).child(quizzID).setValue(awards);
    }
}
