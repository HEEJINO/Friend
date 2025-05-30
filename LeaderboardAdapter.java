package com.example.swith.Friend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.swith.R;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private List<User> leaderboard;

    public LeaderboardAdapter(List<User> leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public LeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rank, parent, false);
        return new LeaderboardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LeaderboardViewHolder holder, int position) {
        User user = leaderboard.get(position);
        holder.rankText.setText(String.valueOf(position + 1));
        holder.nameText.setText(user.getName());
        holder.studyTimeText.setText(String.format("Study Time: %d mins", user.getStudyTime() / 60));
        holder.pointText.setText(String.format("Points: %d", user.getPoints()));
    }

    @Override
    public int getItemCount() {
        return leaderboard.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView rankText, nameText, studyTimeText, pointText;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
            rankText = itemView.findViewById(R.id.rankText);
            nameText = itemView.findViewById(R.id.nameText);
            studyTimeText = itemView.findViewById(R.id.studyTimeText);
            pointText = itemView.findViewById(R.id.pointText);
        }
    }
}
