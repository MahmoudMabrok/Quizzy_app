package com.example.android.quizzy.util;

public interface Constants {

    int MIN_PASSWORD = 6;
    int A_ASCICODE = 65;
    int Z_ASCICODE = 90;
    int a_ASCICODE = 97;
    int z_ASCICODE = 122;
    int ZERO_ASCICODE = 48;
    int NINE_ASCICODE = 57;

    String USERS_KEY = "users";
    String TEACHERS_KEY = "teachers";
    String STUDENTS_KEY = "students";

    public static final String ID_KEY = "id";
    String INPUTS_KEY = "inputs";
    String EMAIL_KEY = "email";
    String PASSWORD_KEY = "password";
    String FIRST_NAME_KEY = "first_name";
    String LAST_NAME_KEY = "last_name";
    String CITY_KEY = "city";
    String TELEPHONE_NUMBER_KEY = "telephone_number";
    String SUBJECT_KEY = "subject";
    String ACADEMIC_YEAR_KEY = "academic_year";
    String TEACHER_TELEPHONE_NUMBER_KEY = "teacher_telephone_number";


    String STUDENT_NAME_KEY = "student_name";


    String QUIZZ_CHILD = "quiz";
    String QUIZZ_QUESTION_LIST = "questionList";

    String Question_answer_list = "answerList";
    String question = "question";
    String answerList = "answerList";
    String correctAnswer = "correctAnswer";
    String weight = "weight";

    String COMPLETED_QUIZZ = "completed";

    int Excellent = 4;
    int VGOOD = 3;
    int GOOD = 2;
    int ACCEPT = 1;
    int FAILED = 0;
    String[] gradesAsString = new String[]{"FAILED", "ACCEPT", "GOOD", "VGOOD", "Excellent"};

    String NOTIFICATION = "notification";
    String AttemptedList = "AttemptedList";
    String STUDENT_NAME = "STUDENT_NAME";
    String QUIZZ_NAME = "name";
    String STUDENT_UUID = "student_uuid";
    String STUDENT_Teacher_uuid = "teacher_uuid";
    String Quizz_id = "id";
    String TEACHER_NAME = "teacherName";

    public static final int RC_SIGN_IN = 1;
    String NO_ACCOUNT = "No account found";
    String AWERD_KEY = "Awards";
    String COMPLETED_Sate = "sate";
}
