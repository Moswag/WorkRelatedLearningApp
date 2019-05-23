package com.example.jonathanmoreno.workrelatedlearning;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathanmoreno.workrelatedlearning.util.Constants;
import com.example.jonathanmoreno.workrelatedlearning.util.FilePath;
import com.example.jonathanmoreno.workrelatedlearning.util.MessageToast;
import com.example.jonathanmoreno.workrelatedlearning.util.UrlConstants;
import com.example.jonathanmoreno.workrelatedlearning.util.UserSessionManager;
import com.example.jonathanmoreno.workrelatedlearning.util.VolleySingleton;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VacancyDetailed extends AppCompatActivity {

    TextView company, description,deadline,goback;
    Button applyButton,useUploadedCv;
    ImageButton chooseButton;

    ProgressDialog progressDialog;

    //Image request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    StringRequest request;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy_detailed);

        getSupportActionBar().setTitle("Vacancy Detail");

        //Requesting storage permission
        requestStoragePermission();

        progressDialog=new ProgressDialog(getApplicationContext());


        //company=findViewById(R.id.company);
        description=findViewById(R.id.description);
        deadline=findViewById(R.id.deadline);
        goback=findViewById(R.id.goBack);
        chooseButton=findViewById(R.id.chooseButton);

        final String id=getIntent().getExtras().getString("id");
        String companyEx=getIntent().getExtras().getString("company");
        String descriptionEx=getIntent().getExtras().getString("description");
        String deadlineEx=getIntent().getExtras().getString("deadline");

        userSessionManager=new UserSessionManager(getApplicationContext());

        HashMap<String,String> session=userSessionManager.getUserDetails();
        final String email=session.get(UserSessionManager.KEY_User);

        applyButton=(Button) findViewById(R.id.applyButton);
        useUploadedCv=findViewById(R.id.useUploadedCv);

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipart(email,id);
            }
        });



        request=new StringRequest(Request.Method.POST, UrlConstants.URL_GET_PROFILE_RESUME_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    String res=jsonObject.getString(Constants.RESPONSE);
                    if(res.equals(Constants.RESPONSE_SUCCESS)){
                        useUploadedCv.setVisibility(View.VISIBLE);
                    }
                    else{
                        MessageToast.show(getApplicationContext(),"Please setup your profile CV");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageToast.show(getApplicationContext(),"Failed to connect to database");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("email",email);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);









        description.setText(descriptionEx);
        deadline.setText("Deadline: "+deadlineEx);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VacancyDetailed.this,NavMain.class));
            }
        });


        useUploadedCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applySoftly(email,id);
            }
        });


    }
    public void uploadMultipart(String email, String id) {
        //getting name for the pdf


        //getting the actual path of the pdf
        String path = FilePath.getPath(getApplicationContext(), filePath);

        progressDialog.dismiss();

        if (path == null) {

            Toast.makeText(getApplicationContext(), "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(getApplicationContext(), uploadId, UrlConstants.UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("email", email)
                        .addParameter("vacancy_id", id)
                        //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    //handling the ima chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void applySoftly(final String email, final String id){
        //progressDialog.dismiss();
        request=new StringRequest(Request.Method.POST, UrlConstants.URL_APPLY_SOFTLY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    String res=jsonObject.getString(Constants.RESPONSE);
                    if(res.equals(Constants.RESPONSE_SUCCESS)){
                        MessageToast.show(getApplicationContext(),"Successfully applied for this post");
                    }
                    else{

                        MessageToast.show(getApplicationContext(),res);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MessageToast.show(getApplicationContext(),"Failed to connect to database");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("vacancy_id",id);
                params.put("email",email);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

}
