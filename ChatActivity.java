package com.example.swith.Friend;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swith.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView messagesRecyclerView;
    private EditText messageInput;
    private Button sendMessageButton;
    private List<Message> messages;
    private ChatAdapter adapter;
    private DatabaseReference messagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        messages = new ArrayList<>();
        adapter = new ChatAdapter(messages);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(adapter);

        // Firebase 데이터베이스 참조
        messagesRef = FirebaseDatabase.getInstance().getReference("studyRooms/roomA/messages");

        // 실시간 데이터 읽기
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                Collections.sort(messages, (a, b) -> Long.compare(a.timestamp, b.timestamp)); // 시간 순 정렬
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 메시지 전송 버튼 클릭
        sendMessageButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!TextUtils.isEmpty(messageText)) {
                long timestamp = System.currentTimeMillis();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    finish();
                    return;
                }
                String senderId = user.getUid();
                Message message = new Message(senderId, messageText, timestamp);

                // Firebase에 메시지 업로드
                messagesRef.push().setValue(message);

                // 입력 필드 초기화
                messageInput.setText("");
            }
        });
    }
}
