package com.example.webservice_assigment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    // define my elematns
    EditText name;
    EditText pass;
    Button log;
    CheckBox check;
    TextView sign;
    TextView error;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //    Set Up all elemant in Activity
           setUp();
//
//        // Set the "Sign Up" text to be pink and handle click event
        setPinkText(sign, "Don't have an account? Sign Up");
//
//           //  Define sign clicker
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpActivity();
            }
        });
//

        logProcess();

//       define The process of LogIn
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean x = true;

                String nam = name.getText().toString().trim();
                String pas = pass.getText().toString().trim();

//                if (checkRemember() && !nam.isEmpty() && !pas.isEmpty()) {
//
//                    openApp();
//                }

                if (nam.isEmpty() || pas.isEmpty()) {

                    x = false;
                }

                if (x && logProcess2(nam, pas)) {

                    if (check.isChecked() && !checkRemember())
                    {

                        User usr = new User(nam, pas);
                        String userJson = gson.toJson(usr);

                        // Save the JSON string to local storage
                        edit.putString("log", userJson);
                        // Apply the changes
                        edit.apply();

                    }
                    else
                    {

                        // Remove the log information
                        edit.remove("log");

                        edit.apply();
                    }

                    openApp();

                } else {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Wrong Account check User name & password");
                    error.setTextColor(Color.RED);
                }

            }


        });

    }

//         implemant Metods Section .

    private void openApp() {
        Intent intent = new Intent(this, BasicActivity.class);
        startActivity(intent);
    }

    //    Method to open SignUpActivity
    private void openSignUpActivity() {

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    // Method to set specific text color in a TextView
    private void setPinkText(TextView textView, String text) {
        int pinkColor = Color.parseColor("#FF00FF"); // Pink color
        SpannableString spannableString = new SpannableString(text);

        // Set pink color only for the "Sign Up" part
        int startIndex = text.indexOf("Sign Up");
        int endIndex = startIndex + "Sign Up".length();
        spannableString.setSpan(new ForegroundColorSpan(pinkColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the styled text to the TextView
        textView.setText(spannableString);
    }


//   SetUp elemant method .

    public void setUp() {

        name = findViewById(R.id.txtN);
        pass = findViewById(R.id.txtP);
        log = findViewById(R.id.btnR);
        check = findViewById(R.id.btnRem);
        sign = findViewById(R.id.txtSign);
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        edit = prefs.edit();
        gson = new Gson();
        error = findViewById(R.id.txtError);

    }

//   check if user Remeber or not

    public Boolean checkRemember() {

        if (prefs.getBoolean("log", false)) {

            return true;
        } else {
            return false;
        }

    }

//    check Register

    public boolean checkRig() {

        if (prefs.getBoolean("register", false)) {

            return true;

        } else {
            return false;
        }
    }

// process of loging

    public void logProcess() {

        if (checkRemember()) {

            String json = prefs.getString("log", "");
            User us = gson.fromJson(json, User.class);

//                set up the input
            name.setText(us.getName());
            pass.setText(us.getPass());
            check.setChecked(true);

        } else {


        }
    }

    public boolean logProcess2(String name, String pass) {

       if(prefs.getBoolean("register", false)){

           String json = prefs.getString("register", "");

           Type type = new TypeToken<ArrayList<User>>() {
           }.getType();
           ArrayList<User> userList = new Gson().fromJson(json, type);

           // Step 2: Check if the entered username and password match any registered user
           for (User user : userList) {
               if (name.equals(user.getName()) && pass.equals(user.getPass())) {
                   // Login successful
                   return true;
               }
           }

           // Login failed
           return false;
       }else{
           return false ;
       }



    }

}