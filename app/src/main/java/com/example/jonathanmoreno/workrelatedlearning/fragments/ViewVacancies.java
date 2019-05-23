package com.example.jonathanmoreno.workrelatedlearning.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class ViewVacancies extends Fragment {

    private RecyclerView recyclerView;
    private VacancyAdapter adapter;
    private StringRequest request;


    public ViewVacancies() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_vacancies, container, false);
        getActivity().setTitle("Vacancies");

        recyclerView=(RecyclerView) view.findViewById(R.id.listshow);
        recyclerView.setHasFixedSize(true);

        getVacancies();
        return view;
    }
    private void getVacancies(){
        final List<Vacancy> availableVacancies=new ArrayList<>();
        request=new StringRequest(Request.Method.GET, UrlConstants.URL_GET_VACANCIES, new Response.Listener<String>() {
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
                MessageToast.show(getActivity(),"Failed to connect to database, please check if you hve internet connection or contact admin");
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private void setuprecyclerview(List<Vacancy> lstVacancies) {
        //
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new VacancyAdapter(getActivity(),lstVacancies);
        recyclerView.setAdapter(adapter);


    }
}
