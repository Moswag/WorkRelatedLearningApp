package com.example.jonathanmoreno.workrelatedlearning;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathanmoreno.workrelatedlearning.model.Student;
import com.example.jonathanmoreno.workrelatedlearning.util.MessageToast;
import com.example.jonathanmoreno.workrelatedlearning.util.UrlConstants;
import com.example.jonathanmoreno.workrelatedlearning.util.UserSessionManager;
import com.example.jonathanmoreno.workrelatedlearning.util.VariableConstants;
import com.example.jonathanmoreno.workrelatedlearning.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity{

    TextView createAccTextView;
    EditText email, password;
    Button loginbutton;
    StringRequest request;
    UserSessionManager sessionManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager=new UserSessionManager(LoginActivity.this);

        if(sessionManager.isUserLoggedIn()){
            MessageToast.show(LoginActivity.this,"Welcome back");
            startActivity(new Intent(LoginActivity.this,NavMain.class));
        }
        else{
            MessageToast.show(LoginActivity.this,"Please login to proceed");
        }

        createAccTextView=findViewById(R.id.createAccTextView);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        loginbutton=findViewById(R.id.loginbutton);


        createAccTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student student=new Student();
                student.setEmail(email.getText().toString().trim());
                student.setPassword(password.getText().toString().trim());

                validateFields(student);

            }
        });



    }

    private void validateFields(Student student){
        if(TextUtils.isEmpty(student.getEmail())){
            email.setFocusable(true);
            email.setError("Email is required");
        }
        else if(TextUtils.isEmpty(student.getPassword())){
            password.setFocusable(true);
            password.setError("Password is required");
        }
        else{

            authenticateUser(student);
        }
    }

    private void clearFields(){
        email.setText("");
        password.setText("");
    }


    private void authenticateUser(final Student student){
        request=new StringRequest(Request.Method.POST, UrlConstants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String res=jsonObject.getString(VariableConstants.RESPONSE);
                    if(res.equals(VariableConstants.RESPONSE_SUCCESS)){
                        clearFields();
                        sessionManager.createUserLoginSession(student.getEmail(),student.getPassword());
                        MessageToast.show(LoginActivity.this,"Welcome to work related learning seeking platform");
                        startActivity(new Intent(LoginActivity.this,NavMain.class));
                    }
                    else{
                        MessageToast.show(LoginActivity.this,res);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageToast.show(LoginActivity.this,"Failed to connect to database");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("email",student.getEmail());
                params.put("password",student.getPassword());

                return params;
            }
        };
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);
    }


}
