package com.example.webservice_assigment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class BasicActivity extends AppCompatActivity {

    //     define varible
    TextView convertFromDropdownTextView , convertToDropdownTextView , conversionRateText ;
    EditText amountToConvert ;
    ArrayList<String> arrayList ;
    Dialog fromDialog ;
    Dialog toDialog ;
    Button convertButton ;
    String convertFromValue , convertToValue , conversionValue ;
    String[] country ={
            "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD",
            "MXN", "SGD", "HKD", "NOK", "KRW", "TRY", "INR", "RUB", "BRL", "ZAR",
            "ILS",  // Israeli Shekel
            "JOD"   // Jordanian Dinar
            // Add more currency codes as needed
    }; ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);


//         setUp all elemant

        convertFromDropdownTextView  = findViewById(R.id.convert_from_dropdown_menu) ;
        convertToDropdownTextView = findViewById(R.id.convert_to_dropdown_menu) ;
        convertButton = findViewById(R.id.convertsionButton);
        conversionRateText = findViewById(R.id.conversionRateText) ;

        arrayList = new ArrayList<>() ;
        for(String i :country){
            arrayList.add(i) ;
        }

        convertFromDropdownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fromDialog = new Dialog(BasicActivity.this) ;
                fromDialog.setContentView(R.layout.from_spinner);
                fromDialog.getWindow().setLayout(650 ,800);
                fromDialog.show();


                EditText editText = fromDialog.findViewById(R.id.edit_text) ;
                ListView listView = fromDialog.findViewById(R.id.list_view) ;

                ArrayAdapter<String > adapter = new ArrayAdapter<>(BasicActivity.this , android.R.layout.simple_list_item_1 ,arrayList) ;
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        adapter.getFilter().filter(charSequence) ;
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        convertFromDropdownTextView.setText(adapter.getItem(i));
                        fromDialog.dismiss();
                        convertFromValue = adapter.getItem(i) ;

                    }
                });
            }
        });


        convertToDropdownTextView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toDialog = new Dialog(BasicActivity.this) ;
                toDialog.setContentView(R.layout.to_spinner);
                toDialog.getWindow().setLayout(650 ,800);
                toDialog.show();

                EditText editText = toDialog.findViewById(R.id.edit_text) ;
                ListView listView = toDialog.findViewById(R.id.list_view) ;

                ArrayAdapter<String> adapter = new ArrayAdapter<>(BasicActivity.this , android.R.layout.simple_list_item_1 ,  arrayList) ;
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    //
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        convertToDropdownTextView.setText(adapter.getItem(i));
                        toDialog.dismiss();
                        convertToValue = adapter.getItem(i) ;

                    }
                });
            }
        });
//
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                try {


                    Double amountToConvert = Double.valueOf(BasicActivity.this.amountToConvert.getText().toString()) ;
                    getConversionRate(convertFromValue ,convertToValue , amountToConvert) ;

                }catch (Exception e){

                }
            }
        });
    }

    public String getConversionRate(String convertFrom ,String convertTo , Double amoutToConvert){

        RequestQueue queue  = Volley.newRequestQueue(this) ;
        String url = "http://free.currcony.com/api/v7/convert?q="+convertFrom +" "+convertTo+"&compact=ultra&apikey=22e91ab924eb2aa6f9a4" ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    Double convertionRateValve = round(((Double) jsonObject.get(convertFrom + " " + convertTo)), 2);
                    conversionValue = "" + round((convertionRateValve * amoutToConvert), 2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
                         @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        queue.add(stringRequest) ;

        return conversionValue ;
    }


    public static double round(double value , int places){

        if(places<0) throw new IllegalArgumentException() ;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places , RoundingMode.HALF_UP) ;
        return bd.doubleValue() ;
    }

}