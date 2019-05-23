package com.example.jonathanmoreno.workrelatedlearning.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jonathanmoreno.workrelatedlearning.R;
import com.example.jonathanmoreno.workrelatedlearning.activities.ViewVacancyByCompany;
import com.example.jonathanmoreno.workrelatedlearning.activities.ViewVacancyByLocation;
import com.example.jonathanmoreno.workrelatedlearning.model.Company;
import com.example.jonathanmoreno.workrelatedlearning.model.Location;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.HolderView>{

    private Context context;
    private List<Company> companyList;

    public CompanyAdapter(Context context, List<Company> companyList) {
        this.context = context;
        this.companyList = companyList;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.company,viewGroup,false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyAdapter.HolderView holderView, int position) {

        holderView.company.setText(companyList.get(position).getName());
        holderView.location.setText(companyList.get(position).getLocation());


        holderView.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewVacancyByCompany.class);
                intent.putExtra("company", holderView.company.getText());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {

        TextView company,location;

        public HolderView(@NonNull View itemView) {
            super(itemView);

            company=(TextView) itemView.findViewById(R.id.company);
            location=(TextView) itemView.findViewById(R.id.location);

        }
    }
}
