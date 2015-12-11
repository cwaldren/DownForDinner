package com.caseywaldren.downfordinner.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.caseywaldren.downfordinner.AcceptedActivity;
import com.caseywaldren.downfordinner.ParseUtils;
import com.caseywaldren.downfordinner.R;
import com.caseywaldren.downfordinner.TimeActivity;
import com.caseywaldren.downfordinner.data.Choice;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by William on 12/11/2015.
 */
public class ChoiceRecyclerAdapter extends RecyclerView.Adapter<ChoiceRecyclerAdapter.ViewHolder> {

    public boolean isRestaurants = true;
    public String objectTag;
    public String objectCountTag;
    public ParseObject status;
    private List<Choice> choices;
    private Context context;

    public ChoiceRecyclerAdapter(Context context, boolean isRestaurants) {
        this.context = context;
        this.isRestaurants = isRestaurants;
        this.choices = new ArrayList<Choice>();

        if (isRestaurants) {
            objectTag = "restaurant";
            objectCountTag = "restaurantVotes";
        } else {
            objectTag = "time";
            objectCountTag = "timeVotes";
        }

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

                fetchLatestVoteCountAndAddOneVote(choices.get(position).getObject(), position);

                holder.tvVoted.setVisibility(View.VISIBLE);
                holder.btnVote.setVisibility(View.GONE);

            }
        });

    }

    private void fetchLatestVoteCountAndAddOneVote(final ParseObject object, final int position) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Suggestions");
        query.getInBackground(object.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject newObject, ParseException e) {
                if (e != null) {
                    Log.e("PARSE_UPDATE", "Failed to fetch updated vote count before voting");
                } else {
                    choices.get(position).setObject(newObject);
                    int voteCount = newObject.getInt(objectCountTag) + 1;
                    if (voteCount == status.getInt("threshold")) {
                        status.put(objectTag,
                                choices.get(position).getObject().getString(objectTag));
                        status.saveInBackground();
                        if (isRestaurants) {
                            notifyEveryoneToAdvanceToTime();
                            advanceStateToTime();
                        } else {
                            notifyEveryoneToAdvanceToRestaurant();
                            advanceStateToAccepted();
                        }
                    } else {
                        choices.get(position).getObject().put(objectCountTag, voteCount);
                        newObject.saveInBackground();
                        String title = isRestaurants ? "Update Vote Count" : "Update Time Count";
                        String action = isRestaurants ? ParseUtils.INTENT_UPDATE_VOTE_COUNT : ParseUtils.INTENT_UPDATE_TIME_COUNT;
                        try {
                            ParseUtils.sendParsePush(ParseUtils.CHANNEL_DINNER_UPDATES, title, title, action);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

                }
            }
        });
    }

    private void notifyEveryoneToAdvanceToRestaurant() {
        try {
            ParseUtils.sendParsePush(ParseUtils.CHANNEL_DINNER_UPDATES, "Plans Created", "Plans have been accepted.", ParseUtils.INTENT_PLANS_CREATED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notifyEveryoneToAdvanceToTime() {
        try {
            ParseUtils.sendParsePush(ParseUtils.CHANNEL_DINNER_UPDATES, "Choose Time", "Its time to choose a time", ParseUtils.INTENT_BEGIN_CHOOSE_TIME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void advanceStateToTime() {

        //update other users

        Intent launchTimeChoiceActivity = new Intent(context, TimeActivity.class);
        context.startActivity(launchTimeChoiceActivity);
        ((Activity) context).finish();
    }

    public void advanceStateToAccepted() {
        Intent launchAcceptedActivity = new Intent(context, AcceptedActivity.class);
        context.startActivity(launchAcceptedActivity);
        ((Activity) context).finish();
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public void addInitialChoices(ParseObject status, List<ParseObject> objects) {
        this.status = status;
        choices = new ArrayList<Choice>();
        for (int i = 0; i < objects.size(); i++) {
            choices.add(new Choice(objects.get(i)));
        }
        notifyDataSetChanged();
    }

    public void updateChoices(List<ParseObject> objects) {
        choices = new ArrayList<Choice>();
        for (int i = 0; i < objects.size(); i++) {
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
