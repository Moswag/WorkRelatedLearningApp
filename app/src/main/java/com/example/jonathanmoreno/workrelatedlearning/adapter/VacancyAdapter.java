package com.example.jonathanmoreno.workrelatedlearning.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathanmoreno.workrelatedlearning.R;
import com.example.jonathanmoreno.workrelatedlearning.VacancyDetailed;
import com.example.jonathanmoreno.workrelatedlearning.model.Vacancy;
import com.example.jonathanmoreno.workrelatedlearning.util.Constants;
import com.example.jonathanmoreno.workrelatedlearning.util.MessageToast;
import com.example.jonathanmoreno.workrelatedlearning.util.UrlConstants;
import com.example.jonathanmoreno.workrelatedlearning.util.UserSessionManager;
import com.example.jonathanmoreno.workrelatedlearning.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VacancyAdapter  extends RecyclerView.Adapter<VacancyAdapter.HolderView>{

    private Context context;
    private List<Vacancy> vacancyList;
    StringRequest request;
    UserSessionManager userSessionManager;
    boolean didYouApply;

    public VacancyAdapter(Context context, List<Vacancy> vacancyList) {
        this.context = context;
        this.vacancyList = vacancyList;

    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vacancy_card,viewGroup,false);
        userSessionManager=new UserSessionManager(context);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VacancyAdapter.HolderView holderView, final int position) {


        HashMap<String,String> session=userSessionManager.getUserDetails();
        final String email=session.get(UserSessionManager.KEY_User);
        final String id=vacancyList.get(position).getId();


        holderView.company.setText(vacancyList.get(position).getCompany());
        holderView.description.setText(vacancyList.get(position).getDescription());
        holderView.deadline.setText(vacancyList.get(position).getDeadline());


        request=new StringRequest(Request.Method.POST, UrlConstants.URL_DID_YOU_APPLY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String res=jsonObject.getString(Constants.RESPONSE);
                    if(res.equals(Constants.RESPONSE_SUCCESS)){
                        didYouApply=true;
                    }
                    else{
                        didYouApply=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("email",email);
                params.put("vacancy_id",id);
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);


        if(didYouApply){
            MessageToast.show(context,"You already applied for this vacancy, please select a different one");
        }
        else {
            holderView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, VacancyDetailed.class);
                    intent.putExtra("id", vacancyList.get(position).getId());
                    intent.putExtra("deadline", holderView.deadline.getText().toString());
                    intent.putExtra("company", holderView.company.getText().toString());
                    intent.putExtra("description", holderView.description.getText().toString());
                    intent.putExtra("deadline", holderView.deadline.getText().toString());
                    context.startActivity(intent);

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return vacancyList.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {

        TextView company,description,deadline;

        public HolderView(@NonNull View itemView) {
            super(itemView);

            company=(TextView) itemView.findViewById(R.id.company);
            description=(TextView) itemView.findViewById(R.id.description);
            deadline=(TextView) itemView.findViewById(R.id.deadline);

        }
    }
}
