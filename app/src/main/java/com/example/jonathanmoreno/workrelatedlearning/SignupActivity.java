package com.example.jonathanmoreno.workrelatedlearning;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathanmoreno.workrelatedlearning.model.Student;
import com.example.jonathanmoreno.workrelatedlearning.util.MessageToast;
import com.example.jonathanmoreno.workrelatedlearning.util.UrlConstants;
import com.example.jonathanmoreno.workrelatedlearning.util.VariableConstants;
import com.example.jonathanmoreno.workrelatedlearning.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity{


    TextView loginReturnTextView;
    EditText name,email,phonenumber,program,password,confirm_password;
    Button signUpButton;
    StringRequest request;

    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        loginReturnTextView=findViewById(R.id.loginReturnTextView);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phonenumber=findViewById(R.id.phonenumber);
        program=findViewById(R.id.program);
        password=findViewById(R.id.password);
        confirm_password=findViewById(R.id.confirm_password);
        signUpButton=findViewById(R.id.signUpButton);


        progressDialog=new ProgressDialog(getApplicationContext());






        loginReturnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student student=new Student();
                student.setName(name.getText().toString().trim());
                student.setEmail(email.getText().toString().trim());
                student.setPhonenumber(phonenumber.getText().toString().trim());
                student.setProgram(program.getText().toString());
                student.setPassword(password.getText().toString().trim());
                String cPassword=confirm_password.getText().toString().trim();

                validateFields(student,cPassword);

            }
        });





    }

    private void validateFields(Student student, String confirmPassword){
        if(TextUtils.isEmpty(student.getName())){
            name.setFocusable(true);
            name.setError("Name is required");
        }
        else if(TextUtils.isEmpty(student.getEmail())){
            email.setFocusable(true);
            email.setError("Email is required");
        }

        else if(TextUtils.isEmpty(student.getPhonenumber())){
            phonenumber.setFocusable(true);
            phonenumber.setError("Phonenumber is required");
        }
        else if(TextUtils.isEmpty(student.getProgram())){
            program.setFocusable(true);
            program.setError("Program is required");
        }
        else if(TextUtils.isEmpty(student.getPassword())){
            password.setFocusable(true);
            password.setError("Password is required");
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            confirm_password.setFocusable(true);
            confirm_password.setError("Confirm password required");
        }
        else if(!student.getPassword().equals(confirmPassword)){
            MessageToast.show(SignupActivity.this,"Passwords do not match, please confirm");
        }
        else{

            jsonRegister(student);
        }
    }

    private void clearFields(){
        name.setText("");
        email.setText("");
        phonenumber.setText("");
        program.setText("");
        password.setText("");
        confirm_password.setText("");
    }



    private void jsonRegister(final Student student){
        request=new StringRequest(Request.Method.POST, UrlConstants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String res=jsonObject.getString(VariableConstants.RESPONSE);
                    if(res.equals(VariableConstants.RESPONSE_SUCCESS)){
                        clearFields();
                        MessageToast.show(SignupActivity.this,"You have been successfully registered");
                        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    }
                    else{
                        MessageToast.show(SignupActivity.this,res);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageToast.show(SignupActivity.this,"Failed to connect to database");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",student.getName());
                params.put("email",student.getEmail());
                params.put("phonenumber",student.getPhonenumber());
                params.put("program",student.getProgram());
                params.put("password",student.getPassword());

                return params;
            }
        };
        VolleySingleton.getInstance(SignupActivity.this).addToRequestQueue(request);

    }



}
