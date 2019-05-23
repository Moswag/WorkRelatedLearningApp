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
import com.example.jonathanmoreno.workrelatedlearning.VacancyDetailed;
import com.example.jonathanmoreno.workrelatedlearning.activities.ViewVacancyByLocation;
import com.example.jonathanmoreno.workrelatedlearning.model.Location;
import com.example.jonathanmoreno.workrelatedlearning.model.Vacancy;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.HolderView>{

    private Context context;
    private List<Location> locationList;

    public LocationAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_vacancy_card,viewGroup,false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocationAdapter.HolderView holderView, int position) {

        holderView.name.setText(locationList.get(position).getName());

        holderView.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewVacancyByLocation.class);
                intent.putExtra("location", holderView.name.getText());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {

        TextView name;

        public HolderView(@NonNull View itemView) {
            super(itemView);

            name=(TextView) itemView.findViewById(R.id.name);

        }
    }
}
