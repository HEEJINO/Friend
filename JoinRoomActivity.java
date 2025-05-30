package com.example.swith.Friend;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swith.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class JoinRoomActivity extends AppCompatActivity {
    private EditText inviteCodeEditText;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user;
    private String myUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            finish();
            return;
        }

        myUid = user.getUid();

        inviteCodeEditText = findViewById(R.id.inviteCodeEditText);

        // intent에서 방 ID 받기
        String roomId = getIntent().getStringExtra("roomId");
        if (roomId != null && roomId.isEmpty()) {
            // 자동 참가 처리
            inviteCodeEditText.setText(roomId);
        }

        findViewById(R.id.joinByCodeButton).setOnClickListener(v -> joinByCode());
        //findViewById(R.id.joinByFriendButton).setOnClickListener(v -> joinByFriend());
    }


    // 입력창에서 수동으로 참가할 때 호출
    private void joinByCode() {
        String code = inviteCodeEditText.getText().toString().trim();
        if (code.isEmpty()) return;
        joinRoomByCode(code);
    }

    private void joinRoomByCode(String code) {
        dbRef.child("studyRooms").child(code).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                dbRef.child("studyRooms").child(code).child("members").child(myUid).setValue(true);
                Toast.makeText(this, "방 참가 완료!", Toast.LENGTH_SHORT).show();

                // 예시: 참가 후 다음 액티비티로 이동
            /*
            Intent intent = new Intent(this, StudyRoomActivity.class);
            intent.putExtra("roomId", code);
            startActivity(intent);
            finish();
            */

            } else {
                Toast.makeText(this, "해당 코드의 방이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void joinByFriend() {
        dbRef.child("studyRooms").get().addOnSuccessListener(snapshot -> {
            for (DataSnapshot roomSnap : snapshot.getChildren()) {
                Map<String, Object> members = (Map<String, Object>) roomSnap.child("members").getValue();
                if (members == null) continue;

                for (String uid : members.keySet()) {
                    if (!uid.equals(myUid)) {
                        // 친구인지 확인
                        dbRef.child("users").child(myUid).child("friends").child(uid)
                                .get().addOnSuccessListener(friendSnap -> {
                                    if (friendSnap.exists()) {
                                        // 친구가 있는 방이므로 참가
                                        dbRef.child("studyRooms").child(roomSnap.getKey()).child("members").child(myUid).setValue(true);
                                        Toast.makeText(this, "친구가 있는 방 참가 완료!", Toast.LENGTH_SHORT).show();
                                        // StudyRoomActivity 등으로 이동
                                    }
                                });
                    }
                }
            }
        });
    }
}

