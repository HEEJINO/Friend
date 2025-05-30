package com.example.swith.Friend;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseHelper {
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    // 리더보드 데이터를 가져와서 정렬하여 반환
    public void getLeaderboard(final OnLeaderboardFetchedListener listener) {
        database.child("leaderboard").get().addOnSuccessListener(snapshot -> {
            List<User> leaderboard = new ArrayList<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                User user = child.getValue(User.class);
                if (user != null) {
                    user.setUid(child.getKey());  //uid 값 설정
                    leaderboard.add(user);
                }
            }
            // 공부 시간과 점수를 기준으로 내림차순 정렬
            Collections.sort(leaderboard, (user1, user2) -> {
                int comparePoints = Integer.compare(user2.getPoints(), user1.getPoints());
                return comparePoints != 0 ? comparePoints :
                        Integer.compare(user2.getStudyTime(), user1.getStudyTime());
            });
            listener.onLeaderboardFetched(leaderboard);
        });
    }

    // 리더보드를 가져오는 리스너 인터페이스
    public interface OnLeaderboardFetchedListener {
        void onLeaderboardFetched(List<User> leaderboard);
    }
}