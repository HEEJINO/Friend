package com.example.swith.Friend;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.swith.R;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private TextView rankTextView;
    private FirebaseHelper firebaseHelper;
    private String myUid;
    private Button createRoomButton;
    //private Button joinRoomButton;

    public FriendFragment() {
        // Required empty public constructor
    }


    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        leaderboardRecyclerView = view.findViewById(R.id.rankRecyclerView);
        rankTextView = view.findViewById(R.id.rankTextView);
        //joinRoomButton = view.findViewById(R.id.joinRoomButton);
        Button createRoomButton = view.findViewById(R.id.createRoomButton);

        firebaseHelper = new FirebaseHelper();
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            rankTextView.setText("로그인 된 사용자 정보가 없음");
            return view;
        }

        myUid = user.getUid();

        firebaseHelper.getLeaderboard(new FirebaseHelper.OnLeaderboardFetchedListener() {
            @Override
            public void onLeaderboardFetched(List<User> leaderboard) {
                leaderboardAdapter = new LeaderboardAdapter(leaderboard);
                leaderboardRecyclerView.setAdapter(leaderboardAdapter);

                for (int i = 0; i < leaderboard.size(); i++) {
                    if (leaderboard.get(i).getUid().equals(myUid)) {
                        rankTextView.setText("내 순위: " + (i + 1) + "위");
                        return;
                    }
                }
                rankTextView.setText("순위 정보 없음");
            }
        });

        createRoomButton.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), StudyRoomActivity.class)));

        //joinRoomButton.setOnClickListener(v ->
        //        startActivity(new Intent(getActivity(), JoinRoomActivity.class)));

        return view;

    }
}