package com.example.swith.Friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swith.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JoinedRoomAdapter adapter;

    private List<String> joinedRoomList = new ArrayList<>();
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    private String myUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
            return;
        }
        myUid = user.getUid();

        recyclerView = findViewById(R.id.roomSelectRecyclerViewSelectRecyclerView);
        adapter = new JoinedRoomAdapter(joinedRoomList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadJoinedRooms();

        Button createRoomButton = findViewById(R.id.createRoomButton);
        createRoomButton.setOnClickListener(v -> {
            Intent intent = new Intent(ScreenActivity.this, StudyRoomActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.createRoomButton).setOnClickListener(v -> createRoom());

        Button rankButton = findViewById(R.id.rank);
        rankButton.setOnClickListener(v -> {
            Intent intent = new Intent(ScreenActivity.this, FriendListActivity.class);
            startActivity(intent);
        });
    }

    private void loadJoinedRooms() {
        dbRef.child("studyRooms").get().addOnSuccessListener(snapshot -> {
            joinedRoomList.clear();
            for (DataSnapshot roomSnap : snapshot.getChildren()) {
                if (roomSnap.child("members").hasChild(myUid)) {
                    joinedRoomList.add(roomSnap.getKey());
                }
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "방 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
        });
    }

    private String generateRoomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rand = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(rand));
        }
        return sb.toString();
    }

    private void createRoom() {
        String roomId = generateRoomCode(10);

        DatabaseReference roomRef = dbRef.child("studyRooms").child(roomId);

        Map<String, Object> membersMap = new HashMap<>();
        membersMap.put(myUid, true);

        Map<String, Object> roomData = new HashMap<>();
        roomData.put("members", membersMap);
        roomData.put("hostId", myUid);
        roomData.put("startTime", 0);
        roomData.put("duration", 0);

        roomRef.setValue(roomData).addOnSuccessListener(unused -> {
            Toast.makeText(this, "방 생성 완료! 코드: " + roomId, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, StudyRoomActivity.class);
            intent.putExtra("roomId", roomId);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "방 생성에 실패했습니다.", Toast.LENGTH_SHORT).show();
        });
    }
}
