package com.example.swith.Friend;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swith.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectableFriendAdapter extends RecyclerView.Adapter<SelectableFriendAdapter.FriendViewHolder> {
    private List<User> friends;
    private Set<String> selectedIds = new HashSet<>();

    public SelectableFriendAdapter(List<User> friends) {
        this.friends = friends;
    }

    public Set<String> getSelectedIds() {
        return selectedIds;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rank, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User user = friends.get(position);
        holder.nameText.setText(user.name);
        holder.pointText.setText("점수: " + user.points);

        // 상태 표시
        int color = Color.GRAY;
        if ("online".equals(user.status)) color = Color.GREEN;
        else if ("busy".equals(user.status)) color = Color.RED;
        holder.statusIndicator.setBackgroundColor(color);

        // 선택 시 배경 변경
        holder.itemView.setBackgroundColor(
                selectedIds.contains(user.uid) ? Color.LTGRAY : Color.TRANSPARENT
        );

        holder.itemView.setOnClickListener(v -> {
            if (selectedIds.contains(user.uid)) {
                selectedIds.remove(user.uid);
            } else {
                selectedIds.add(user.uid);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, pointText;
        View statusIndicator;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            pointText = itemView.findViewById(R.id.pointText);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
    }
}
