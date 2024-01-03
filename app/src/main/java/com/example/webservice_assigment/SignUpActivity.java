package com.example.webservice_assigment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    // define my elematns
    EditText name;
    EditText pass;
    Button log;
    CheckBox check;
    TextView sign;
    TextView error ;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //    Set Up all elemant in Activity
        setUp();



        //  Define sign clicker
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpActivity();
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean x = true  ;

                String userN = name.getText().toString().trim();
                String userP = pass.getText().toString().trim();
                if(userP.isEmpty() || userN.isEmpty()){

                    x = false ;
                }

                if(x){

//                  add new user to local storge
                    ArrayList<User> userList;



// Step 1: Retrieve the existing user list from SharedPreferences
                    String json = prefs.getString("register", "");
                    Type type = new TypeToken<ArrayList<User>>() {}.getType();
                    userList = new Gson().fromJson(json, type);

                    if (userList == null) {
                        // If the list is null, create a new one
                        userList = new ArrayList<>();
                    }

                    String u = name.getText().toString();
                    String p = pass.getText().toString();

// Step 2: Add the new user information to the array
                    User newUser = new User(u, p); // Replace with actual data
                    userList.add(newUser);

// Step 3: Save the updated array back to local storage
                    String updatedJson = new Gson().toJson(userList);
                    edit.putString("register", updatedJson);
                    edit.apply();

// Open The basic Activity
                    Intent intent = new Intent(SignUpActivity.this, BasicActivity.class);
                    startActivity(intent);


                }else{

                    error.setVisibility(View.VISIBLE);
                    error.setText("User Name or Password is Empty  ");
                    error.setTextColor(Color.RED);
                }


            }
        });



    }





    private void openSignUpActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setUp(){

        name = findViewById(R.id.txtN) ;
        pass = findViewById(R.id.txtP) ;
        log = findViewById(R.id.btnR) ;

        sign = findViewById(R.id.txtSign) ;
        prefs = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this) ;
        edit = prefs.edit() ;
        gson = new Gson() ;
        error = findViewById(R.id.txtError) ;

    }


}