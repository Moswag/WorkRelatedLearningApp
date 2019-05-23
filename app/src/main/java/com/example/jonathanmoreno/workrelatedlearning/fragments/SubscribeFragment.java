package com.example.jonathanmoreno.workrelatedlearning.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathanmoreno.workrelatedlearning.R;
import com.example.jonathanmoreno.workrelatedlearning.util.Constants;
import com.example.jonathanmoreno.workrelatedlearning.util.MessageToast;
import com.example.jonathanmoreno.workrelatedlearning.util.UrlConstants;
import com.example.jonathanmoreno.workrelatedlearning.util.UserSessionManager;
import com.example.jonathanmoreno.workrelatedlearning.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribeFragment extends Fragment {

    Spinner category;
    Button submit;
    StringRequest request;
    ProgressDialog progressDialog;
    UserSessionManager userSessionManager;


    public SubscribeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_subscribe, container, false);
        getActivity().setTitle("Subscribe");

        progressDialog=new ProgressDialog(getActivity());
        userSessionManager=new UserSessionManager(getActivity());
        progressDialog.setTitle("Subscribing...");


        HashMap<String,String> session=userSessionManager.getUserDetails();
        final String email=session.get(UserSessionManager.KEY_User);

        category=view.findViewById(R.id.category);
        submit=view.findViewById(R.id.submit);

        List<String> vacancyCategory=new ArrayList<String>();
        vacancyCategory.add("Technical");
        vacancyCategory.add("Financial");
        vacancyCategory.add("HR");
        vacancyCategory.add("Building");
        vacancyCategory.add("Science");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, vacancyCategory);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(dataAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                subscribeJSON(String.valueOf(category.getSelectedItem()),email);

            }
        });



        return view;
    }


    private void subscribeJSON(final String category, final String email){
        request=new StringRequest(Request.Method.POST, UrlConstants.URL_SUBSCRIBE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String res=jsonObject.getString(Constants.RESPONSE);

                    if(res.equals(Constants.RESPONSE_SUCCESS)){
                        MessageToast.show(getActivity(),"You successfully subscribed to "+category);
                    }
                    else{
                        MessageToast.show(getActivity(),res);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageToast.show(getActivity(),"Failed to connect to database");
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("category",category);
                params.put("email",email);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

}
