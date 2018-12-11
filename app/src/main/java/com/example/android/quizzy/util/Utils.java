package com.example.android.quizzy.util;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brkckr.circularprogressbar.CircularProgressBar;
import com.example.android.quizzy.R;

import az.plainpie.PieView;

public class Utils {

    private final static String TAG = "Utils";


    /*public static AlertDialog getDialog(Context context, String message, String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_dialoge_title , null) ;
        TextView textView = view.findViewById(R.id.tvDialogeText);
        textView.setText(message);
        return new AlertDialog.Builder(context).setView(view).create();
    }
    */

    /**
     * checks if the device is connected to the internet or not
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * checks if email is in valid format
     * Got from "https://codereview.stackexchange.com/questions/33546/simple-code-to-check-format-of-user-inputted-email-address"
     */
    public static boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9\\.]+@[a-zA-Z0-9\\-\\_\\.]+\\.[a-zA-Z0-9]{3}");
    }

    /**
     * checks if password is valid(6 characters or more)
     */
    public static boolean isValidPassword(String password) {
        return password.length() >= Constants.MIN_PASSWORD;
    }

    /**
     * Checks if name is valid(consists of only letters)
     */
    public static boolean isValidName(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (!((name.charAt(i) >= Constants.A_ASCICODE && name.charAt(i) <= Constants.Z_ASCICODE) || ((name.charAt(i) >= Constants.a_ASCICODE && name.charAt(i) <= Constants.z_ASCICODE))))
                return false;
        }
        return true;
    }

    public static void hideInputKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * Checks if telephoneNumber is valid(consists of only numbers)
     */
    public static boolean isValidTelephoneNumber(String telephoneNumber) {
        for (int i = 0; i < telephoneNumber.length(); i++) {
            if (!((telephoneNumber.charAt(i) >= Constants.ZERO_ASCICODE && telephoneNumber.charAt(i) <= Constants.NINE_ASCICODE)))
                return false;
        }
        return true;
    }

    public static boolean checkEmptyInputs(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                Log.d(TAG, "empty input");
                return false;
            }
        }
        return true;
    }


    public static AlertDialog makeAlert(Context context, String title, int percentage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_quiz_grade, null);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(title);
        TextView score = view.findViewById(R.id.score);
        score.setText(String.valueOf(percentage));
        CircularProgressBar bar = view.findViewById(R.id.circularProgressBar);
        bar.setProgressValue(percentage);

        return builder.setView(view).create();

    }


    public static String timeToString(long durationInMillis) {
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);

    }

}
