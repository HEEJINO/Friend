package com.example.swith.Friend;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swith.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<User> friendList = new ArrayList<>();
    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.rankRecyclerView);
        adapter = new FriendAdapter(friendList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            finish();
            return;
        }
        String myUid = user.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/" + myUid + "/friends");
        userRef.get().addOnSuccessListener(snapshot -> {
            List<com.google.android.gms.tasks.Task<DataSnapshot>> tasks = new ArrayList<>();

            for (DataSnapshot child : snapshot.getChildren()) {
                String friendUid = child.getKey();
                com.google.android.gms.tasks.Task<DataSnapshot> task =
                        FirebaseDatabase.getInstance().getReference("users/" + friendUid).get();
                tasks.add(task);
            }

            com.google.android.gms.tasks.Tasks.whenAllSuccess(tasks)
                    .addOnSuccessListener(results -> {
                        friendList.clear();
                        for (Object result : results) {
                            DataSnapshot friendSnap = (DataSnapshot) result;
                            User friend = friendSnap.getValue(User.class);
                            if (friend != null) {
                                friendList.add(friend);
                            }
                        }
                        Collections.sort(friendList, (a, b) -> b.getPoints() - a.getPoints());
                        adapter.notifyDataSetChanged();
                    });
        });

    }
}
