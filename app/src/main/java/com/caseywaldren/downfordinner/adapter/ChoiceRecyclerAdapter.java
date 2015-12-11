package com.caseywaldren.downfordinner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.caseywaldren.downfordinner.R;
import com.caseywaldren.downfordinner.data.Choice;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by William on 12/11/2015.
 */
public class ChoiceRecyclerAdapter extends RecyclerView.Adapter<ChoiceRecyclerAdapter.ViewHolder>{

    public static boolean isRestaurants = true;
    private List<Choice> choices;
    private Context context;

    public ChoiceRecyclerAdapter(Context context, boolean isRestaurants) {
        this.context = context;
        this.isRestaurants = isRestaurants;
        this.choices  = new ArrayList<Choice>();

    }

    @Override
    public ChoiceRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View choiceRow = LayoutInflater.from(
                context).inflate(R.layout.choice_row, parent, false);
        return new ViewHolder(choiceRow);
    }

    @Override
    public void onBindViewHolder(final ChoiceRecyclerAdapter.ViewHolder holder, final int position) {

        final Choice choice = choices.get(position);


        //get restaraunt name
        if (isRestaurants) {
            holder.tvTitle.setText(choice.getTitle());
        } else {
            holder.tvTitle.setText(choice.getTime());
        }

        holder.btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update vote count on parse
                holder.tvVoted.setVisibility(View.VISIBLE);
                holder.btnVote.setVisibility(View.GONE);
            }
        });

    }

    /*
    private void launchWeatherDetailsActivity(Choice choice) {
        Intent intentOpenWeather = new Intent();
        intentOpenWeather.setClass(context,
                WeatherInfoActivity.class);
        intentOpenWeather.putExtra(WeatherInfoActivity.CITY_OBJECT, city);
        context.startActivity(intentOpenWeather);
    }
    */

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public void updateAllChoices() {
        if(getItemCount() != 0) {

        }
    }

    public void removeChoice(int index) {
        choices.remove(index);
    }


    public void addInitialChoices(List<ParseObject> objects) {
        for(int i = 0; i < objects.size(); i++) {
            choices.add(new Choice(objects.get(i)));
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvVoted;
        private final Button btnVote;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvVoted = (TextView) itemView.findViewById(R.id.tvVoted);
            btnVote = (Button) itemView.findViewById(R.id.btnVote);
        }

    }


}
