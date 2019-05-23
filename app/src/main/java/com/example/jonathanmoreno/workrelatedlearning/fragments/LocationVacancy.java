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
import com.example.jonathanmoreno.workrelatedlearning.adapter.LocationAdapter;
import com.example.jonathanmoreno.workrelatedlearning.adapter.VacancyAdapter;
import com.example.jonathanmoreno.workrelatedlearning.model.Location;
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
public class LocationVacancy extends Fragment {

    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private StringRequest request;


    public LocationVacancy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_location_vacancy, container, false);
        getActivity().setTitle("Locations");


        recyclerView=(RecyclerView) view.findViewById(R.id.listshow);
        recyclerView.setHasFixedSize(true);

        getLocations();


        return view;
    }
    private void getLocations(){
        final List<Location> locationList=new ArrayList<>();
        request=new StringRequest(Request.Method.GET, UrlConstants.URL_GET_LOCATIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject) jsonArray.get(i);
                        Location location=new Location();
                        location.setName(jsonObject.getString("name"));

                        locationList.add(location);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setuprecyclerview(locationList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageToast.show(getActivity(),"Failed to connect to database, please check if you have internet connection or contact admin");
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private void setuprecyclerview(List<Location> lstVacancies) {
        //
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new LocationAdapter(getActivity(),lstVacancies);
        recyclerView.setAdapter(adapter);


    }

}
