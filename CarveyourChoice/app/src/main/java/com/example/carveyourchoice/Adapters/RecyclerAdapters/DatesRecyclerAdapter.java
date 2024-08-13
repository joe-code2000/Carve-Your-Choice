package com.example.carveyourchoice.Adapters.RecyclerAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Activities.MealPlanner.MealPlannerActivity;
import com.example.carveyourchoice.Activities.MealPlanner.SelectMealPlannerActivity;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class DatesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private final Context context;
    private List<String> dates;
    private List<TextView> dateViews;
    private Activity activity;

    public DatesRecyclerAdapter(Context context, List<String> dates) {
        this.context = context;
        this.dates = dates;
        this.dateViews = new ArrayList<>();
    }

    public DatesRecyclerAdapter(Context context, List<String> dates, Activity activity) {
        this.context = context;
        this.dates = dates;
        this.dateViews = new ArrayList<>();
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_recycler_item,parent,false);
        return new ItemViewHolder(layoutView);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder view = (ItemViewHolder) holder;

        String date = dates.get(position);
        String day = Integer.toString(Utilities.getDateSplit(date,"-",0));
        String month =  Utilities.getMonth(Utilities.getDateSplit(date,"-",1));

        dateViews.add(view.date);
        view.date.setText(day+"\n"+month);

        if (Utilities.getTodayDateStr().compareTo(date) == 0){
            view.date.setTextColor(Color.rgb(255,90,120));
        }
        if (activity != null){
            switch (activity.getLocalClassName()){
                case "Activities.MealPlanner.MealPlannerActivity":
                    view.itemView.setOnClickListener(v->{
                        dateViews.stream().filter(x->x != view.date).forEach(n->{
                            n.setTextColor(Color.WHITE);
                        });
                        view.date.setTextColor(Color.rgb(255,90,120));
                        MealPlannerActivity.setMealPlanner(date);
                    });
                    break;
                case "Activities.MealPlanner.SelectMealPlannerActivity":
                    view.itemView.setOnClickListener(v->{
                        dateViews.stream().filter(x->x != view.date).forEach(n->{
                            n.setTextColor(Color.WHITE);
                        });
                        view.date.setTextColor(Color.rgb(255,90,120));
                        SelectMealPlannerActivity.setCurrentDate(date);
                    });
                    break;
            }
        }



    }


    @Override
    public int getItemCount() {
        return dates.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView date;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
