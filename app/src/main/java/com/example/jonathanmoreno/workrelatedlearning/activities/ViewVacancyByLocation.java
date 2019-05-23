package com.example.jonathanmoreno.workrelatedlearning.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathanmoreno.workrelatedlearning.R;
import com.example.jonathanmoreno.workrelatedlearning.adapter.VacancyAdapter;
import com.example.jonathanmoreno.workrelatedlearning.model.Vacancy;
import com.example.jonathanmoreno.workrelatedlearning.util.MessageToast;
import com.example.jonathanmoreno.workrelatedlearning.util.UrlConstants;
import com.example.jonathanmoreno.workrelatedlearning.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewVacancyByLocation extends AppCompatActivity {
    
    
    private RecyclerView recyclerView;
    private VacancyAdapter adapter;
    private StringRequest request;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vacancy_by_location);

        location=getIntent().getExtras().getString("location");
        getSupportActionBar().setTitle(location+" Vacancies");

        recyclerView=(RecyclerView) findViewById(R.id.listshow);
        recyclerView.setHasFixedSize(true);



        getVacancies();
    }


    private void getVacancies(){
        final List<Vacancy> availableVacancies=new ArrayList<>();
        request=new StringRequest(Request.Method.POST, UrlConstants.URL_GET_VACANCY_BY_LOCATIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject) jsonArray.get(i);
                        Vacancy vacancy=new Vacancy();
                        vacancy.setId(jsonObject.getString("id"));
                        vacancy.setCompany(jsonObject.getString("company"));
                        vacancy.setCategory(jsonObject.getString("category"));
                        vacancy.setDescription(jsonObject.getString("description"));
                        vacancy.setDeadline(jsonObject.getString("deadline"));

                        availableVacancies.add(vacancy);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setuprecyclerview(availableVacancies);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageToast.show(ViewVacancyByLocation.this,"Failed to connect to database, please check if you hve internet connection or contact admin");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("location",location);
                return params;
            }
        };
        VolleySingleton.getInstance(ViewVacancyByLocation.this).addToRequestQueue(request);

    }

    private void setuprecyclerview(List<Vacancy> lstVacancies) {
        //
        LinearLayoutManager layoutManager=new LinearLayoutManager(ViewVacancyByLocation.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new VacancyAdapter(ViewVacancyByLocation.this,lstVacancies);
        recyclerView.setAdapter(adapter);


    }
}
