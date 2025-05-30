package com.example.swith.Friend;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swith.R;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private List<User> friends;

    public FriendAdapter(List<User> friends) {
        this.friends = friends;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, pointText, rankText, studyTime;
        View statusIndicator;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            rankText = itemView.findViewById(R.id.rankText);
            nameText = itemView.findViewById(R.id.nameText);
            pointText = itemView.findViewById(R.id.pointText);
            studyTime = itemView.findViewById(R.id.studyTimeText);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rank, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        User friend = friends.get(position);
        holder.rankText.setText((position + 1) + "위");
        holder.nameText.setText(friend.name); // name || nickname|| id 중 결정해서 사용
        //공부시간
        int studyTimeSecond = friend.getStudyTime();
        int studyTimeMinutes = studyTimeSecond / 60;
        holder.studyTime.setText(studyTimeMinutes + "분");

        holder.pointText.setText(friend.points +"xp");

        // 사용자 상태 -> 색상
        GradientDrawable background = (GradientDrawable) holder.statusIndicator.getBackground();
        if("online".equals(friend.status)){
            background.setColor(Color.GREEN);
        } else if("busy".equals(friend.status)) {
            background.setColor(Color.RED);
        } else {
            background.setColor(Color.GRAY);
        }
        holder.rankText.setText((position + 1) + "위");
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
