package com.example.swith.Friend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// joinedRoomAdapter는 참여중인 방을 acivity_screen.xml 하단에 띄워주는 역할을 함.
public class JoinedRoomAdapter extends RecyclerView.Adapter<JoinedRoomAdapter.RoomViewHolder> {

    private List<String> joinedRoomList;
    private OnItemClickListener listener;

    public JoinedRoomAdapter(List<String> joinedRoomList) {
        this.joinedRoomList = joinedRoomList;
    }

    // 클릭 인터페이스 (필요 시)
    public interface OnItemClickListener {
        void onItemClick(String roomId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        String roomId = joinedRoomList.get(position);
        holder.roomIdTextView.setText(roomId);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(roomId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return joinedRoomList.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomIdTextView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomIdTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
