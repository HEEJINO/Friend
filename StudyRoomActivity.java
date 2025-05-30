package com.example.swith.Friend;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swith.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudyRoomActivity extends AppCompatActivity {

    private SetNumberPicker numberPicker;
    private DatabaseReference roomRef;
    private CountDownTimer localTimer;
    private TextView timerTextView;

    private boolean isHost = false;
    private long duration = 0;
    private long startTime = 0;

    private final String roomCode = "AB12CD";
    public String myUid = "uid123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_room);

        Button startButton = findViewById(R.id.startButton);
        TextView timerTextView = findViewById(R.id.timerTextView);

        roomRef = FirebaseDatabase.getInstance().getReference("studyRooms").child(roomCode);

        // 호스트 여부, 기존 타이머 상태 확인
        roomRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                isHost = myUid.equals(snapshot.child("hostId").getValue(String.class));
                duration = snapshot.child("duration").getValue(Long.class);
                startTime = snapshot.child("startTime").getValue(Long.class);

                if (startTime > 0) startCountdown();

                startButton.setEnabled(isHost);
            }
        });

        // ⏱️ NumberPicker 초기화
        NumberPicker picker1 = findViewById(R.id.numberPicker1);
        NumberPicker picker2 = findViewById(R.id.numberPicker2);
        NumberPicker picker3 = findViewById(R.id.numberPicker3);
        numberPicker.setting(5, 59, 59);

        // 🟢 호스트인 경우 버튼 클릭 시 시간 설정 후 시작
        startButton.setOnClickListener(v -> {
            if (isHost) {
                long pickedTime = numberPicker.getTotalMillis();
                duration = pickedTime;
                long now = System.currentTimeMillis();

                roomRef.child("duration").setValue(duration);
                roomRef.child("startTime").setValue(now);
            }
        });

        // 실시간 타이머 반영
        roomRef.child("startTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long start = snapshot.getValue(Long.class);
                if (start != null && start > 0) {
                    startTime = start;
                    startCountdown();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void startCountdown() {
        if (localTimer != null) localTimer.cancel();

        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = duration - elapsed;

        if (remaining <= 0) {
            timerTextView.setText("00:00:00");
            return;
        }

        localTimer = new CountDownTimer(remaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long h = seconds / 3600;
                long m = (seconds % 3600) / 60;
                long s = seconds % 60;
                timerTextView.setText(String.format("%02d:%02d:%02d", h, m, s));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00:00");

                long studied = duration;
                roomRef.child("users").child(myUid).child("totalStudyTime").runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        Long current = currentData.getValue(Long.class);
                        if (current == null) current = 0L;
                        currentData.setValue(current + studied);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@NonNull DatabaseError error, boolean committed, @Nullable DataSnapshot snapshot) {}
                });
            }
        }.start();
    }

    // ... showRanking() 및 formatTime() 생략 (기존 코드 유지)



    private void showRanking() {
        roomRef.child("users").orderByChild("totalStudyTime")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> rankingList = new ArrayList<>();

                        // 내림차순 정렬을 위해 임시 리스트에 추가 후 정렬
                        List<DataSnapshot> users = new ArrayList<>();
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            users.add(userSnap);
                        }

                        Collections.sort(users, (a, b) -> {
                            Long timeA = a.child("totalStudyTime").getValue(Long.class);
                            Long timeB = b.child("totalStudyTime").getValue(Long.class);
                            return Long.compare(timeB != null ? timeB : 0, timeA != null ? timeA : 0);
                        });

                        int rank = 1;
                        for (DataSnapshot userSnap : users) {
                            String name = userSnap.child("nickname").getValue(String.class);
                            Long time = userSnap.child("totalStudyTime").getValue(Long.class);
                            rankingList.add(rank + "위 - " + name + " : " + formatTime(time));
                            rank++;
                        }

                        // 예: TextView나 AlertDialog 등에 출력
                        new AlertDialog.Builder(StudyRoomActivity.this)
                                .setTitle("랭킹")
                                .setItems(rankingList.toArray(new String[0]), null)
                                .setPositiveButton("닫기", null)
                                .show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private String formatTime(Long millis) {
        if (millis == null) millis = 0L;
        long seconds = millis / 1000;
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

}
